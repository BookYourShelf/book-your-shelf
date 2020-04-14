package com.oak.bookyourshelf.controller.user_details;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDetailsMessageController {

    @GetMapping("/user-details/message")
    public String tab() {

        return "user_details/_message";
    }
}
