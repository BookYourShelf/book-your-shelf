package com.oak.bookyourshelf.controller.subcategory_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.service.subcategory_details.SubcategoryDetailsInformationService;
import com.oak.bookyourshelf.service.subcategory_details.SubcategoryDetailsSubcategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@SessionAttributes({"subcategoriesBreadcrumbs", "categoryBreadcrumb"})
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
                                     @RequestParam("sort") Optional<String> sort,
                                     Model model, @PathVariable int id) {
        String currentSort = sort.orElse("ID-asc");
        Subcategory subcategory = subcategoryDetailsInformationService.get(id);

        ArrayList<Subcategory> subcategoriesBreadcrumbs = (ArrayList<Subcategory>) model.getAttribute("subcategoriesBreadcrumbs");

        model.addAttribute("subcategoriesBreadcrumbs", subcategoriesBreadcrumbs);
        model.addAttribute("categoryBreadcrumb", model.getAttribute("categoryBreadcrumb"));


        Set<Subcategory> s = subcategory.getSubcategories();
        List<Subcategory> subcategories = new ArrayList<>(s);
        sortSubcategories(subcategories, currentSort);
        Globals.getPageNumbers(page, size, subcategories, model, "subcategoryPage");
        model.addAttribute("allSubcategories", subcategories);
        model.addAttribute("subcategory", subcategory);
        model.addAttribute("sort", currentSort);

        return "subcategory_details/_subcategory";
    }

    @RequestMapping(value = "/subcategory-details/subcategory/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> categoryUpdate(String name, @PathVariable int id) {
        Subcategory subcategory = subcategoryDetailsInformationService.get(id);
        Subcategory newSubcategory = new Subcategory();
        newSubcategory.setName(name);
        newSubcategory.setProductType(subcategory.getProductType());
        subcategory.getSubcategories().add(newSubcategory);
        subcategoryDetailsInformationService.save(subcategory);

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
