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

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
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
        model.addAttribute("productListEmpty", ((List) adminPanelProductService.listAll()).isEmpty());
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

        List<Image> images = new ArrayList<>();

        if (coverImage == null) {
            return ResponseEntity.badRequest().body("A cover image must be uploaded.");
        } else if (productImages.length > 10) {
            return ResponseEntity.badRequest().body("At most 10 product images can be uploaded.");
        }

        if (!coverImage.isEmpty()) {
            InputStream coverImageStream = coverImage.getInputStream();
            Image img = new Image();
            img.setImage(coverImageStream.readAllBytes());
            images.add(img);
        }

        for (MultipartFile file : productImages) {
            InputStream productImageStream = file.getInputStream();
            Image img = new Image();
            img.setImage(productImageStream.readAllBytes());
            images.add(img);
        }

        switch (productType) {
            case "book":
                physicalBook.setImages(images);
                return bookBarcodeAndISBNCheck(physicalBook, lists, category_name, subcategory_name);

            case "ebook":
                electronicBook.setImages(images);
                return bookBarcodeAndISBNCheck(electronicBook, lists, category_name, subcategory_name);

            case "audio_book":
                audioBook.setImages(images);
                return bookBarcodeAndISBNCheck(audioBook, lists, category_name, subcategory_name);

            case "ebook_reader":
                electronicBookReader.setImages(images);
                return productBarcodeCheck(electronicBookReader);

            case "ebook_reader_case":
                electronicBookReaderCase.setImages(images);
                return productBarcodeCheck(electronicBookReaderCase);

            case "book_case":
                physicalBookCase.setImages(images);
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

        product.setUploadDate(new Timestamp(System.currentTimeMillis()));
        adminPanelProductService.save(product);
        return ResponseEntity.ok("");
    }

    public ResponseEntity<String> bookBarcodeAndISBNCheck(Book product, String lists, String categoryName, String subcategoryName) throws JsonProcessingException {

        // Add lists to product
        ObjectMapper mapper = new ObjectMapper();
        Category category;
        Subcategory subcategory;
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

        product.setUploadDate(new Timestamp(System.currentTimeMillis()));

        category = adminPanelCategoryService.getByName(categoryName);
        product.setCategory(category);


        if (!subcategoryName.equals("")) {      // subcategory selected
            subcategory = adminPanelCategoryService.getSubcategory(category, subcategoryName);
            product.setSubcategory(subcategory);
            subcategory.getBooks().add(product);
        } else {
            category.getBookSet().add(product);
        }

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
