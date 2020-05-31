package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.service.SubcategoryDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({"subcategoriesBreadcrumbs", "categoryBreadcrumb"})
public class SubcategoryDetailsController {

    final SubcategoryDetailsService subcategoryDetailsService;

    public SubcategoryDetailsController(SubcategoryDetailsService subcategoryDetailsService) {
        this.subcategoryDetailsService = subcategoryDetailsService;
    }

    @RequestMapping(value = "/subcategory-details/{id}", method = RequestMethod.GET)
    public String showCategoryDetailsPage(@ModelAttribute("subcategoriesBreadcrumbs") List<Subcategory> subcategoriesBreadcrumbs,
                                          @PathVariable int id, Model model) {
        Subcategory subcategory = subcategoryDetailsService.get(id);
        if (subcategoriesBreadcrumbs.indexOf(subcategory) == -1) {
            subcategoriesBreadcrumbs.add(subcategory);
        }
        if (subcategory == null) {
            return "redirect:/admin-panel";
        }
        model.addAttribute("subcategory", subcategory);

        return "subcategory-details";
    }

    @RequestMapping(value = "/subcategory-details/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> subcategoryUpdate(@RequestParam String button, String name, @PathVariable int id) {
        switch (button) {
            case "updateSubcategory":
                Subcategory subcategory = subcategoryDetailsService.get(id);
                subcategory.setName(name);
                subcategoryDetailsService.save(subcategory);
                break;
            case "deleteSubcategory":
                subcategoryDetailsService.delete(id);
                break;
        }

        return ResponseEntity.ok("");
    }
}
