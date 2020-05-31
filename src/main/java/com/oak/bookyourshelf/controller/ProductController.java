package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.ProductService;
import com.oak.bookyourshelf.service.ReviewService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelProductService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsReviewService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ProductController {

    final ProductService productService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final AdminPanelProductService adminPanelProductService;
    final ReviewService reviewService;
    final UserDetailsReviewService userDetailsReviewService;

    public ProductController(ProductService productService, ProfileInformationService profileInformationService,
                             @Qualifier("customUserDetailsService") AuthService authService,
                             AdminPanelProductService adminPanelProductService,
                             ReviewService reviewService, UserDetailsReviewService userDetailsReviewService) {
        this.productService = productService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
        this.adminPanelProductService = adminPanelProductService;
        this.reviewService = reviewService;
        this.userDetailsReviewService = userDetailsReviewService;
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String showProductPage(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size, Model model, @PathVariable int id) {

        Product product = productService.get(id);
        Globals.getPageNumbers(page, size, product.getReviews(), model, "reviewPage");
        model.addAttribute("product", product);
        return "product";
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addProductToList(@RequestParam String type, @PathVariable int id, Review review, int reviewId) {

        UserDetails userDetails = authService.getUserDetails();
        Product product = productService.get(id);

        if (userDetails != null) {
            User user = profileInformationService.getByEmail(userDetails.getUsername());

            switch (type) {
                case "wish-list":
                    if (!user.getWishList().contains(product)) {
                        user.addToWishList(product);
                        profileInformationService.save(user);
                    }
                    // already in wish list
                    return ResponseEntity.ok("");

                case "cart":
                    if (!user.getShoppingCart().contains(product)) {
                        if (product.getStock() > 0) {
                            user.addToCart(product);
                            profileInformationService.save(user);
                            return ResponseEntity.ok("");
                        } else {
                            return ResponseEntity.badRequest().body("There is no stock.");
                        }
                    }
                    // already in cart
                    return ResponseEntity.ok("");

                case "reminder":
                    return ResponseEntity.ok("");

                case "add_review":
                    java.util.Date utilDate = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                    review.setUploadDate(sqlDate);

                    review.setUserId(user.getUserId());
                    review.setUserName(user.getName());
                    review.setUserSurname(user.getSurname());

                    user.addReview(review);
                    product.addReview(review);
                    product.increaseStarNum(review.getScoreOutOf5() - 1);
                    reviewService.save(review);

                    return ResponseEntity.ok("");

                case "delete_review":
                    userDetailsReviewService.delete(reviewId);
                    return ResponseEntity.ok("");
            }

        } else {
            // direct to login page
            return ResponseEntity.badRequest().body("");
        }

        return ResponseEntity.badRequest().body("An error occurred.");
    }
}
