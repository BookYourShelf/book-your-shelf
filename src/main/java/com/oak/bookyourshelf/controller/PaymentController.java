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

@Controller
public class PaymentController {

    final PaymentService paymentService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final CartService cartService;
    final ProductDetailsInformationService productDetailsInformationService;
    Order order;

    public PaymentController(PaymentService paymentService,
                             @Qualifier("customUserDetailsService") AuthService authService,
                             ProfileInformationService profileInformationService,
                             CartService cartService,
                             ProductDetailsInformationService productDetailsInformationService) {
        this.paymentService = paymentService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
        this.cartService = cartService;
        this.productDetailsInformationService = productDetailsInformationService;
    }

    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    public String showNewPaymentPage(Model model) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        for (Order o : user.getOrders()) {
            if (o.getPaymentStatus() == Order.PaymentStatus.NULL) {
                order = o;
            }
        }

        // if order already payed
        if (order == null) {
            return "index";
        }

        CreditCard creditCard = new CreditCard();
        model.addAttribute("creditCard", creditCard);
        return "payment";
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ResponseEntity<String> payment(@RequestParam String button, CreditCard creditCard) {
        // TODO: Credit Card
        Payment payment = new Payment();
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());

        if (button.equals("ConfirmCreditCard")) {
            payment.setPaymentMethod(Payment.PaymentMethod.PAYMENT_METHOD_CREDIT_CARD);
            order.setPaymentOption(Order.PaymentOption.CREDIT_CARD);

        } else if (button.equals("PayPal")) {
            payment.setPaymentMethod(Payment.PaymentMethod.PAYMENT_METHOD_PAYPAL);
            order.setPaymentOption(Order.PaymentOption.PAYPAL);

        } else {
            payment.setPaymentMethod(Payment.PaymentMethod.PAYMENT_METHOD_BANK_TRANSFER);
            order.setPaymentOption(Order.PaymentOption.TRANSFERRING_MONEY_PTT);
        }

        payment.setIssueDate(new Timestamp(System.currentTimeMillis()));
        payment.setPayerId(user.getUserId());
        payment.setPaymentResult(Payment.PaymentResult.PAYMENT_RESULT_SUCCESS);
        paymentService.save(payment);

        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setUserName(user.getName());
        order.setUserId(user.getUserId());
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setDeliveryStatus(Order.DeliveryStatus.PENDING);

        productModification(user);
        user.getShoppingCart().clear();

        List<Order> orders = user.getOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId() == order.getOrderId()) {
                orders.set(i, order);
            }
        }

        profileInformationService.save(user);
        order = null;
        return ResponseEntity.ok("");
    }


    public void productModification(User user) {
        for (CartItem cartItem : user.getShoppingCart()) {
            if (!cartItem.getProduct().getBuyerUserIds().contains(user.getUserId())) {
                cartItem.getProduct().getBuyerUserIds().add(user.getUserId());
                productDetailsInformationService.save(cartItem.getProduct());
            }
        }
    }
}
