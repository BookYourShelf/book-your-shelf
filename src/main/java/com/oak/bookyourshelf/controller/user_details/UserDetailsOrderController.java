package com.oak.bookyourshelf.controller.user_details;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserDetailsOrderController {

    final UserDetailsOrderService userDetailsOrderService;
    final UserDetailsInformationService userDetailsInformationService;
    final ProductDetailsInformationService productDetailsInformationService;
    ObjectMapper mapper = new ObjectMapper();

    public UserDetailsOrderController(UserDetailsOrderService userDetailsOrderService,
                                      UserDetailsInformationService userDetailsInformationService,
                                      ProductDetailsInformationService productDetailsInformationService) {
        this.userDetailsOrderService = userDetailsOrderService;
        this.userDetailsInformationService = userDetailsInformationService;
        this.productDetailsInformationService = productDetailsInformationService;
    }

    @RequestMapping(value = "/user-details/order", method = RequestMethod.GET)
    public String tab(@RequestParam("id") int id,
                      @RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort,
                      @RequestParam("payOptFilter") Optional<String> payOptFilter,
                      @RequestParam("payStatFilter") Optional<String> payStatFilter,
                      @RequestParam("delStatFilter") Optional<String> delStatFilter,
                      @RequestParam("couponFilter") Optional<String> couponFilter, Model model) {

        User user = userDetailsInformationService.get(id);
        String currentSort = sort.orElse("time-desc");
        String curPayOptFilter = payOptFilter.orElse("all");
        String curPayStatFilter = payStatFilter.orElse("all");
        String curDelStatFilter = delStatFilter.orElse("all");
        String curCouponFilter = couponFilter.orElse("all");
        Globals.getPageNumbers(page, size, filterOrdersByCouponUsed(filterOrdersByPaymentOption(
                filterOrdersByPaymentStatus(
                        filterOrdersByDeliveryStatus(
                                userDetailsOrderService.sortOrders(currentSort, user.getUserId()), curDelStatFilter),
                        curPayStatFilter),
                curPayOptFilter), curCouponFilter), model, "orderPage");

        model.addAttribute("orderListEmpty", userDetailsOrderService.getAllOrdersOfUser(user.getUserId()).isEmpty());
        model.addAttribute("user", user);
        model.addAttribute("productService", productDetailsInformationService);
        model.addAttribute("sort", currentSort);
        model.addAttribute("payOptFilter", curPayOptFilter);
        model.addAttribute("payStatFilter", curPayStatFilter);
        model.addAttribute("delStatFilter", curDelStatFilter);
        model.addAttribute("couponFilter", curCouponFilter);

        return "user_details/_order";
    }

    @RequestMapping(value = "/user-details/order/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> confirmOrCancelOrder(@PathVariable int id, @RequestParam("reply") String reply,
                                                       @RequestParam("orderId") Integer orderId, String confirmedProducts) throws JsonProcessingException {
        Order order = userDetailsOrderService.get(orderId);

        switch (reply) {
            case "confirm":
                List<Integer> productsIds = mapper.readValue(confirmedProducts, new TypeReference<List<Integer>>() {
                });
                Product outOfStockProduct = checkStock(productsIds);

                if (outOfStockProduct != null) {
                    return ResponseEntity.badRequest().body(outOfStockProduct.getProductName() + " is out of stock.");
                }

                updateProducts(productsIds);
                updateOrders(order, 1);
                return ResponseEntity.ok("");

            case "cancel":
                updateOrders(order, 0);
                return ResponseEntity.ok("");

            default:
                return ResponseEntity.badRequest().body("An error occurred.");
        }
    }

    public Product checkStock(List<Integer> productsIds) {
        for (Integer id : productsIds) {
            Product product = productDetailsInformationService.get(id);
            if (product.getStock() <= 0) {   // TODO quantity
                return product;
            }
        }
        return null;
    }

    public void updateProducts(List<Integer> productsIds) {
        for (Integer id : productsIds) {
            Product product = productDetailsInformationService.get(id);
            product.increaseSalesNum();
            product.setStock(product.getStock() - 1);   // TODO quantity
            productDetailsInformationService.save(product);
        }
    }

    public void updateOrders(Order order, int success) {
        if (success == 1) {
            order.setOrderStatus(Order.OrderStatus.CONFIRMED);
            order.setPaymentStatus(Order.PaymentStatus.COMPLETED);
            order.setDeliveryStatus(Order.DeliveryStatus.INFO_RECEIVED);
        } else {
            order.setOrderStatus(Order.OrderStatus.CANCELED);
            order.setPaymentStatus(Order.PaymentStatus.CANCELLED);
            order.setDeliveryStatus(Order.DeliveryStatus.CANCELED);
        }

        userDetailsOrderService.save(order);
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

    public List<Order> filterOrdersByCouponUsed(List<com.oak.bookyourshelf.model.Order> orders, String couponUse) {
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
