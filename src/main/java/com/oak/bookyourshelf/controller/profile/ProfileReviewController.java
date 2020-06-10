package com.oak.bookyourshelf.controller.profile;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.ReviewService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import com.oak.bookyourshelf.service.profile.ProfileReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProfileReviewController {

    final ProfileReviewService profileReviewService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final ProductDetailsInformationService productDetailsInformationService;
    final ReviewService reviewService;

    public ProfileReviewController(ProfileReviewService profileReviewService,
                                   ProfileInformationService profileInformationService,
                                   AuthService authService,
                                   ProductDetailsInformationService productDetailsInformationService,
                                   ReviewService reviewService) {
        this.profileReviewService = profileReviewService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
        this.productDetailsInformationService = productDetailsInformationService;
        this.reviewService = reviewService;
    }

    @RequestMapping(value = "/profile/review", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort,
                      @RequestParam("filter") Optional<String> filter, Model model) {

        UserDetails userDetails = authService.getUserDetails();
        String currentSort = sort.orElse("date");
        String currentFilter = filter.orElse("all");

        if (userDetails != null) {
            User user = profileInformationService.getByEmail(userDetails.getUsername());
            Globals.getPageNumbers(page, size, filterReview(profileReviewService.sortReviews(currentSort, user.getUserId()), currentFilter),
                    model, "reviewPage");

            model.addAttribute("reviewListEmpty", user.getReviews().isEmpty());
            model.addAttribute("user", user);
            model.addAttribute("reviews", user.getReviews());
            model.addAttribute("productService", productDetailsInformationService);
            model.addAttribute("sort", currentSort);
            model.addAttribute("filter", currentFilter);
            return "profile/_review";
        }
        return "/";
    }

    @RequestMapping(value = "/profile/review", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> deleteReview(@RequestParam int reviewId) {
        Review toBeDeleted = reviewService.get(reviewId);
        Product product = productDetailsInformationService.get(toBeDeleted.getProductId());
        product.decreaseStarNum(toBeDeleted.getScoreOutOf5() - 1);
        profileReviewService.delete(reviewId);
        return ResponseEntity.ok("");
    }

    public List<Review> filterReview(List<Review> reviews, String rate) {
        switch (rate) {
            case "1":
                return reviews.stream().filter(p -> p.getScoreOutOf5() == 1).collect(Collectors.toList());
            case "2":
                return reviews.stream().filter(p -> p.getScoreOutOf5() == 2).collect(Collectors.toList());
            case "3":
                return reviews.stream().filter(p -> p.getScoreOutOf5() == 3).collect(Collectors.toList());
            case "4":
                return reviews.stream().filter(p -> p.getScoreOutOf5() == 4).collect(Collectors.toList());
            case "5":
                return reviews.stream().filter(p -> p.getScoreOutOf5() == 5).collect(Collectors.toList());
            default:
                return reviews;
        }
    }
}
