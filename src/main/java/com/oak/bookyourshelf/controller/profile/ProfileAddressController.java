package com.oak.bookyourshelf.controller.profile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileAddressController {

    @GetMapping("/profile/address")
    public String tab() {

        return "profile/_address";
    }
}
