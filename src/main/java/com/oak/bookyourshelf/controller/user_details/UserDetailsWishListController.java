package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsWishListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class UserDetailsWishListController {

    final UserDetailsInformationService userDetailsInformationService;
    final UserDetailsWishListService userDetailsWishListService;

    public UserDetailsWishListController(UserDetailsInformationService userDetailsInformationService,
                                         UserDetailsWishListService userDetailsWishListService) {
        this.userDetailsInformationService = userDetailsInformationService;
        this.userDetailsWishListService = userDetailsWishListService;
    }

    @RequestMapping(value = "/user-details/wish-list", method = RequestMethod.GET)
    public String tab(@RequestParam("id") int id, @RequestParam("size") Optional<Integer> size,
                      @RequestParam("page") Optional<Integer> page, Model model) {
        User user = userDetailsInformationService.get(id);
        Globals.getPageNumbers(page, size, new ArrayList<>(user.getWishList()), model, "wishListPage");
        model.addAttribute("user", user);
        return "user_details/_wish-list";
    }
}
