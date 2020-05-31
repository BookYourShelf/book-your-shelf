package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelHotListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AdminPanelHotListController {
    final AdminPanelCategoryService adminPanelCategoryService;
    final AdminPanelHotListService adminPanelHotListService;

    public AdminPanelHotListController(AdminPanelCategoryService adminPanelCategoryService, AdminPanelHotListService adminPanelHotListService) {
        this.adminPanelCategoryService = adminPanelCategoryService;
        this.adminPanelHotListService = adminPanelHotListService;
    }

    @RequestMapping(value = "/admin-panel/hotList", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size, Model model) {

        Globals.getPageNumbers(page, size, (List) adminPanelHotListService.listAll(), model, "hotListPage");

        HotList hotList = new HotList();
        model.addAttribute("hotList", hotList);
        model.addAttribute("categoryService", adminPanelCategoryService);


        return "admin_panel/_hotList";
    }


    @RequestMapping(value = "/admin-panel/hotList/category", method = RequestMethod.GET)
    @ResponseBody
    public List<Category> findAllCategories(@RequestParam String category) {

        if (category.equals("BOOK")) {
            System.out.println("mkdfg");
            System.out.println(adminPanelCategoryService.getAllByCategory("Book"));
            return  (List<Category>) adminPanelCategoryService.getAllByCategory("Book");
        }
        else if(category.equals("E_BOOK"))
            return  (List<Category>) adminPanelCategoryService.getAllByCategory("E-Book");
        else if(category.equals("AUDIO_BOOK"))
            return  (List<Category>) adminPanelCategoryService.getAllByCategory("Audio Book");
        else return null;

    }

    @RequestMapping(value = "/admin-panel/hotList/subcategory", method = RequestMethod.GET)
    @ResponseBody
    public List<Subcategory> findAllSubcategories(@RequestParam String category) {
        return Globals.getAllSubcategories(adminPanelCategoryService.getByName(category));
    }



    @RequestMapping(value = "/admin-panel/hotList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(@RequestParam String category, String subctgry, String ptype , HotList hotList) {
        System.out.println("post method");
        List<HotList> sameType =adminPanelHotListService.findAllByProductType(hotList.getProductType());
        List<Category> newCategoryList = new ArrayList<>();
        List<Subcategory> newSubcategories = new ArrayList<Subcategory>();
        if(ptype.equals("BOOK") || ptype.equals("E_BOOK") || ptype.equals("AUDIO_BOOK"))
        {
            Category newCategory = adminPanelCategoryService.getByName(category);

            newCategoryList.add(newCategory);
            if(subctgry.equals(""))
            {
                for (HotList i : sameType) {
                    Category hotListCategory =i.getCategories().get(0);
                    if(hotListCategory.getName().equals(category)) {

                        return ResponseEntity.badRequest().body("There is a  hot list in " + category +" category . Please change your selection");

                    }
                }
                hotList.setCategories(newCategoryList);
            }

            else{
                String[] subcategory = subctgry.split("-");
                List<String> subcategories = Arrays.asList(subcategory);

                for (HotList i : sameType) {
                    Category hotListCategory =i.getCategories().get(0);
                    if(hotListCategory.getName().equals(category)) {
                        for (String s : subcategories) {
                            for(Subcategory sub : i.getSubcategories())
                                if (sub.getName().equals(s)) {
                                    return ResponseEntity.badRequest().body("There is a hot list in " + s +" subcategory . Please change your selection");
                                }
                        }
                    }
                }


                for (String s : subcategories) {
                    Subcategory newSubcategory = adminPanelCategoryService.getSubcategory(newCategory,s);
                    newSubcategories.add(newSubcategory);
                }

                hotList.setSubcategories(newSubcategories);
                hotList.setCategories(newCategoryList);
            }
        }

        else
        {
            if(sameType.size()>0)
                return ResponseEntity.badRequest().body("There is a hot list in " + ptype +" product type . Please change your selection");

            else{
                hotList.setSubcategories(newSubcategories);
                hotList.setCategories(newCategoryList);
            }
        }

        adminPanelHotListService.save(hotList);


        return ResponseEntity.ok("");

    }
}
