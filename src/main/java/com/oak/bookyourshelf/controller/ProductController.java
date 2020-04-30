package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProductController {

    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String showProductPage(Model model, @PathVariable int id) {

        Product product = productService.get(id);
        model.addAttribute("product", product);

        return "product";
    }
}
