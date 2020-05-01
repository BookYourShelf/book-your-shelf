package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.ProductService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    final ProductService productService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;

    public ProductController(ProductService productService, ProfileInformationService profileInformationService,
                             @Qualifier("customUserDetailsService") AuthService authService) {
        this.productService = productService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String showProductPage(Model model, @PathVariable int id) {

        Product product = productService.get(id);
        model.addAttribute("product", product);

        return "product";
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addProductToList(@RequestParam String type, @PathVariable int id) {

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
            }

        } else {
            // direct to login page
            return ResponseEntity.badRequest().body("");
        }

        return ResponseEntity.badRequest().body("An error occurred.");
    }
}
