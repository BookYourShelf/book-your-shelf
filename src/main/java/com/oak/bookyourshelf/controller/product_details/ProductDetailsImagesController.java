package com.oak.bookyourshelf.controller.product_details;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductDetailsImagesController {

    @GetMapping("/product-details/images")
    public String tab() {

        return "_images";
    }
}
