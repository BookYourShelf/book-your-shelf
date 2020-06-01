package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.oak.bookyourshelf.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AdminPanelUserController {

    final AdminPanelUserService adminPanelUserService;

    public AdminPanelUserController(AdminPanelUserService adminPanelUserService) {
        this.adminPanelUserService = adminPanelUserService;
    }

    @RequestMapping(value = "/admin-panel/user", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size, Model model) {


        Globals.getPageNumbers(page, size, (List) adminPanelUserService.listAllCustomers(), model, "userPage");

        return "admin_panel/_user";
    }

    @RequestMapping(value = "/admin-panel/user", method = RequestMethod.POST)
    @ResponseBody
    public String users(@ModelAttribute List<User> allUsers) {
        return "redirect:/user-details";
    }
}
