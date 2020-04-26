package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.service.ProductDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProductDetailsController {

    final ProductDetailsService productDetailsService;

    public ProductDetailsController(ProductDetailsService productDetailsService) {
        this.productDetailsService = productDetailsService;
    }

    @RequestMapping(value = "/product-details/{id}", method = RequestMethod.GET)
    public String showProduct(Model model, @PathVariable int id) {

        Product product = productDetailsService.get(id);
        model.addAttribute("product", product);

        return "product-details";
    }
}
