package com.oak.bookyourshelf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.HotListDetailsService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HotListDetailsController {

    final HotListDetailsService hotListDetailsService;
    final AdminPanelCategoryService adminPanelCategoryService;

    public HotListDetailsController(HotListDetailsService hotListDetailsService, AdminPanelCategoryService adminPanelCategoryService) {
        this.hotListDetailsService = hotListDetailsService;
        this.adminPanelCategoryService = adminPanelCategoryService;
    }

    @RequestMapping("/hotList-details/{id}")
    public String showHotListDetailsPage(@PathVariable int id, Model model) {
        HotList hotList = hotListDetailsService.get(id);
        if (hotList == null) {
            return "redirect:/admin-panel";
        }
        model.addAttribute("hotList", hotList);
        model.addAttribute("categoryService", adminPanelCategoryService);

        return "hotList-details";
    }

    @RequestMapping(value = "/hotList-details/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateHotList(@PathVariable int id, @RequestParam String button, HotList newHotList)  {

        HotList hotList = hotListDetailsService.get(id);
        System.out.println("update");
        switch (button) {
            case "update_hotList":
                hotList.setCategories(newHotList.getCategories());
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
                hotListDetailsService.delete(id);
                return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body("An error occurred.");
    }
}
