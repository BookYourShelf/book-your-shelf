package com.oak.bookyourshelf.controller.category_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.service.category_details.CategoryDetailsInformationService;
import com.oak.bookyourshelf.service.category_details.CategoryDetailsSubcategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryDetailsSubcategoryController {

    final CategoryDetailsSubcategoryService categoryDetailsSubcategoryService;
    final CategoryDetailsInformationService categoryDetailsInformationService;

    public CategoryDetailsSubcategoryController(CategoryDetailsSubcategoryService categoryDetailsSubcategoryService, CategoryDetailsInformationService categoryDetailsInformationService) {
        this.categoryDetailsSubcategoryService = categoryDetailsSubcategoryService;
        this.categoryDetailsInformationService = categoryDetailsInformationService;
    }

    @RequestMapping(value = "/category-details/subcategory/{id}", method = RequestMethod.GET)
    public String showAdminPanelPage(@RequestParam("page") Optional<Integer> page,
                                     @RequestParam("size") Optional<Integer> size,
                                     Model model, @PathVariable int id) {
        Category category = categoryDetailsInformationService.get(id);
        Globals.getPageNumbers(page, size, (List) category.getSubcategories(), model, "subcategoryPage");
        List<Subcategory> subcategories = category.getSubcategories();
        model.addAttribute("allSubcategories", subcategories);
        model.addAttribute("category", category);

        return "category_details/_subcategory";
    }

    @RequestMapping(value = "/category-details/subcategory/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> categoryUpdate(String name, @PathVariable int id) {
        Category category = categoryDetailsInformationService.get(id);
        Subcategory subcategory = new Subcategory();
        subcategory.setName(name);
        category.getSubcategories().add(subcategory);
        categoryDetailsInformationService.save(category);

        return ResponseEntity.ok("");
    }
}
