package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CartService;
import com.oak.bookyourshelf.service.PaymentService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCouponService;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
public class PaymentController {

    final PaymentService paymentService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final CartService cartService;
    final ProductDetailsInformationService productDetailsInformationService;
    final AdminPanelCouponService adminPanelCouponService;
    Order order;

    public PaymentController(PaymentService paymentService,
                             @Qualifier("customUserDetailsService") AuthService authService,
                             ProfileInformationService profileInformationService,
                             CartService cartService,
                             ProductDetailsInformationService productDetailsInformationService,
                             AdminPanelCouponService adminPanelCouponService) {
        this.paymentService = paymentService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
        this.cartService = cartService;
        this.productDetailsInformationService = productDetailsInformationService;
        this.adminPanelCouponService = adminPanelCouponService;
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
            return "redirect:/";
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
        payment.setAmount(order.getTotalAmount());
        payment.setOrderId(order.getOrderId());
        payment.setPaymentResult(Payment.PaymentResult.PAYMENT_RESULT_SUCCESS);
        paymentService.save(payment);

        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setUserName(user.getName());
        order.setUserId(user.getUserId());
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setDeliveryStatus(Order.DeliveryStatus.PENDING);
        coupon(order, user);

        updateStocks(order.getProducts());
        updateBuyerUserIds(user);
        user.getShoppingCart().clear();

        Iterator<Order> iterator = user.getOrders().iterator();
        while (iterator.hasNext()) {
            Order o = iterator.next();
            if (o.getOrderId() == order.getOrderId()) {
                iterator.remove();
                break;
            }
        }
        user.getOrders().add(order);

        profileInformationService.save(user);
        order = null;
        return ResponseEntity.ok("");
    }

    public void updateBuyerUserIds(User user) {
        for (CartItem c : user.getShoppingCart()) {
            if (!c.getProduct().getBuyerUserIds().contains(user.getUserId())) {
                c.getProduct().getBuyerUserIds().add(user.getUserId());
                productDetailsInformationService.save(c.getProduct());
            }
        }
    }

    public void updateStocks(Set<CartItem> cartItems) {
        for (CartItem c : cartItems) {
            c.getProduct().increaseSalesNum();
            c.getProduct().setStock(c.getProduct().getStock() - c.getQuantity());
            productDetailsInformationService.save(c.getProduct());
        }
    }

    public int getQuantity(Set<CartItem> cartItems, int productId) {
        for (CartItem c : cartItems) {
            if (c.getProduct().getProductId() == productId) {
                return c.getQuantity();
            }
        }
        return 0;
    }

    public void coupon(Order order, User user) {
        if (order.getCouponCode() != null) {
            Coupon coupon = adminPanelCouponService.findByCouponCode(order.getCouponCode());
            coupon.getUserId().add(user.getUserId());
            adminPanelCouponService.save(coupon);
        }
    }
}
