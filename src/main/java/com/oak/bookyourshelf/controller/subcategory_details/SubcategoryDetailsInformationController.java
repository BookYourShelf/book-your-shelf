package com.oak.bookyourshelf.controller.subcategory_details;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.service.subcategory_details.SubcategoryDetailsInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@SessionAttributes({"subcategoriesBreadcrumbs", "categoryBreadcrumb"})
public class SubcategoryDetailsInformationController {

    final SubcategoryDetailsInformationService subcategoryDetailsInformationService;

    public SubcategoryDetailsInformationController(SubcategoryDetailsInformationService subcategoryDetailsInformationService) {
        this.subcategoryDetailsInformationService = subcategoryDetailsInformationService;
    }

    @RequestMapping(value = "/subcategory-details/information/{id}", method = RequestMethod.GET)
    public String showAdminPanelPage(Model model, @PathVariable int id) {
        Subcategory subcategory = subcategoryDetailsInformationService.get(id);
        model.addAttribute("subcategory", subcategory);
        ArrayList<Subcategory> subcategoriesBreadcrumbs = (ArrayList<Subcategory>) model.getAttribute("subcategoriesBreadcrumbs");

        model.addAttribute("subcategoriesBreadcrumbs", subcategoriesBreadcrumbs);
        model.addAttribute("categoryBreadcrumb", model.getAttribute("categoryBreadcrumb"));

        return "subcategory_details/_information";
    }

    @RequestMapping(value = "/subcategory-details/information/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> categoryUpdate(@RequestParam String button, @PathVariable int id, Category category) {
        switch (button) {
            case "updateCategory":
                Subcategory subcategoryDb = subcategoryDetailsInformationService.get(id);
                subcategoryDb.setName(category.getName());
                subcategoryDetailsInformationService.save(subcategoryDb);
                break;
            case "deleteCategory":
                subcategoryDetailsInformationService.delete(id);
        }

        return ResponseEntity.ok("");
    }
}
