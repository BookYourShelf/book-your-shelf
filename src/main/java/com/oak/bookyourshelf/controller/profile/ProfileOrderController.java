package com.oak.bookyourshelf.controller.profile;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import com.oak.bookyourshelf.service.profile.ProfileOrderService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProfileOrderController {

    final ProfileOrderService profileOrderService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;

    public ProfileOrderController(ProfileOrderService profileOrderService,
                                  ProfileInformationService profileInformationService,
                                  AuthService authService) {
        this.profileOrderService = profileOrderService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
    }

    @RequestMapping(value = "/profile/order", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort,
                      @RequestParam("payOptFilter") Optional<String> payOptFilter,
                      @RequestParam("payStatFilter") Optional<String> payStatFilter,
                      @RequestParam("orderStatFilter") Optional<String> orderStatFilter,
                      @RequestParam("delStatFilter") Optional<String> delStatFilter,
                      @RequestParam("couponFilter") Optional<String> couponFilter, Model model) {

        UserDetails userDetails = authService.getUserDetails();
        if (userDetails != null) {
            User user = profileInformationService.getByEmail(userDetails.getUsername());
            String currentSort = sort.orElse("time-desc");
            String curPayOptFilter = payOptFilter.orElse("all");
            String curPayStatFilter = payStatFilter.orElse("all");
            String curOrderStatFilter = orderStatFilter.orElse("all");
            String curDelStatFilter = delStatFilter.orElse("all");
            String curCouponFilter = couponFilter.orElse("all");

            Globals.getPageNumbers(page, size, filterOrdersByOrderStatus(filterOrdersByCouponUsed(filterOrdersByPaymentOption(
                    filterOrdersByPaymentStatus(
                            filterOrdersByDeliveryStatus(
                                    profileOrderService.sortOrders(currentSort, user.getUserId()), curDelStatFilter),
                            curPayStatFilter),
                    curPayOptFilter), curCouponFilter), curOrderStatFilter), model, "orderPage");

            model.addAttribute("orderListEmpty", profileOrderService.getAllOrdersOfUser(user.getUserId()).isEmpty());
            model.addAttribute("sort", currentSort);
            model.addAttribute("payOptFilter", curPayOptFilter);
            model.addAttribute("payStatFilter", curPayStatFilter);
            model.addAttribute("orderStatFilter", curOrderStatFilter);
            model.addAttribute("delStatFilter", curDelStatFilter);
            model.addAttribute("couponFilter", curCouponFilter);

            return "profile/_order";
        }
        return "redirect:/";
    }

    public List<Order> filterOrdersByPaymentOption(List<Order> orders, String paymentOption) {
        switch (paymentOption) {
            case "card":
                return orders.stream().filter(p -> p.getPaymentOption() == Order.PaymentOption.CREDIT_CARD).collect(Collectors.toList());
            case "paypal":
                return orders.stream().filter(p -> p.getPaymentOption() == Order.PaymentOption.PAYPAL).collect(Collectors.toList());
            case "ptt":
                return orders.stream().filter(p -> p.getPaymentOption() == Order.PaymentOption.TRANSFERRING_MONEY_PTT).collect(Collectors.toList());
            default:
                return orders;
        }
    }

    public List<Order> filterOrdersByPaymentStatus(List<Order> orders, String paymentStatus) {
        switch (paymentStatus) {
            case "pending":
                return orders.stream().filter(p -> p.getPaymentStatus() == Order.PaymentStatus.PENDING).collect(Collectors.toList());
            case "processed":
                return orders.stream().filter(p -> p.getPaymentStatus() == Order.PaymentStatus.PROCESSED).collect(Collectors.toList());
            case "completed":
                return orders.stream().filter(p -> p.getPaymentStatus() == Order.PaymentStatus.COMPLETED).collect(Collectors.toList());
            case "refunded":
                return orders.stream().filter(p -> p.getPaymentStatus() == Order.PaymentStatus.REFUNDED).collect(Collectors.toList());
            case "failed":
                return orders.stream().filter(p -> p.getPaymentStatus() == Order.PaymentStatus.FAILED).collect(Collectors.toList());
            case "expired":
                return orders.stream().filter(p -> p.getPaymentStatus() == Order.PaymentStatus.EXPIRED).collect(Collectors.toList());
            case "revoked":
                return orders.stream().filter(p -> p.getPaymentStatus() == Order.PaymentStatus.REVOKED).collect(Collectors.toList());
            case "pre":
                return orders.stream().filter(p -> p.getPaymentStatus() == Order.PaymentStatus.PREAPPROVED).collect(Collectors.toList());
            case "canceled":
                return orders.stream().filter(p -> p.getPaymentStatus() == Order.PaymentStatus.CANCELLED).collect(Collectors.toList());
            default:
                return orders;
        }
    }

    public List<Order> filterOrdersByOrderStatus(List<Order> orders, String couponUse) {
        switch (couponUse) {
            case "pending":
                return orders.stream().filter(o -> o.getOrderStatus() == Order.OrderStatus.PENDING).collect(Collectors.toList());
            case "confirmed":
                return orders.stream().filter(o -> o.getOrderStatus() == Order.OrderStatus.CONFIRMED).collect(Collectors.toList());
            case "canceled":
                return orders.stream().filter(o -> o.getOrderStatus() == Order.OrderStatus.CANCELED).collect(Collectors.toList());
            default:
                return orders;
        }
    }

    public List<Order> filterOrdersByDeliveryStatus(List<Order> orders, String paymentStatus) {
        switch (paymentStatus) {
            case "info-received":
                return orders.stream().filter(p -> p.getDeliveryStatus() == Order.DeliveryStatus.INFO_RECEIVED).collect(Collectors.toList());
            case "transit":
                return orders.stream().filter(p -> p.getDeliveryStatus() == Order.DeliveryStatus.IN_TRANSIT).collect(Collectors.toList());
            case "out":
                return orders.stream().filter(p -> p.getDeliveryStatus() == Order.DeliveryStatus.OUT_FOR_DELIVERY).collect(Collectors.toList());
            case "failed":
                return orders.stream().filter(p -> p.getDeliveryStatus() == Order.DeliveryStatus.FAILED_ATTEMPT).collect(Collectors.toList());
            case "delivered":
                return orders.stream().filter(p -> p.getDeliveryStatus() == Order.DeliveryStatus.DELIVERED).collect(Collectors.toList());
            case "exception":
                return orders.stream().filter(p -> p.getDeliveryStatus() == Order.DeliveryStatus.EXCEPTION).collect(Collectors.toList());
            case "expired":
                return orders.stream().filter(p -> p.getDeliveryStatus() == Order.DeliveryStatus.EXPIRED).collect(Collectors.toList());
            case "pending":
                return orders.stream().filter(p -> p.getDeliveryStatus() == Order.DeliveryStatus.PENDING).collect(Collectors.toList());
            case "canceled":
                return orders.stream().filter(p -> p.getDeliveryStatus() == Order.DeliveryStatus.CANCELED).collect(Collectors.toList());
            default:
                return orders;
        }
    }

    public List<Order> filterOrdersByCouponUsed(List<Order> orders, String couponUse) {
        switch (couponUse) {
            case "yes":
                return orders.stream().filter(o -> o.getCouponCode() != null).collect(Collectors.toList());
            case "no":
                return orders.stream().filter(o -> o.getCouponCode() == null).collect(Collectors.toList());
            default:
                return orders;
        }
    }
}
