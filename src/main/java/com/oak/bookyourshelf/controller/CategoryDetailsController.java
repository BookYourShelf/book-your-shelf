package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.service.CategoryDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CategoryDetailsController {

    final CategoryDetailsService categoryDetailsService;

    public CategoryDetailsController(CategoryDetailsService categoryDetailsService) {
        this.categoryDetailsService = categoryDetailsService;
    }

    @RequestMapping("/category-details/{id}")
    public String showCategoryDetailsPage(@PathVariable int id, Model model) {
        Category category = categoryDetailsService.get(id);
        if (category == null) {
            return "redirect:/admin-panel";
        }
        model.addAttribute("category", category);

        return "category-details";
    }
}
