package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CartService;
import com.oak.bookyourshelf.service.PaymentService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class PaymentController {

    final PaymentService paymentService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final CartService cartService;
    final ProductDetailsInformationService productDetailsInformationService;
    Order order = CartController.order;

    public PaymentController(PaymentService paymentService, @Qualifier("customUserDetailsService") AuthService authService,
                             ProfileInformationService profileInformationService, CartService cartService,
                             ProductDetailsInformationService productDetailsInformationService) {
        this.paymentService = paymentService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
        this.cartService = cartService;
        this.productDetailsInformationService = productDetailsInformationService;

    }

    @RequestMapping("/payment")
    public String showNewPaymentPage(Model model) {
        CreditCard creditCard = new CreditCard();
        model.addAttribute("creditCard", creditCard);
        return "payment";
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ResponseEntity<String> payment(@RequestParam String button, CreditCard creditCard) {
        Payment payment = new Payment();
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());


        if (button.equals("ConfirmCreditCard")) {
            System.out.println("In credit card");
            payment.setPaymentMethod(Payment.PaymentMethod.PAYMENT_METHOD_CREDIT_CARD);
            order.setPaymentOption(Order.PaymentOption.CREDIT_CARD);
            payment(payment, user);


        } else if (button.equals("PayPal")) {
            payment.setPaymentMethod(Payment.PaymentMethod.PAYMENT_METHOD_PAYPAL);
            order.setPaymentOption(Order.PaymentOption.PAYPAL);
            payment(payment, user);


        } else {
            payment.setPaymentMethod(Payment.PaymentMethod.PAYMENT_METHOD_BANK_TRANSFER);
            order.setPaymentOption(Order.PaymentOption.TRANSFERRING_MONEY_PTT);
            payment(payment, user);
        }

        return ResponseEntity.ok("");
    }


    public void payment(Payment payment, User user) {
        payment.setIssueDate(new Timestamp(System.currentTimeMillis()));
        payment.setPayerId(user.getUserId());
        payment.setPaymentResult(Payment.PaymentResult.PAYMENT_RESULT_SUCCESS);
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setUserName(user.getName());
        order.setUserId(user.getUserId());
        order.setPaymentStatus(Order.PaymentStatus.COMPLETED);
        order.setOrderStatus(Order.OrderStatus.PENDING);
        user.getOrders().add(order);
        productModification(user);
        paymentService.save(payment);
        cartService.save(order);
        profileInformationService.save(user);
        order = null;
    }

    public void productModification(User user) {
        List<Integer> productIds = user.getShoppingCart().stream().map(Product::getProductId).collect(Collectors.toList());
        for (Integer productID : productIds) {
            Product product = productDetailsInformationService.get(productID);
            if(!product.getBuyerUserIds().contains(user.getUserId())){
                product.getBuyerUserIds().add(user.getUserId());
            }
           // product.increaseSalesNum();
           // product.setStock(product.getStock() - 1);
            productDetailsInformationService.save(product);
        }

    }
}
