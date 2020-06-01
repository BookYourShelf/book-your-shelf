package com.oak.bookyourshelf.controller;


import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.WishListService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class WishListController {
    final WishListService wishListService;
    final AuthService authService;
    final ProfileInformationService profileInformationService;

    public WishListController(WishListService wishListService, @Qualifier("customUserDetailsService") AuthService authService,
                              ProfileInformationService profileInformationService) {
        this.wishListService = wishListService;
        this.authService = authService;
        this.profileInformationService = profileInformationService;
    }

    @RequestMapping(value = "/wish-list", method = RequestMethod.GET)
    public String showWishList(Model model) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("wishListProducts", user.getWishList());
        return "/wish-list";
    }

    @RequestMapping(value = "/wish-list", method = RequestMethod.POST)
    public ResponseEntity<String> showWishList(@RequestParam String button, @RequestParam int productID) {
        System.out.println(productID);
        System.out.println(button);
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());


        if (button.equals("add_to_shopping_cart")) {
            Product product = wishListService.get(productID);
            if(!user.getShoppingCart().contains(product)) {
                if (product.getStock() > 0) {
                    user.getWishList().remove(product);
                    profileInformationService.save(user);
                    user.getShoppingCart().add(product);
                    profileInformationService.save(user);
                } else {
                    return ResponseEntity.badRequest().body("Product out of stock.");
                }
            } else {
                user.getWishList().remove(product);
                profileInformationService.save(user);
            }
        } else if (button.equals("delete_product")) {
            Product product = wishListService.get(productID);
            user.getWishList().remove(product);
            profileInformationService.save(user);

        } else {
            user.getWishList().clear();
            profileInformationService.save(user);

        }
        return ResponseEntity.ok("");
    }

}
