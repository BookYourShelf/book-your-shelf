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
}