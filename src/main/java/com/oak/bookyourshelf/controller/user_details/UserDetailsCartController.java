package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsCartService;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class UserDetailsCartController {

    final UserDetailsInformationService userDetailsInformationService;
    final UserDetailsCartService userDetailsCartService;

    public UserDetailsCartController(UserDetailsInformationService userDetailsInformationService,
                                     UserDetailsCartService userDetailsCartService) {
        this.userDetailsInformationService = userDetailsInformationService;
        this.userDetailsCartService = userDetailsCartService;
    }

    @RequestMapping(value = "/user-details/cart", method = RequestMethod.GET)
    public String tab(@RequestParam("id") int id, @RequestParam("size") Optional<Integer> size,
                      @RequestParam("page") Optional<Integer> page, Model model) {
        User user = userDetailsInformationService.get(id);
        Globals.getPageNumbers(page, size, new ArrayList<>(user.getShoppingCart()), model, "cartPage");
        model.addAttribute("user", user);
        return "user_details/_cart";
    }
}
