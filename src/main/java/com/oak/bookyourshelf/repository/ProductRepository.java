package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
