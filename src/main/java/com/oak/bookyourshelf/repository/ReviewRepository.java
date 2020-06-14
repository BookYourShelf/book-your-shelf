package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ReviewRepository extends CrudRepository<Review, Integer> {

    Review getReviewByProductAndUser(Product product, User user);

    @Modifying
    @Transactional
    @Query(value = "delete from product_reviews where product_product_id like ?1", nativeQuery = true)
    void removeAllReviewsProductId(int id);
}
