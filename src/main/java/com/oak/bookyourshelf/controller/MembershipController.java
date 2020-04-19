package com.oak.bookyourshelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MembershipController {

    @RequestMapping(value= "/membership", method = RequestMethod.GET)
    public String showMembershipPage(Model model) {
        return "membership";
    }

}
