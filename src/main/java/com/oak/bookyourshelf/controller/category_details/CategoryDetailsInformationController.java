package com.oak.bookyourshelf.controller.category_details;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.service.category_details.CategoryDetailsInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryDetailsInformationController {

    final CategoryDetailsInformationService categoryDetailsInformationService;

    public CategoryDetailsInformationController(CategoryDetailsInformationService categoryDetailsInformationService) {
        this.categoryDetailsInformationService = categoryDetailsInformationService;
    }

    @RequestMapping(value = "/category-details/information/{id}", method = RequestMethod.GET)
    public String showAdminPanelPage(Model model, @PathVariable int id) {
        Category category = categoryDetailsInformationService.get(id);
        model.addAttribute("category", category);

        return "category_details/_information";
    }

    @RequestMapping(value = "/category-details/information/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> categoryUpdate(@RequestParam String button, @PathVariable int id, Category category) {
        switch (button) {
            case "updateCategory":
                Category categoryDb = categoryDetailsInformationService.get(id);
                categoryDb.setName(category.getName());
                categoryDb.setProductType(category.getProductType());
                categoryDetailsInformationService.save(categoryDb);
                break;
            case "deleteCategory":
                categoryDetailsInformationService.delete(id);
        }

        return ResponseEntity.ok("");
    }
}
