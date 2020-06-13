package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Modifying
    @Transactional
    @Query(value = "delete from user_wish_list where wish_list_product_id like ?1", nativeQuery = true)
    void removeAllWishlistByProductId(int id);

    @Modifying
    @Transactional
    @Query(value = "delete from user_shopping_cart where shopping_cart_cart_item_id like ?1", nativeQuery = true)
    void removeAllShoppingCartProductId(int id);

}
