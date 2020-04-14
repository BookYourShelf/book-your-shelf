package com.oak.bookyourshelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CategoryDetailsController {

    @RequestMapping("/category-details")
    public String showAdminPanelPage(Model model) {

        return "category-details";
    }
}
