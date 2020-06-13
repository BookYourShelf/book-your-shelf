package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.HotListDetailsService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelHotListService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class HotListDetailsController {

    final HotListDetailsService hotListDetailsService;
    final AdminPanelCategoryService adminPanelCategoryService;
    final AdminPanelHotListService adminPanelHotListService;

    public HotListDetailsController(HotListDetailsService hotListDetailsService, AdminPanelCategoryService adminPanelCategoryService, AdminPanelHotListService adminPanelHotListService) {
        this.hotListDetailsService = hotListDetailsService;
        this.adminPanelCategoryService = adminPanelCategoryService;
        this.adminPanelHotListService = adminPanelHotListService;
    }

    @RequestMapping("/hotList-details/{id}")
    public String showHotListDetailsPage(@PathVariable int id, Model model) {
        HotList hotList = hotListDetailsService.get(id);
        if (hotList == null) {
            return "redirect:/admin-panel";
        }
        model.addAttribute("hotList", hotList);
        model.addAttribute("productType", adminPanelHotListService.createProductType(hotList));
        model.addAttribute("hotListType", adminPanelHotListService.createHotListType(hotList));
        model.addAttribute("categoryService", adminPanelCategoryService);

        return "hotList-details";
    }

    @RequestMapping(value = "/hotList-details/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateHotList(@PathVariable int id, @RequestParam String button, HotList newHotList) {

        HotList hotList = hotListDetailsService.get(id);
        System.out.println("update");
        switch (button) {
            case "update_hotList":

                String[] start = newHotList.getStartDate().split("/");
                String[] end = newHotList.getEndDate().split("/");
                String[] start_time = newHotList.getStartTime().split(":");
                String[] end_time = newHotList.getEndTime().split(":");

                List<String> startTime = Arrays.asList(start_time);
                List<String> endTime = Arrays.asList(end_time);
                List<String> startDate = Arrays.asList(start);
                List<String> endDate = Arrays.asList(end);

                if (!adminPanelHotListService.isDateValid(startDate))
                    return ResponseEntity.badRequest().body("Start date is not valid");
                if (!adminPanelHotListService.isDateValid(endDate))
                    return ResponseEntity.badRequest().body("End date is not valid");
                if (adminPanelHotListService.isDateValid(startDate) && adminPanelHotListService.isDateValid(endDate)) {
                    if (!adminPanelHotListService.isDateCorrect(endDate, startDate))
                        return ResponseEntity.badRequest().body("End date cannot be smaller than start date");
                    else {
                        if (!adminPanelHotListService.isTimeValid(startTime))
                            return ResponseEntity.badRequest().body("Start time is not valid");
                        if (!adminPanelHotListService.isTimeValid(endTime))
                            return ResponseEntity.badRequest().body("End time is not valid");
                        if (startDate.get(0).equals(endDate.get(0)) && startDate.get(1).equals(endDate.get(1)) && startDate.get(2).equals(endDate.get(2))) {
                            if (!adminPanelHotListService.isTimeCorrect(startTime, endTime))
                                return ResponseEntity.badRequest().body("If dates are the same , end time can not be smaller than start time");
                        }
                    }
                }

                hotList.setHotListType(newHotList.getHotListType());
                hotList.setEndDate(newHotList.getEndDate());
                hotList.setEndTime(newHotList.getEndTime());
                hotList.setStartDate(newHotList.getStartDate());
                hotList.setStartTime(newHotList.getStartTime());
                hotList.setProductNum(newHotList.getProductNum());
                hotList.setId(id);
                hotListDetailsService.save(hotList);
                return ResponseEntity.ok("");
            case "delete_hotList":
                if (hotList.getProductType() == Category.ProductType.BOOK || hotList.getProductType() == Category.ProductType.E_BOOK || hotList.getProductType() == Category.ProductType.AUDIO_BOOK) {
                    /*campaign.setSubcategories(campaignDetailsService.removeDiscount(campaign.getSubcategories()));*/
                    List<Subcategory> subcategories = new ArrayList<>();
                    for(Subcategory s: hotList.getSubcategories())
                        subcategories.add(s);

                    hotListDetailsService.delete(id);


                    for (Subcategory subcategory : subcategories) {
                      subcategory.setInHotList(false);
                    }

                } else {
                    hotListDetailsService.delete(id);
                }

                return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body("An error occurred.");
    }
}
