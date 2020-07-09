package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.CartItem;
import com.oak.bookyourshelf.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
    @Modifying
    @Transactional
    @Query(value = "delete from cart_item where product_product_id like ?1", nativeQuery = true)
    void removeAllByProductId(int id);
}
