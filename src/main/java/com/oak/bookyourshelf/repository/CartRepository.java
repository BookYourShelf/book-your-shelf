package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Order, Integer> {

}
