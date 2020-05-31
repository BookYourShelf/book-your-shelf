package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.userId = ?1 AND  r.productId = ?2")
    Review checkUserReviewsForProduct(int userId, int productId);
}
