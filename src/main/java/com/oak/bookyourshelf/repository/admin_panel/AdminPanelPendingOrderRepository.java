package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelPendingOrderRepository extends CrudRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.orderStatus = ?1")
    List<Order> getPendingOrders(Order.OrderStatus status);
}
