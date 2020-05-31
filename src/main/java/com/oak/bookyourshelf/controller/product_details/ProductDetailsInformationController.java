package com.oak.bookyourshelf.controller.product_details;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public List<Subcategory> findAllSubcategories(@RequestParam String category) {
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
                                                @RequestParam("lists") String lists) throws JsonProcessingException {

        Product product = productDetailsInformationService.get(id);

        switch (buttonType) {
            case "update":
                switch (productType) {
                    case "book":
                        return bookBarcodeAndISBNCheck(physicalBook, lists, product);
                    case "ebook":
                        return bookBarcodeAndISBNCheck(electronicBook, lists, product);
                    case "audio_book":
                        return bookBarcodeAndISBNCheck(audioBook, lists, product);
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

        newProduct = copyProduct(oldProduct, newProduct);
        productDetailsInformationService.save(newProduct);
        return ResponseEntity.ok("");
    }

    public ResponseEntity<String> bookBarcodeAndISBNCheck(Book newProduct, String lists, Product oldProduct)
            throws JsonProcessingException {

        // Add lists to product
        ObjectMapper mapper = new ObjectMapper();
        Map<String, ArrayList<String>> map = mapper.readValue(lists, Map.class);
        newProduct.setPublishers(map.get("publishers"));
        newProduct.setTranslators(map.get("translators"));
        newProduct.setAuthors(map.get("authors"));
        newProduct.setKeywords(map.get("keywords"));

        Product barcodeDb = productDetailsInformationService.getByBarcode(newProduct.getBarcode());
        if (barcodeDb != null && oldProduct.getProductId() != barcodeDb.getProductId()) {
            return ResponseEntity.badRequest().body("Product with given barcode already exists.");
        }

        Product ISBNDb = productDetailsInformationService.getByISBN(newProduct.getIsbn());
        if (ISBNDb != null && oldProduct.getProductId() != ISBNDb.getProductId()) {
            return ResponseEntity.badRequest().body("Product with given ISBN already exists.");
        }

        newProduct = (Book) copyProduct(oldProduct, newProduct);
        productDetailsInformationService.save(newProduct);
        return ResponseEntity.ok("");
    }

    public Product copyProduct(Product oldProduct, Product newProduct) {
        newProduct.setProductId(oldProduct.getProductId());
        newProduct.setSalesNum(oldProduct.getSalesNum());
        newProduct.setTotalStarNum(oldProduct.getTotalStarNum());
        newProduct.setDiscountRate(oldProduct.getDiscountRate());
        newProduct.setOnDiscount(oldProduct.isOnDiscount());
        newProduct.setUploadDate(oldProduct.getUploadDate());
        newProduct.setBuyerUserIds(oldProduct.getBuyerUserIds());
        newProduct.setReviews(oldProduct.getReviews());
        newProduct.setImages(oldProduct.getImages());
        return newProduct;
    }
}
