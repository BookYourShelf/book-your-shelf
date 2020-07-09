package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.service.admin_panel.AdminPanelUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminPanelController {

    final AdminPanelUserService adminPanelUserService;

    public AdminPanelController(AdminPanelUserService adminPanelUserService) {
        this.adminPanelUserService = adminPanelUserService;
    }

    @RequestMapping(value = "/admin-panel", method = RequestMethod.GET)
    public String showAdminPanelPage(Model model) {

        return "admin-panel";
    }
}
