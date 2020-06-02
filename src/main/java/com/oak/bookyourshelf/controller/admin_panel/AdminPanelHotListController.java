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
        return Globals.getAllChildSubcategories(adminPanelCategoryService.getByName(category));
    }



    @RequestMapping(value = "/admin-panel/hotList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(@RequestParam String category, String subctgry, String ptype ,String htype, HotList hotList) {
        System.out.println("post method");
        List<HotList> sameType =adminPanelHotListService.findAllByProductType(hotList.getProductType());
        List<Category> newCategoryList = new ArrayList<>();
        List<Subcategory> newSubcategories = new ArrayList<Subcategory>();

        String[] start = hotList.getStartDate().split("/");
        String[] end = hotList.getEndDate().split("/");
        String[] start_time = hotList.getStartTime().split(":");
        String[] end_time = hotList.getEndTime().split(":");

        List<String> startTime=Arrays.asList(start_time);
        List<String> endTime = Arrays.asList(end_time);
        List<String> startDate =Arrays.asList(start);
        List<String> endDate = Arrays.asList(end);

        if( !adminPanelHotListService.isDateValid(startDate))
            return ResponseEntity.badRequest().body("Start date is not valid");
        if(!adminPanelHotListService.isDateValid(endDate))
            return ResponseEntity.badRequest().body("End date is not valid");
        if(adminPanelHotListService.isDateValid(startDate) && adminPanelHotListService.isDateValid(endDate))
        {
            if(!adminPanelHotListService.isDateCorrect(endDate,startDate))
                return ResponseEntity.badRequest().body("End date cannot be smaller than start date");
            else{
                if(!adminPanelHotListService.isTimeValid(startTime))
                    return ResponseEntity.badRequest().body("Start time is not valid");
                if(!adminPanelHotListService.isTimeValid(endTime))
                    return ResponseEntity.badRequest().body("End time is not valid");
                if(startDate.get(0).equals(endDate.get(0)) && startDate.get(1).equals(endDate.get(1)) && startDate.get(2).equals(endDate.get(2)))
                {
                    if(!adminPanelHotListService.isTimeCorrect(startTime,endTime))
                        return ResponseEntity.badRequest().body("If dates are the same , end time can not be smaller than start time");
                }
            }
        }



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

                for (String s : subcategories) {
                    Subcategory newSubcategory = adminPanelCategoryService.getSubcategory(newCategory,s);
                    if(newCategory != null){
                        newSubcategories.add(newSubcategory);}
                }

                for (HotList i : sameType) {
                    Category hotListCategory =i.getCategories().get(0);
                    if(hotListCategory.getName().equals(category)) {
                        for (Subcategory s : newSubcategories) {
                            if(s.isInHotList())
                                return ResponseEntity.badRequest().body("There is a hot list in " + s.getName() +" subcategory . Please change your selection");
                        }
                    }
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
        /*adminPanelHotListService.setProductByType(hotList,htype,adminPanelHotListService.createProductSet(newSubcategories));*/
        adminPanelHotListService.save(hotList);


        return ResponseEntity.ok("");

    }
}
