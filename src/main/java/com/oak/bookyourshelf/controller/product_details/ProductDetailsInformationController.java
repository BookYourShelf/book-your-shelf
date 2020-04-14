package com.oak.bookyourshelf.controller.product_details;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductDetailsInformationController {

    @GetMapping("/product-details/information")
    public String tab() {

        return "product_details/_information";
    }
}
