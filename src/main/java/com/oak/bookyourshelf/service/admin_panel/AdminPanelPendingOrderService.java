package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelPendingOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminPanelPendingOrderService {

    final AdminPanelPendingOrderRepository adminPanelPendingOrderRepository;

    public AdminPanelPendingOrderService(AdminPanelPendingOrderRepository adminPanelPendingOrderRepository) {
        this.adminPanelPendingOrderRepository = adminPanelPendingOrderRepository;
    }

    public List<Order> getPendingOrders(Order.OrderStatus status) {
        return adminPanelPendingOrderRepository.getPendingOrders(status);
    }

    public Order get(int id) {
        return adminPanelPendingOrderRepository.findById(id).get();
    }

    public void save(Order order) {
        adminPanelPendingOrderRepository.save(order);
    }

    public Iterable<Order> listAll(Order.OrderStatus orderStatus) {
        return adminPanelPendingOrderRepository.findByOrderStatus(orderStatus);
    }

    public List<Order> sortOrders(String sortType, Order.OrderStatus orderStatus) {
        switch (sortType) {
            case "time-asc":
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByOrderDateAsc(orderStatus));

            case "ID-desc":
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByUserIdDesc(orderStatus));

            case "ID-asc":
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByUserIdAsc(orderStatus));

            case "code-desc":
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByOrderCodeDesc(orderStatus));

            case "code-asc":
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByOrderCodeAsc(orderStatus));

            case "price-desc":
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByTotalAmountDesc(orderStatus));

            case "price-asc":
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByTotalAmountAsc(orderStatus));

            case "company-desc":
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByShippingCompanyDesc(orderStatus));

            case "company-asc":
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByShippingCompanyAsc(orderStatus));

            default:        // time-desc
                return eliminateUnpayedOrders(adminPanelPendingOrderRepository.findByOrderStatusOrderByOrderDateDesc(orderStatus));
        }
    }

    public List<Order> eliminateUnpayedOrders(List<Order> orders) {
        orders.removeIf(o -> o.getPaymentStatus() == Order.PaymentStatus.NULL);
        return orders;
    }
}