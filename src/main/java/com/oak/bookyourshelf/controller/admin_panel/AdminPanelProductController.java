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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                      @RequestParam("size") Optional<Integer> size, Model model) {

        Globals.getPageNumbers(page, size, (List) adminPanelProductService.listAll(), model, "productPage");
        model.addAttribute("allProducts", adminPanelProductService.listAll());
        model.addAttribute("categoryService", adminPanelCategoryService);
        return "admin_panel/_product";
    }

    @RequestMapping(value = "/admin-panel/product/subcategory", method = RequestMethod.GET)
    @ResponseBody
    public List<Subcategory> findAllSubcategories(@RequestParam String category) {
        return Globals.getAllSubcategories(adminPanelCategoryService.getByName(category));
    }

    @RequestMapping(value = "/admin-panel/product", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveProduct(@RequestParam String productType, PhysicalBook physicalBook,
                                              ElectronicBook electronicBook, AudioBook audioBook,
                                              ElectronicBookReader electronicBookReader,
                                              ElectronicBookReaderCase electronicBookReaderCase,
                                              PhysicalBookCase physicalBookCase,
                                              @RequestParam("lists") String lists) throws JsonProcessingException {

        switch (productType) {
            case "book":
                return bookBarcodeAndISBNCheck(physicalBook, lists);
            case "ebook":
                return bookBarcodeAndISBNCheck(electronicBook, lists);
            case "audio_book":
                return bookBarcodeAndISBNCheck(audioBook, lists);
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

    public ResponseEntity<String> bookBarcodeAndISBNCheck(Book product, String lists) throws JsonProcessingException {

        // Add lists to product
        ObjectMapper mapper = new ObjectMapper();
        Map<String, ArrayList<String>> map = mapper.readValue(lists, Map.class);
        product.setPublishers(map.get("publishers"));
        product.setTranslators(map.get("translators"));
        product.setAuthors(map.get("authors"));
        product.setKeywords(map.get("keywords"));

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
        adminPanelProductService.save(product);
        return ResponseEntity.ok("");
    }
}
