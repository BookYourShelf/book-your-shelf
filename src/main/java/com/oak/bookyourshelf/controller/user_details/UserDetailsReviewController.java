package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserDetailsReviewController {

    final UserDetailsReviewService userDetailsReviewService;
    final UserDetailsInformationService userDetailsInformationService;
    final ProductDetailsInformationService productDetailsInformationService;

    public UserDetailsReviewController(UserDetailsReviewService userDetailsReviewService,
                                       UserDetailsInformationService userDetailsInformationService,
                                       ProductDetailsInformationService productDetailsInformationService) {
        this.userDetailsReviewService = userDetailsReviewService;
        this.userDetailsInformationService = userDetailsInformationService;
        this.productDetailsInformationService = productDetailsInformationService;
    }

    @RequestMapping(value = "/user-details/review", method = RequestMethod.GET)
    public String tab(@RequestParam("id") int id,
                      @RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort, Model model) {

        User user = userDetailsInformationService.get(id);
        String currentSort = sort.orElse("date");
        Globals.getPageNumbers(page, size, userDetailsReviewService.sortReviews(currentSort, user.getUserId()),
                model, "reviewPage");

        model.addAttribute("user", user);
        model.addAttribute("reviews", user.getReviews());
        model.addAttribute("productService", productDetailsInformationService);
        model.addAttribute("sort", currentSort);
        return "user_details/_review";
    }

    @RequestMapping(value = "/user-details/review/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> deleteReview(@PathVariable int id, @RequestParam int reviewId) {
        userDetailsReviewService.delete(reviewId);
        return ResponseEntity.ok("");
    }
}
