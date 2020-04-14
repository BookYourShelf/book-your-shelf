package com.oak.bookyourshelf.controller.profile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileInformationController {

    @GetMapping("/profile/information")
    public String tab() {

        return "profile/_information";
    }
}
