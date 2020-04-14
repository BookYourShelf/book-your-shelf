package com.oak.bookyourshelf.controller.profile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileReviewController {

    @GetMapping("/profile/review")
    public String tab() {

        return "profile/_review";
    }
}
