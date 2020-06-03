package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CartService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ThankYouController {

    final AuthService authService;
    final ProfileInformationService profileInformationService;
    final CartService cartService;



    public ThankYouController(AuthService authService, ProfileInformationService profileInformationService,
                              CartService cartService){
        this.authService = authService;
        this.profileInformationService = profileInformationService;
        this.cartService =cartService;
    }

    @RequestMapping(value= "/thank-you", method = RequestMethod.GET)
    public String showThankYouPage(Model model) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        Order order = new Order();
        for(Order o: user.getOrders()){
            if(o.getPaymentStatus() == null){
                order =o;
            }
        }
        model.addAttribute("order",order);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        cartService.save(order);
        user.getOrders().add(order);
        user.getShoppingCart().clear();
        profileInformationService.save(user);
        return "thank-you";
    }

}
