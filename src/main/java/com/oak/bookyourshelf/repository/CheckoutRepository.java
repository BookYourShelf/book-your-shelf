package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface CheckoutRepository extends CrudRepository<Order,Integer> {

}
