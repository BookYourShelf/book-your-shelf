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

@Controller
public class ThankYouController {

    final AuthService authService;
    final ProfileInformationService profileInformationService;
    final CartService cartService;
    Order order;

    public ThankYouController(AuthService authService,
                              ProfileInformationService profileInformationService,
                              CartService cartService) {
        this.authService = authService;
        this.profileInformationService = profileInformationService;
        this.cartService = cartService;
    }

    @RequestMapping(value = "/thank-you", method = RequestMethod.GET)
    public String showThankYouPage(Model model) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        for (Order o : user.getOrders()) {
            if (o.getPaymentStatus() == Order.PaymentStatus.NULL) {
                order = o;
            }
        }

        order.setPaymentStatus(Order.PaymentStatus.COMPLETED);
        profileInformationService.save(user);
        model.addAttribute("order", order);
        return "thank-you";
    }
}
