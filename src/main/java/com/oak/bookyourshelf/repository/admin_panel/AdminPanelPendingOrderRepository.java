package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelPendingOrderRepository extends CrudRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.orderStatus = ?1")
    List<Order> getPendingOrders(Order.OrderStatus status);

    List<Order> findByOrderStatusOrderByOrderDateDesc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatusOrderByOrderDateAsc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatusOrderByUserIdDesc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatusOrderByUserIdAsc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatusOrderByOrderCodeDesc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatusOrderByOrderCodeAsc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatusOrderByTotalAmountDesc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatusOrderByTotalAmountAsc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatusOrderByShippingCompanyDesc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatusOrderByShippingCompanyAsc(Order.OrderStatus orderStatus);

    List<Order> findByOrderStatus(Order.OrderStatus orderStatus);
}
