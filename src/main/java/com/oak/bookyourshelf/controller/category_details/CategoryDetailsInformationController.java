package com.oak.bookyourshelf.controller.category_details;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CategoryDetailsInformationController {

    @RequestMapping("/category-details/information")
    public String showAdminPanelPage(Model model) {

        return "category_details/_information";
    }
}
