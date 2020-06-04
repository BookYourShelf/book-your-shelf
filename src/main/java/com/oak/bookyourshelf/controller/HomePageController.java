package com.oak.bookyourshelf.controller;


import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CompareProductsService;
import com.oak.bookyourshelf.service.HomePageService;
import com.oak.bookyourshelf.service.ProductService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomePageController {

    final AuthService authService;
    final ProfileInformationService profileInformationService;
    final HomePageService homePageService;
    final CompareProductsService compareProductsService;
    final ProductService productService;
    final ProductDetailsInformationService productDetailsInformationService;


    public HomePageController(AuthService authService, ProfileInformationService profileInformationService, HomePageService homePageService, CompareProductsService compareProductsService, ProductService productService, ProductDetailsInformationService productDetailsInformationService) {
        this.authService = authService;
        this.profileInformationService = profileInformationService;
        this.homePageService = homePageService;
        this.compareProductsService = compareProductsService;
        this.productService = productService;
        this.productDetailsInformationService = productDetailsInformationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showUser(Model model ) {

        UserDetails userDetails = authService.getUserDetails();
        List<Product> ourPicksForYou = new ArrayList<>();
        if(userDetails != null) {
            model.addAttribute("bestSeller",homePageService.createBestSellerList(0,8));
            model.addAttribute("newProducts",homePageService.createNewProductsList(0,8));

            model.addAttribute("moreBestSeller",homePageService.createBestSellerList(8,15));
            model.addAttribute("moreNewProducts",homePageService.createNewProductsList(8,15));
            model.addAttribute("user", profileInformationService.getByEmail(userDetails.getUsername()));
            model.addAttribute("ourpicksforYou",ourPicksForYou);
        }
        else
        {
            model.addAttribute("bestSeller",homePageService.createBestSellerList(0,8));
            model.addAttribute("newProducts",homePageService.createNewProductsList(0,8));

            model.addAttribute("moreBestSeller",homePageService.createBestSellerList(8,15));
            model.addAttribute("moreNewProducts",homePageService.createNewProductsList(8,15));

            model.addAttribute("ourpicksforYou",ourPicksForYou);
        }
        model.addAttribute("compareProductsService",compareProductsService);
        return "/index";
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
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
                            product.getProductQuantity().put(product.getProductId(), 1);
                            user.addToCart(product);
                            productDetailsInformationService.save(product);
                            profileInformationService.save(user);
                            return ResponseEntity.ok("");
                        } else {
                            return ResponseEntity.badRequest().body("There is no stock.");
                        }
                    }

                    product.getProductQuantity().put(product.getProductId(), product.getProductQuantity().get(product.getProductId()) + 1);
                    productDetailsInformationService.save(product);
                    // already in cart
                    return ResponseEntity.ok("");


            }
        }else {
            // direct to login page
            return ResponseEntity.badRequest().body("");
        }

        return ResponseEntity.badRequest().body("An error occurred.");
    }






}
