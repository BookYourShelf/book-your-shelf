package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.CartRepository;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CartService;
import com.oak.bookyourshelf.service.ProductService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CartController {

    final CartService cartService;
    final AuthService authService;
    final ProfileInformationService profileInformationService;
    final ProductService productService;
    public static Order order = new Order();


    public CartController(CartService cartService, @Qualifier("customUserDetailsService") AuthService authService,
                          ProfileInformationService profileInformationService, ProductService productService) {
        this.cartService = cartService;
        this.authService = authService;
        this.profileInformationService = profileInformationService;
        this.productService = productService;
    }



    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String showCart(Model model) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("cartProducts", user.getShoppingCart());
        return "/cart";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public ResponseEntity<String> showCartList(@RequestParam String button, @RequestParam Optional<Integer> productID,
                                               @RequestParam Optional<String> subTotal, @RequestParam Optional<String> totalAmount,
                                               @RequestParam Optional<String> shipping, @RequestParam Optional<String> coupon) {

        System.out.println(button);
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());



        if (button.equals("add_to_wish_list")) {
            Product product = productService.get(productID.get());
            user.getShoppingCart().remove(product);
            profileInformationService.save(user);
            user.getWishList().add(product);
            profileInformationService.save(user);

        } else if (button.equals("delete_product")) {
            Product product = productService.get(productID.get());
            user.getShoppingCart().remove(product);
            profileInformationService.save(user);

        } else if (button.equals("clear_list")) {
            user.getShoppingCart().clear();
            profileInformationService.save(user);

        } else {

            //Todo this gives null exception

            List<Integer> productIds = user.getShoppingCart().stream().map(Product::getProductId).collect(Collectors.toList());
            for(Integer p : productIds){
                System.out.println(p);
            }
            order.setProductId(productIds);
            order.setTotalAmount(totalAmount.get());
            order.setSubTotal(subTotal.get());
            order.setShippingMethod(shipping.get());

            System.out.println(shipping.get());
            System.out.println(totalAmount.get());
            System.out.println(subTotal.get());



        }
        return ResponseEntity.ok("");
    }

}
