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

    public List<Order> sortOrders(String sortType, Order.OrderStatus orderStatus) {
        switch (sortType) {
            case "time-asc":
                return adminPanelPendingOrderRepository.findByOrderStatusOrderByOrderDateAsc(orderStatus);

            case "ID-desc":
                return adminPanelPendingOrderRepository.findByOrderStatusOrderByUserIdDesc(orderStatus);

            case "ID-asc":
                return adminPanelPendingOrderRepository.findByOrderStatusOrderByUserIdAsc(orderStatus);

            case "price-desc":
                return adminPanelPendingOrderRepository.findByOrderStatusOrderByTotalAmountDesc(orderStatus);

            case "price-asc":
                return adminPanelPendingOrderRepository.findByOrderStatusOrderByTotalAmountAsc(orderStatus);

            case "company-desc":
                return adminPanelPendingOrderRepository.findByOrderStatusOrderByShippingCompanyDesc(orderStatus);

            case "company-asc":
                return adminPanelPendingOrderRepository.findByOrderStatusOrderByShippingCompanyAsc(orderStatus);

            default:        // time-desc
                return adminPanelPendingOrderRepository.findByOrderStatusOrderByOrderDateDesc(orderStatus);
        }
    }
}