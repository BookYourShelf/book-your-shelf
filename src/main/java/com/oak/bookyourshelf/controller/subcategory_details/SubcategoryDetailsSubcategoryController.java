package com.oak.bookyourshelf.controller.subcategory_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.service.subcategory_details.SubcategoryDetailsInformationService;
import com.oak.bookyourshelf.service.subcategory_details.SubcategoryDetailsSubcategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SubcategoryDetailsSubcategoryController {

    final SubcategoryDetailsSubcategoryService subcategoryDetailsSubcategoryService;
    final SubcategoryDetailsInformationService subcategoryDetailsInformationService;

    public SubcategoryDetailsSubcategoryController(SubcategoryDetailsSubcategoryService subcategoryDetailsSubcategoryService, SubcategoryDetailsInformationService subcategoryDetailsInformationService) {
        this.subcategoryDetailsSubcategoryService = subcategoryDetailsSubcategoryService;
        this.subcategoryDetailsInformationService = subcategoryDetailsInformationService;
    }

    @RequestMapping(value = "/subcategory-details/subcategory/{id}", method = RequestMethod.GET)
    public String showAdminPanelPage(@RequestParam("page") Optional<Integer> page,
                                     @RequestParam("size") Optional<Integer> size,
                                     Model model, @PathVariable int id) {
        Subcategory subcategory = subcategoryDetailsInformationService.get(id);
        model.addAttribute("subcategoriesBreadcrumbs", new ArrayList<>()); // Add session breadcrumbs
        model.addAttribute("categoryBreadcrumb", subcategory);
        Globals.getPageNumbers(page, size, (List) subcategory.getSubcategories(), model, "subcategoryPage");
        List<Subcategory> subcategories = subcategory.getSubcategories();
        model.addAttribute("allSubcategories", subcategories);
        model.addAttribute("category", subcategory);

        return "subcategory_details/_subcategory";
    }

    @RequestMapping(value = "/subcategory-details/subcategory/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> categoryUpdate(String name, @PathVariable int id) {
        Subcategory subcategory = subcategoryDetailsInformationService.get(id);
        Subcategory newSubcategory = new Subcategory();
        newSubcategory.setName(name);
        subcategory.getSubcategories().add(newSubcategory);
        subcategoryDetailsInformationService.save(subcategory);

        return ResponseEntity.ok("");
    }
}
