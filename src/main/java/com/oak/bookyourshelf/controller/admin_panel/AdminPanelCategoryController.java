package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.model.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class AdminPanelCategoryController {

    @RequestMapping(value = "/admin-panel/category", method = RequestMethod.GET)
    public String tab(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);

        return "admin_panel/_category";
    }

    @RequestMapping(value = "/admin-panel/category", method = RequestMethod.POST)
    public String saveCategory(@Valid @ModelAttribute Category category, Model model) {
        if (!StringUtils.isEmpty(category.getName())) {

        }
        System.out.println("hede");
        return "redirect:/admin-panel#category";
    }
}
