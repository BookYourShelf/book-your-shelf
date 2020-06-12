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
                return profileOrderRepository.findByUserIdOrderByOrderDateAsc(userId);

            case "price-desc":
                return profileOrderRepository.findByUserIdOrderByTotalAmountDesc(userId);

            case "price-asc":
                return profileOrderRepository.findByUserIdOrderByTotalAmountAsc(userId);

            case "company-desc":
                return profileOrderRepository.findByUserIdOrderByShippingCompanyDesc(userId);

            case "company-asc":
                return profileOrderRepository.findByUserIdOrderByShippingCompanyAsc(userId);

            default:        // time-desc
                return profileOrderRepository.findByUserIdOrderByOrderDateDesc(userId);
        }
    }
}
