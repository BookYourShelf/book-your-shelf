package com.oak.bookyourshelf.repository.user_details;

import com.oak.bookyourshelf.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailsWishListRepository extends CrudRepository<Product, Integer> {
}
