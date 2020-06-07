package com.oak.bookyourshelf.repository.user_details;

import com.oak.bookyourshelf.model.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDetailsOrderRepository extends CrudRepository<Order, Integer> {

    List<Order> findByUserIdOrderByOrderDateDesc(int userId);

    List<Order> findByUserIdOrderByOrderDateAsc(int userId);

    List<Order> findByUserIdOrderByTotalAmountDesc(int userId);

    List<Order> findByUserIdOrderByTotalAmountAsc(int userId);

    List<Order> findByUserIdOrderByShippingCompanyDesc(int userId);

    List<Order> findByUserIdOrderByShippingCompanyAsc(int userId);
}
