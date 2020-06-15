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

import java.util.*;

@Controller
@SessionAttributes({"subcategoriesBreadcrumbs", "categoryBreadcrumb"})
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
                                     @RequestParam("sort") Optional<String> sort,
                                     Model model, @PathVariable int id) {
        String currentSort = sort.orElse("ID-asc");
        Category category = categoryDetailsInformationService.get(id);
        model.addAttribute("subcategoriesBreadcrumbs", new ArrayList<>()); // Add session breadcrumbs
        model.addAttribute("categoryBreadcrumb", category);
        Set<Subcategory> sub = category.getSubcategories();
        List<Subcategory> subcategories = new ArrayList<>(sub);
        sortSubcategories(subcategories, currentSort);
        Globals.getPageNumbers(page, size, subcategories, model, "subcategoryPage");
        model.addAttribute("allSubcategories", subcategories);
        model.addAttribute("category", category);
        model.addAttribute("sort", currentSort);

        return "category_details/_subcategory";
    }

    @RequestMapping(value = "/category-details/subcategory/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> categoryUpdate(String name, @PathVariable int id) {
        Category category = categoryDetailsInformationService.get(id);
        for (Subcategory sub : category.getSubcategories()) {
            if (sub.getName().equals(name)) {
                return ResponseEntity.badRequest().body("There is a subcategory named " + name + " in " + category.getName());
            }
        }
        Subcategory subcategory = new Subcategory();
        subcategory.setName(name);
        subcategory.setProductType(category.getProductType());
        category.getSubcategories().add(subcategory);
        categoryDetailsInformationService.save(category);

        return ResponseEntity.ok("");
    }

    private void sortSubcategories(List<Subcategory> subcategories, String sort) {
        switch (sort) {
            case "ID-desc":
                subcategories.sort(Comparator.comparingInt(Subcategory::getId).reversed());
                break;

            case "ID-asc":
                subcategories.sort(Comparator.comparingInt(Subcategory::getId));
                break;

            case "name-desc":
                subcategories.sort(Comparator.comparing(Subcategory::getName).reversed());
                break;

            case "name-asc":
                subcategories.sort(Comparator.comparing(Subcategory::getName));
                break;

            case "total-desc":
                subcategories.sort(Comparator.comparingInt(o -> o.getBooks().size()));
                Collections.reverse(subcategories);
                break;

            case "total-asc":
                subcategories.sort(Comparator.comparingInt(o -> o.getBooks().size()));
                break;

            case "subcategory-desc":
                subcategories.sort(Comparator.comparingInt(o -> o.getSubcategories().size()));
                Collections.reverse(subcategories);
                break;

            case "subcategory-asc":
                subcategories.sort(Comparator.comparingInt(o -> o.getSubcategories().size()));
                break;

            default:
                subcategories.sort(Comparator.comparingInt(Subcategory::getId).reversed());
                break;
        }
    }
}
