package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.service.HotListDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HotListDetailsController {

    final HotListDetailsService hotListDetailsService;

    public HotListDetailsController(HotListDetailsService hotListDetailsService) {
        this.hotListDetailsService = hotListDetailsService;
    }

    @RequestMapping("/hotList-details/{id}")
    public String showHotListDetailsPage(@PathVariable int id, Model model) {
        HotList hotList = hotListDetailsService.get(id);
        if (hotList == null) {
            return "redirect:/admin-panel";
        }
        model.addAttribute("hotList", hotList);

        return "hotList-details";
    }
}
