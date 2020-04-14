package com.oak.bookyourshelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPanelController {
    @RequestMapping("/admin-panel")
    public String showAdminPanelPage(Model model) {

        return "admin-panel";
    }
}
