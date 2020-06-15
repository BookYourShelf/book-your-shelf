package com.oak.bookyourshelf.controller.product_details;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.controller.admin_panel.AdminPanelProductController;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ProductDetailsInformationController {

    final ProductDetailsInformationService productDetailsInformationService;
    final AdminPanelCategoryService adminPanelCategoryService;

    public ProductDetailsInformationController(ProductDetailsInformationService productDetailsInformationService, AdminPanelCategoryService adminPanelCategoryService) {
        this.productDetailsInformationService = productDetailsInformationService;
        this.adminPanelCategoryService = adminPanelCategoryService;
    }

    @RequestMapping(value = "/product-details/information/{id}", method = RequestMethod.GET)
    public String tab(Model model, @PathVariable int id) {

        Product product = productDetailsInformationService.get(id);
        model.addAttribute("product", product);
        model.addAttribute("categoryService", adminPanelCategoryService);

        return "product_details/_information";
    }

    @RequestMapping(value = "/product-details/information/subcategory/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findAllSubcategories(@PathVariable int id, @RequestParam String category) {
        return Globals.getAllSubcategories(adminPanelCategoryService.getByName(category));
    }

    @RequestMapping(value = "/product-details/information/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateProduct(@RequestParam String productType, @PathVariable int id,
                                                @RequestParam String buttonType, PhysicalBook physicalBook,
                                                ElectronicBook electronicBook, AudioBook audioBook,
                                                ElectronicBookReader electronicBookReader,
                                                ElectronicBookReaderCase electronicBookReaderCase,
                                                PhysicalBookCase physicalBookCase,
                                                @RequestParam("lists") String lists,
                                                @RequestParam("category_name") String category_name,
                                                @RequestParam("subcategory_name") String subcategory_name) throws JsonProcessingException {

        Product product = productDetailsInformationService.get(id);

        switch (buttonType) {
            case "update":
                switch (productType) {
                    case "book":
                        return bookBarcodeAndISBNCheck(physicalBook, lists, (Book) product, category_name, subcategory_name);

                    case "ebook":
                        return bookBarcodeAndISBNCheck(electronicBook, lists, (Book) product, category_name, subcategory_name);

                    case "audio_book":
                        return bookBarcodeAndISBNCheck(audioBook, lists, (Book) product, category_name, subcategory_name);

                    case "ebook_reader":
                        return productBarcodeCheck(electronicBookReader, product);

                    case "ebook_reader_case":
                        return productBarcodeCheck(electronicBookReaderCase, product);

                    case "book_case":
                        return productBarcodeCheck(physicalBookCase, product);
                }
                break;

            case "delete":
                productDetailsInformationService.deleteProduct(id);
                return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body("An error occurred.");
    }

    public ResponseEntity<String> productBarcodeCheck(Product newProduct, Product oldProduct) {

        Product productDb = productDetailsInformationService.getByBarcode(newProduct.getBarcode());
        if (productDb != null && oldProduct.getProductId() != productDb.getProductId()) {
            return ResponseEntity.badRequest().body("Product with given barcode already exists.");
        }

        remindProduct(newProduct, oldProduct);
        productDetailsInformationService.save(oldProduct);

        copyProduct(oldProduct, newProduct);
        productDetailsInformationService.save(oldProduct);
        return ResponseEntity.ok("");
    }

    void copyProduct(Product oldProduct, Product newProduct) {
        oldProduct.setProductName(newProduct.getProductName());
        oldProduct.setBarcode(newProduct.getBarcode());
        oldProduct.setStock(newProduct.getStock());
        oldProduct.setPrice(newProduct.getPrice());
        oldProduct.setShortDesc(newProduct.getShortDesc());
        oldProduct.setLongDesc(newProduct.getLongDesc());
    }

    public ResponseEntity<String> bookBarcodeAndISBNCheck(Book newProduct, String lists, Book oldProduct, String categoryName, String subcategoryName)
            throws JsonProcessingException {

        // Add lists to product
        ObjectMapper mapper = new ObjectMapper();
        Category category;
        Subcategory subcategory;
        Map<String, ArrayList<String>> map = mapper.readValue(lists, Map.class);
        oldProduct.setPublishers(AdminPanelProductController.trimList(map.get("publishers")));
        oldProduct.setTranslators(AdminPanelProductController.trimList(map.get("translators")));
        oldProduct.setAuthors(AdminPanelProductController.trimList(map.get("authors")));
        oldProduct.setKeywords(AdminPanelProductController.trimList(map.get("keywords")));

        Product barcodeDb = productDetailsInformationService.getByBarcode(newProduct.getBarcode());
        if (barcodeDb != null && oldProduct.getProductId() != barcodeDb.getProductId()) {
            return ResponseEntity.badRequest().body("Product with given barcode already exists.");
        }

        Product ISBNDb = productDetailsInformationService.getByISBN(newProduct.getIsbn());
        if (ISBNDb != null && oldProduct.getProductId() != ISBNDb.getProductId()) {
            return ResponseEntity.badRequest().body("Product with given ISBN already exists.");
        }

        remindProduct(newProduct, oldProduct);
//        productDetailsInformationService.save(oldProduct);

        category = adminPanelCategoryService.getByName(categoryName);
        oldProduct.setCategory(category);
        // TODO: category books add?

        if (!subcategoryName.equals("") && !subcategoryName.equals("-")) {      // subcategory selected
            subcategory = adminPanelCategoryService.getSubcategory(category, subcategoryName);
            oldProduct.setSubcategory(subcategory);
            subcategory.getBooks().add(oldProduct);
            // TODO: subcategory books add?
        }

        copyBook(oldProduct, newProduct);
        productDetailsInformationService.save(oldProduct);
        return ResponseEntity.ok("");
    }

    public void copyBook(Book oldProduct, Book newProduct) {
        oldProduct.setProductName(newProduct.getProductName());
        oldProduct.setBarcode(newProduct.getBarcode());
        oldProduct.setStock(newProduct.getStock());
        oldProduct.setPrice(newProduct.getPrice());
        oldProduct.setLanguage(newProduct.getLanguage());
        oldProduct.setIsbn(newProduct.getIsbn());
        oldProduct.setAuthors(newProduct.getAuthors());
        oldProduct.setTranslators(newProduct.getTranslators());
        oldProduct.setPublishers(newProduct.getPublishers());
        oldProduct.setKeywords(newProduct.getKeywords());
        oldProduct.setShortDesc(newProduct.getShortDesc());
        oldProduct.setLongDesc(newProduct.getLongDesc());
    }

    public void remindProduct(Product newProduct, Product oldProduct) {
        if (newProduct.getStock() > 0 && oldProduct.getRemind().size() != 0) {
            for (RemindProduct r : oldProduct.getRemind()) {
                r.setProductAvailability(RemindProduct.ProductAvailability.AVAILABLE);
                r.setAvailableTime(new Timestamp(System.currentTimeMillis()));
            }
        }
    }
}
