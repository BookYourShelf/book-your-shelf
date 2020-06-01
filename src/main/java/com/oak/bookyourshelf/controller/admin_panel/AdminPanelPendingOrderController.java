package com.oak.bookyourshelf.controller.admin_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.model.Product;
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
                      @RequestParam("size") Optional<Integer> size, Model model) {

//        Order order = adminPanelPendingOrderService.get(1);
//        List<Integer> productIds = order.getProductId();
//        productIds.add(7);
//        productIds.add(12);
//        productIds.add(13);
//        productIds.add(39);
//        productIds.add(20);
//        productIds.add(40);
//        order.setProductId(productIds);
//        adminPanelPendingOrderService.save(order);

        Globals.getPageNumbers(page, size, adminPanelPendingOrderService.getPendingOrders(Order.OrderStatus.PENDING),
                model, "orderPage");
        model.addAttribute("profileInformationService", profileInformationService);
        model.addAttribute("productService", productDetailsInformationService);

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

        adminPanelPendingOrderService.save(order);
    }
}
