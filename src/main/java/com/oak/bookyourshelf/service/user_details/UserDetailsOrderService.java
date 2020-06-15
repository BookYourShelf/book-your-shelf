package com.oak.bookyourshelf.service.user_details;

import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.repository.user_details.UserDetailsOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsOrderService {

    final UserDetailsOrderRepository userDetailsOrderRepository;

    public UserDetailsOrderService(UserDetailsOrderRepository userDetailsOrderRepository) {
        this.userDetailsOrderRepository = userDetailsOrderRepository;
    }

    public Order get(int id) {
        return userDetailsOrderRepository.findById(id).get();
    }

    public void save(Order order) {
        userDetailsOrderRepository.save(order);
    }

    public List<Order> getAllOrdersOfUser(int userId) {
        return userDetailsOrderRepository.findByUserId(userId);
    }

    public List<Order> sortOrders(String sortType, int userId) {
        switch (sortType) {
            case "time-asc":
                return eliminateUnpayedOrders(userDetailsOrderRepository.findByUserIdOrderByOrderDateAsc(userId));

            case "code-desc":
                return eliminateUnpayedOrders(userDetailsOrderRepository.findByUserIdOrderByOrderCodeDesc(userId));

            case "code-asc":
                return eliminateUnpayedOrders(userDetailsOrderRepository.findByUserIdOrderByOrderCodeAsc(userId));

            case "price-desc":
                return eliminateUnpayedOrders(userDetailsOrderRepository.findByUserIdOrderByTotalAmountDesc(userId));

            case "price-asc":
                return eliminateUnpayedOrders(userDetailsOrderRepository.findByUserIdOrderByTotalAmountAsc(userId));

            case "company-desc":
                return eliminateUnpayedOrders(userDetailsOrderRepository.findByUserIdOrderByShippingCompanyDesc(userId));

            case "company-asc":
                return eliminateUnpayedOrders(userDetailsOrderRepository.findByUserIdOrderByShippingCompanyAsc(userId));

            default:        // time-desc
                return eliminateUnpayedOrders(userDetailsOrderRepository.findByUserIdOrderByOrderDateDesc(userId));
        }
    }

    public List<Order> eliminateUnpayedOrders(List<Order> orders) {
        orders.removeIf(o -> o.getPaymentStatus() == Order.PaymentStatus.NULL);
        return orders;
    }
}
