package com.oak.bookyourshelf.controller.user_details;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDetailsWishListController {

    @GetMapping("/user-details/wish-list")
    public String tab() {

        return "user_details/_wish-list";
    }
}
