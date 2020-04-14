package com.oak.bookyourshelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserDetailsController {

    @RequestMapping("/user-details")
    public String showUserDetailsPage(Model model) {

        return "user-details";
    }
}
