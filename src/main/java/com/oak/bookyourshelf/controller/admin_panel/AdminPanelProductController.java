package com.oak.bookyourshelf.controller.admin_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AdminPanelProductController {

    final AdminPanelProductService adminPanelProductService;
    final AdminPanelCategoryService adminPanelCategoryService;

    public AdminPanelProductController(AdminPanelProductService adminPanelProductService, AdminPanelCategoryService adminPanelCategoryService) {
        this.adminPanelProductService = adminPanelProductService;
        this.adminPanelCategoryService = adminPanelCategoryService;
    }

    @RequestMapping(value = "/admin-panel/product", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort,
                      @RequestParam("filter") Optional<String> filter, Model model) {

        String currentSort = sort.orElse("date");
        String currentFilter = filter.orElse("all");
        Globals.getPageNumbers(page, size, filterProducts(adminPanelProductService.sortProducts(currentSort), currentFilter), model, "productPage");
        model.addAttribute("allProducts", adminPanelProductService.listAll());
        model.addAttribute("categoryService", adminPanelCategoryService);
        model.addAttribute("sort", currentSort);
        model.addAttribute("filter", currentFilter);
        return "admin_panel/_product";
    }

    @RequestMapping(value = "/admin-panel/product/subcategory", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findAllSubcategories(@RequestParam String category) {
        List<String> subcategories = Globals.getAllSubcategories(adminPanelCategoryService.getByName(category));
        return subcategories;
    }

    @RequestMapping(value = "/admin-panel/product", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<String> saveProduct(@RequestParam String productType, PhysicalBook physicalBook,
                                              ElectronicBook electronicBook, AudioBook audioBook,
                                              ElectronicBookReader electronicBookReader,
                                              ElectronicBookReaderCase electronicBookReaderCase,
                                              PhysicalBookCase physicalBookCase,
                                              @RequestParam("lists") String lists,
                                              @RequestParam("category_name") String category_name,
                                              @RequestParam("subcategory_name") String subcategory_name,
                                              @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
                                              @RequestParam(value = "productImages", required = false) MultipartFile[] productImages) throws IOException {
        Category category;
        Subcategory subcategory;
        List<Image> images = new ArrayList<>();

        if (coverImage == null) {
            return ResponseEntity.badRequest().body("A cover image must be uploaded.");
        } else if (productImages.length > 10) {
            return ResponseEntity.badRequest().body("At most 10 product images can be uploaded.");
        }

        if (!coverImage.isEmpty()) {
            FileInputStream coverImageStream = (FileInputStream) coverImage.getInputStream();
            Image img = new Image();
            img.setImage(coverImageStream.readAllBytes());
            images.add(img);
        }

        for (MultipartFile file : productImages) {
            FileInputStream productImageStream = (FileInputStream) file.getInputStream();
            Image img = new Image();
            img.setImage(productImageStream.readAllBytes());
            images.add(img);
        }

        switch (productType) {
            case "book":
                category = adminPanelCategoryService.getByName(category_name);
                subcategory = adminPanelCategoryService.getSubcategory(category, subcategory_name);
                physicalBook.setCategory(new ArrayList<Category>());
                physicalBook.getCategory().add(category);
                physicalBook.setSubcategory(new ArrayList<Subcategory>());
                physicalBook.getSubcategory().add(subcategory);
                physicalBook.setImages(images);
                return bookBarcodeAndISBNCheck(physicalBook, lists, category, subcategory);
            case "ebook":
                category = adminPanelCategoryService.getByName(category_name);
                subcategory = adminPanelCategoryService.getSubcategory(category, subcategory_name);
                electronicBook.setCategory(new ArrayList<Category>());
                electronicBook.getCategory().add(category);
                electronicBook.setSubcategory(new ArrayList<Subcategory>());
                electronicBook.getSubcategory().add(subcategory);
                electronicBook.setImages(images);
                return bookBarcodeAndISBNCheck(electronicBook, lists, category, subcategory);
            case "audio_book":
                category = adminPanelCategoryService.getByName(category_name);
                subcategory = adminPanelCategoryService.getSubcategory(category, subcategory_name);
                audioBook.setCategory(new ArrayList<Category>());
                audioBook.getCategory().add(category);
                audioBook.setSubcategory(new ArrayList<Subcategory>());
                audioBook.getSubcategory().add(subcategory);
                audioBook.setImages(images);
                return bookBarcodeAndISBNCheck(audioBook, lists, category, subcategory);
            case "ebook_reader":
                return productBarcodeCheck(electronicBookReader);
            case "ebook_reader_case":
                return productBarcodeCheck(electronicBookReaderCase);
            case "book_case":
                return productBarcodeCheck(physicalBookCase);
            default:
                return ResponseEntity.badRequest().body("An error occurred.");
        }
    }

    public ResponseEntity<String> productBarcodeCheck(Product product) {

        Product productDb = adminPanelProductService.getByBarcode(product.getBarcode());
        if (productDb != null) {
            return ResponseEntity.badRequest().body("Product with given barcode already exists.");
        }

        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        product.setUploadDate(sqlDate);
        adminPanelProductService.save(product);
        return ResponseEntity.ok("");
    }

    public ResponseEntity<String> bookBarcodeAndISBNCheck(Book product, String lists, Category category, Subcategory subcategory) throws JsonProcessingException {

        // Add lists to product
        ObjectMapper mapper = new ObjectMapper();
        Map<String, ArrayList<String>> map = mapper.readValue(lists, Map.class);
        product.setPublishers(trimList(map.get("publishers")));
        product.setTranslators(trimList(map.get("translators")));
        product.setAuthors(trimList(map.get("authors")));
        product.setKeywords(trimList(map.get("keywords")));

        Product barcodeDb = adminPanelProductService.getByBarcode(product.getBarcode());
        if (barcodeDb != null) {
            return ResponseEntity.badRequest().body("Product with given barcode already exists.");
        }

        Product ISBNDb = adminPanelProductService.getByISBN(product.getIsbn());
        if (ISBNDb != null) {
            return ResponseEntity.badRequest().body("Product with given ISBN already exists.");
        }

        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        product.setUploadDate(sqlDate);
        category.getBooks().add(product);
        subcategory.getBooks().add(product);
        adminPanelProductService.save(product);
        return ResponseEntity.ok("");
    }

    public static List<String> trimList(List<String> list) {
        List<String> trimmedList = list.stream().map(String::trim).collect(Collectors.toList());
        trimmedList.removeIf(s -> s.equals(""));
        return trimmedList;
    }

    public List<Product> filterProducts(List<Product> products, String productType) {
        switch (productType) {
            case "book":
                return products.stream().filter(p -> p instanceof PhysicalBook).collect(Collectors.toList());
            case "e-book":
                return products.stream().filter(p -> p instanceof ElectronicBook).collect(Collectors.toList());
            case "audio-book":
                return products.stream().filter(p -> p instanceof AudioBook).collect(Collectors.toList());
            case "e-book-reader":
                return products.stream().filter(p -> p instanceof ElectronicBookReader).collect(Collectors.toList());
            case "e-book-reader-case":
                return products.stream().filter(p -> p instanceof ElectronicBookReaderCase).collect(Collectors.toList());
            case "book-case":
                return products.stream().filter(p -> p instanceof PhysicalBookCase).collect(Collectors.toList());
            default:
                return products;
        }
    }
}
