package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserDetailsReviewController {

    final UserDetailsReviewService userDetailsReviewService;
    final UserDetailsInformationService userDetailsInformationService;

    public UserDetailsReviewController(UserDetailsReviewService userDetailsReviewService, UserDetailsInformationService userDetailsInformationService) {
        this.userDetailsReviewService = userDetailsReviewService;
        this.userDetailsInformationService = userDetailsInformationService;
    }

    @RequestMapping(value = "/user-details/review", method = RequestMethod.GET)
    public String tab(@RequestParam("id") int id, @RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size, Model model) {
        User user = userDetailsInformationService.get(id);

        Globals.getPageNumbers(page, size, user.getReviews(), model, "reviewPage");

        model.addAttribute("user", user);
        model.addAttribute("reviews", user.getReviews());
        return "user_details/_review";
    }
}
