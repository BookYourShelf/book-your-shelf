package com.oak.bookyourshelf.controller.profile;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import com.oak.bookyourshelf.service.profile.ProfileReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ProfileReviewController {

    final ProfileReviewService profileReviewService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final ProductDetailsInformationService productDetailsInformationService;

    public ProfileReviewController(ProfileReviewService profileReviewService,
                                   ProfileInformationService profileInformationService,
                                   AuthService authService, ProductDetailsInformationService productDetailsInformationService) {
        this.profileReviewService = profileReviewService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
        this.productDetailsInformationService = productDetailsInformationService;
    }

    @RequestMapping(value = "/profile/review", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size, Model model) {

        UserDetails userDetails = authService.getUserDetails();
        if (userDetails != null) {
            User user = profileInformationService.getByEmail(userDetails.getUsername());
            Globals.getPageNumbers(page, size, user.getReviews(), model, "reviewPage");
            model.addAttribute("user", user);
            model.addAttribute("reviews", user.getReviews());
            model.addAttribute("productService", productDetailsInformationService);
            return "profile/_review";
        }
        return "/";
    }

    @RequestMapping(value = "/profile/review", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> deleteReview(@RequestParam int reviewId) {
        profileReviewService.delete(reviewId);
        return ResponseEntity.ok("");
    }
}
