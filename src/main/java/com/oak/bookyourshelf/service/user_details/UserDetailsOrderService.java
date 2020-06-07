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

    public List<Order> sortOrders(String sortType, int userId) {
        switch (sortType) {
            case "time-asc":
                return userDetailsOrderRepository.findByUserIdOrderByOrderDateAsc(userId);

            case "price-desc":
                return userDetailsOrderRepository.findByUserIdOrderByTotalAmountDesc(userId);

            case "price-asc":
                return userDetailsOrderRepository.findByUserIdOrderByTotalAmountAsc(userId);

            case "company-desc":
                return userDetailsOrderRepository.findByUserIdOrderByShippingCompanyDesc(userId);

            case "company-asc":
                return userDetailsOrderRepository.findByUserIdOrderByShippingCompanyAsc(userId);

            default:        // time-desc
                return userDetailsOrderRepository.findByUserIdOrderByOrderDateDesc(userId);
        }
    }
}
