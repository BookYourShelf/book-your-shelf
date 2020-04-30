package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.service.admin_panel.AdminPanelUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.oak.bookyourshelf.model.User;

import java.util.List;

@Controller
public class AdminPanelUserController {

    final AdminPanelUserService adminPanelUserService;

    public AdminPanelUserController(AdminPanelUserService adminPanelUserService) {
        this.adminPanelUserService = adminPanelUserService;
    }

    @RequestMapping(value = "/admin-panel/user", method = RequestMethod.GET)
    public String tab(Model model) {
        model.addAttribute("allUsers" , adminPanelUserService.listAllCustomers());
        System.out.println(adminPanelUserService.listAllCustomers());
        return "admin_panel/_user";
    }

    @RequestMapping(value = "/admin-panel/user", method = RequestMethod.POST)
    @ResponseBody
    public String users(@ModelAttribute  List<User> allUsers) {
        return "redirect:/user-details";
    }
}
