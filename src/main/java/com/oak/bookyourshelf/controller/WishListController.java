package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.CartItem;
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

import java.util.Set;

@Controller
public class WishListController {
    final WishListService wishListService;
    final AuthService authService;
    final ProfileInformationService profileInformationService;

    public WishListController(WishListService wishListService,
                              @Qualifier("customUserDetailsService") AuthService authService,
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
        return "wish-list";
    }

    @RequestMapping(value = "/wish-list", method = RequestMethod.POST)
    public ResponseEntity<String> showWishList(@RequestParam String button, @RequestParam int productID) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());

        switch (button) {
            case "add_to_shopping_cart":
                Product product = wishListService.get(productID);
                if (!containsCartItem(user.getShoppingCart(), productID)) {
                    if (product.getStock() > 0) {
                        deleteProductFromWishList(user.getWishList(), productID);
                        CartItem cartItem = new CartItem();
                        cartItem.setProduct(product);
                        cartItem.setQuantity(1);
                        user.addToCart(cartItem);
                        profileInformationService.save(user);
                        return ResponseEntity.ok("");

                    } else {
                        return ResponseEntity.badRequest().body("Product out of stock.");
                    }

                } else {
                    deleteProductFromWishList(user.getWishList(), productID);
                    // already in cart, quantity updated, save user
                    profileInformationService.save(user);
                    return ResponseEntity.ok("");
                }

            case "delete_product":
                deleteProductFromWishList(user.getWishList(), productID);
                profileInformationService.save(user);
                return ResponseEntity.ok("");

            case "clear_list":
                user.getWishList().clear();
                profileInformationService.save(user);
                return ResponseEntity.ok("");

            default:
                return ResponseEntity.badRequest().body("An error occurred.");
        }
    }

    public boolean containsCartItem(Set<CartItem> set, int productId) {
        for (CartItem c : set) {
            if (c.getProduct().getProductId() == productId) {
                c.setQuantity(c.getQuantity() + 1);
                return true;
            }
        }
        return false;
    }

    public void deleteProductFromWishList(Set<Product> products, int productId) {
        products.removeIf(p -> p.getProductId() == productId);
    }
}
