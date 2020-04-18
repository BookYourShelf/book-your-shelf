package com.oak.bookyourshelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PolicyController {

    @RequestMapping(value = "/privacy_policy", method = RequestMethod.GET)
    public String showPolicyPage(Model model) {
        return "privacy_policy";
    }
}
