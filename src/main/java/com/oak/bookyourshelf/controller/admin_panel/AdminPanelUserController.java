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
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort, Model model) {

        String currentSort = sort.orElse("ID-desc");
        Globals.getPageNumbers(page, size, adminPanelUserService.sortUsers(currentSort), model, "userPage");
        model.addAttribute("sort", currentSort);
        model.addAttribute("userListEmpty",((List)adminPanelUserService.listAllCustomers()).isEmpty());
        return "admin_panel/_user";
    }

    @RequestMapping(value = "/admin-panel/user", method = RequestMethod.POST)
    @ResponseBody
    public String users(@ModelAttribute List<User> allUsers) {
        return "redirect:/user-details";
    }
}
