package com.oak.bookyourshelf.controller.product_details;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductDetailsPhotosController {

    @GetMapping("/product-details/photos")
    public String tab() {

        return "product_details/_photos";
    }
}
