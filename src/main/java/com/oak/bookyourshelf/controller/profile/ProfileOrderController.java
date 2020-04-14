package com.oak.bookyourshelf.controller.profile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileOrderController {

    @GetMapping("/profile/order")
    public String tab() {

        return "profile/_order";
    }
}
