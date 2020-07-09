package com.oak.bookyourshelf.service.profile;

import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.repository.profile.ProfileOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfileOrderService {
    final ProfileOrderRepository profileOrderRepository;

    public ProfileOrderService(ProfileOrderRepository profileOrderRepository) {
        this.profileOrderRepository = profileOrderRepository;
    }

    public List<Order> getAllOrdersOfUser(int userId) {
        return profileOrderRepository.findByUserId(userId);
    }

    public List<Order> sortOrders(String sortType, int userId) {
        switch (sortType) {
            case "time-asc":
                return eliminateUnpayedOrders(profileOrderRepository.findByUserIdOrderByOrderDateAsc(userId));

            case "price-desc":
                return eliminateUnpayedOrders(profileOrderRepository.findByUserIdOrderByTotalAmountDesc(userId));

            case "price-asc":
                return eliminateUnpayedOrders(profileOrderRepository.findByUserIdOrderByTotalAmountAsc(userId));

            case "company-desc":
                return eliminateUnpayedOrders(profileOrderRepository.findByUserIdOrderByShippingCompanyDesc(userId));

            case "company-asc":
                return eliminateUnpayedOrders(profileOrderRepository.findByUserIdOrderByShippingCompanyAsc(userId));

            default:        // time-desc
                return eliminateUnpayedOrders(profileOrderRepository.findByUserIdOrderByOrderDateDesc(userId));
        }
    }

    public List<Order> eliminateUnpayedOrders(List<Order> orders) {
        orders.removeIf(o -> o.getPaymentStatus() == Order.PaymentStatus.NULL);
        return orders;
    }
}
