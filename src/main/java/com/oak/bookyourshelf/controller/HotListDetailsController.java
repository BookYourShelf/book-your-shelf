package com.oak.bookyourshelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HotListDetailsController {

    @RequestMapping("/hot-list-details")
    public String showAdminPanelPage(Model model) {

        return "hot-list-details";
    }
}
