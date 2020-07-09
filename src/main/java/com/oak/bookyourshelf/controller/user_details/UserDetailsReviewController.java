package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.ReviewService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserDetailsReviewController {

    final UserDetailsReviewService userDetailsReviewService;
    final UserDetailsInformationService userDetailsInformationService;
    final ProductDetailsInformationService productDetailsInformationService;
    final ReviewService reviewService;

    public UserDetailsReviewController(UserDetailsReviewService userDetailsReviewService,
                                       UserDetailsInformationService userDetailsInformationService,
                                       ProductDetailsInformationService productDetailsInformationService,
                                       ReviewService reviewService) {
        this.userDetailsReviewService = userDetailsReviewService;
        this.userDetailsInformationService = userDetailsInformationService;
        this.productDetailsInformationService = productDetailsInformationService;
        this.reviewService = reviewService;
    }

    @RequestMapping(value = "/user-details/review", method = RequestMethod.GET)
    public String tab(@RequestParam("id") int id,
                      @RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort,
                      @RequestParam("ratingFilter") Optional<String> ratingFilter,
                      @RequestParam("titleFilter") Optional<String> titleFilter, Model model) {

        User user = userDetailsInformationService.get(id);
        String currentSort = sort.orElse("date");
        String curRatingFilter = ratingFilter.orElse("all");
        String curTitleFilter = titleFilter.orElse("all");
        Globals.getPageNumbers(page, size, titleFilterReview(
                ratingFilterReview(
                        userDetailsReviewService.sortReviewsOfUser(currentSort, user),
                        curRatingFilter),
                curTitleFilter), model, "reviewPage");

        model.addAttribute("reviewListEmpty", user.getReviews().isEmpty());
        model.addAttribute("user", user);
        model.addAttribute("reviews", user.getReviews());
        model.addAttribute("productService", productDetailsInformationService);
        model.addAttribute("sort", currentSort);
        model.addAttribute("ratingFilter", curRatingFilter);
        model.addAttribute("titleFilter", curTitleFilter);
        return "user_details/_review";
    }

    @RequestMapping(value = "/user-details/review/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> deleteReview(@PathVariable int id, @RequestParam int reviewId) {
        Review toBeDeleted = reviewService.get(reviewId);
        Product product = productDetailsInformationService.get(toBeDeleted.getProduct().getProductId());
        product.decreaseStarNum(toBeDeleted.getScoreOutOf5() - 1);
        userDetailsReviewService.delete(reviewId);
        return ResponseEntity.ok("");
    }

    public List<Review> ratingFilterReview(List<Review> reviews, String rate) {
        switch (rate) {
            case "1":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 1).collect(Collectors.toList());
            case "2":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 2).collect(Collectors.toList());
            case "3":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 3).collect(Collectors.toList());
            case "4":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 4).collect(Collectors.toList());
            case "5":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 5).collect(Collectors.toList());
            default:
                return reviews;
        }
    }

    public List<Review> titleFilterReview(List<Review> reviews, String title) {
        switch (title) {
            case "titled":
                return reviews.stream().filter(r -> !r.getReviewTitle().equals("")).collect(Collectors.toList());
            case "untitled":
                return reviews.stream().filter(r -> r.getReviewTitle().equals("")).collect(Collectors.toList());
            default:
                return reviews;
        }
    }
}
