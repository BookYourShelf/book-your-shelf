package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserDetailsController {

    final UserDetailsInformationService userDetailsInformationService;

    public UserDetailsController(UserDetailsInformationService userDetailsInformationService) {
        this.userDetailsInformationService = userDetailsInformationService;
    }

    @RequestMapping(value = "/user-details/{id}", method = RequestMethod.GET)
    public String showUser(@PathVariable int id, Model model) {

        User user = userDetailsInformationService.get(id);
        model.addAttribute("user", user);
        return "user-details";
    }
}
