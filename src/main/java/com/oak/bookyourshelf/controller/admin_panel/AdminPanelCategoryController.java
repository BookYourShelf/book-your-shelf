package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
public class AdminPanelCategoryController {
    final
    AdminPanelCategoryService adminPanelCategoryService;

    public AdminPanelCategoryController(AdminPanelCategoryService adminPanelCategoryService) {
        this.adminPanelCategoryService = adminPanelCategoryService;
    }

    @RequestMapping(value = "/admin-panel/category", method = RequestMethod.GET)
    public String tab(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        model.addAttribute("allCategories", adminPanelCategoryService.listAll());

        return "admin_panel/_category";
    }

    @RequestMapping(value = "/admin-panel/category", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(@RequestParam String name, Category category) {
        if (name != null) {
            ArrayList<Category> categoryDb = (ArrayList<Category>) adminPanelCategoryService.getAllByName(name);
            for (Category cat : categoryDb) {
                if (name.equals(cat.getName()) && cat.getProductType() == category.getProductType()) {
                    return ResponseEntity.badRequest().body("Category name already exists with same product type.");
                }
            }
        }

        adminPanelCategoryService.save(category);
        return ResponseEntity.ok("");
    }
}
