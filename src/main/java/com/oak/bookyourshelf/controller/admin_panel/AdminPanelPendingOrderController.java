package com.oak.bookyourshelf.controller.admin_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelPendingOrderService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AdminPanelPendingOrderController {

    final AdminPanelPendingOrderService adminPanelPendingOrderService;
    final ProfileInformationService profileInformationService;
    final ProductDetailsInformationService productDetailsInformationService;
    ObjectMapper mapper = new ObjectMapper();

    public AdminPanelPendingOrderController(AdminPanelPendingOrderService adminPanelPendingOrderService,
                                            ProfileInformationService profileInformationService,
                                            ProductDetailsInformationService productDetailsInformationService) {
        this.adminPanelPendingOrderService = adminPanelPendingOrderService;
        this.profileInformationService = profileInformationService;
        this.productDetailsInformationService = productDetailsInformationService;
    }

    @RequestMapping(value = "/admin-panel/pending-order", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort,
                      @RequestParam("optionFilter") Optional<String> paymentOptionFilter,
                      @RequestParam("statusFilter") Optional<String> paymentStatusFilter,
                      @RequestParam("couponFilter") Optional<String> couponFilter, Model model) {

        String currentSort = sort.orElse("time-desc");
        String optionFilter = paymentOptionFilter.orElse("all");
        String statusFilter = paymentStatusFilter.orElse("all");
        String curCouponFilter = couponFilter.orElse("all");
        Globals.getPageNumbers(page, size, filterOrdersByCouponUsed(
                filterOrdersByPaymentOption(filterOrdersByPaymentStatus(
                        adminPanelPendingOrderService.sortOrders(currentSort, Order.OrderStatus.PENDING),
                        statusFilter), optionFilter), curCouponFilter), model, "orderPage");

        model.addAttribute("orderListEmpty", ((List) adminPanelPendingOrderService.listAll(Order.OrderStatus.PENDING)).isEmpty());
        model.addAttribute("profileInformationService", profileInformationService);
        model.addAttribute("productService", productDetailsInformationService);
        model.addAttribute("sort", currentSort);
        model.addAttribute("optionFilter", optionFilter);
        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("couponFilter", curCouponFilter);

        return "admin_panel/_pending-order";
    }

    @RequestMapping(value = "/admin-panel/pending-order", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> confirmOrCancelOrder(@RequestParam("reply") String reply,
                                                       @RequestParam("orderId") Integer orderId, String confirmedProducts) throws JsonProcessingException {
        Order order = adminPanelPendingOrderService.get(orderId);

        switch (reply) {
            case "confirm":
                List<Integer> productsIds = mapper.readValue(confirmedProducts, new TypeReference<List<Integer>>() {
                });
                Product outOfStockProduct = checkStock(order, productsIds);

                if (outOfStockProduct != null) {
                    return ResponseEntity.badRequest().body(outOfStockProduct.getProductName() + " is out of stock.");
                }

                updateProducts(order, productsIds);
                updateOrders(order, 1);
                return ResponseEntity.ok("");

            case "cancel":
                updateOrders(order, 0);
                return ResponseEntity.ok("");

            default:
                return ResponseEntity.badRequest().body("An error occurred.");
        }
    }

    public Product checkStock(Order order, List<Integer> productsIds) {
        for (Integer id : productsIds) {
            Product product = productDetailsInformationService.get(id);
            if (product.getStock() < getQuantity(order.getProducts(), id)) {
                return product;
            }
        }
        return null;
    }

    public void updateProducts(Order order, List<Integer> productsIds) {
        for (Integer id : productsIds) {
            Product product = productDetailsInformationService.get(id);
            product.increaseSalesNum();
            product.setStock(product.getStock() - getQuantity(order.getProducts(), id));
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

        adminPanelPendingOrderService.save(order);
    }

    public int getQuantity(List<CartItem> cartItems, int productId) {
        for (CartItem c : cartItems) {
            if (c.getProduct().getProductId() == productId) {
                return c.getQuantity();
            }
        }
        return 0;
    }

    public List<Order> filterOrdersByPaymentOption(List<Order> orders, String paymentOption) {
        switch (paymentOption) {
            case "card":
                return orders.stream().filter(o -> o.getPaymentOption() == Order.PaymentOption.CREDIT_CARD).collect(Collectors.toList());
            case "paypal":
                return orders.stream().filter(o -> o.getPaymentOption() == Order.PaymentOption.PAYPAL).collect(Collectors.toList());
            case "ptt":
                return orders.stream().filter(o -> o.getPaymentOption() == Order.PaymentOption.TRANSFERRING_MONEY_PTT).collect(Collectors.toList());
            default:
                return orders;
        }
    }

    public List<Order> filterOrdersByPaymentStatus(List<Order> orders, String paymentStatus) {
        switch (paymentStatus) {
            case "pending":
                return orders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.PENDING).collect(Collectors.toList());
            case "processed":
                return orders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.PROCESSED).collect(Collectors.toList());
            case "completed":
                return orders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.COMPLETED).collect(Collectors.toList());
            case "refunded":
                return orders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.REFUNDED).collect(Collectors.toList());
            case "failed":
                return orders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.FAILED).collect(Collectors.toList());
            case "expired":
                return orders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.EXPIRED).collect(Collectors.toList());
            case "revoked":
                return orders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.REVOKED).collect(Collectors.toList());
            case "pre":
                return orders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.PREAPPROVED).collect(Collectors.toList());
            case "canceled":
                return orders.stream().filter(o -> o.getPaymentStatus() == Order.PaymentStatus.CANCELLED).collect(Collectors.toList());
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
