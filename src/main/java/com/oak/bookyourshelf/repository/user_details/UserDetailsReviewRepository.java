package com.oak.bookyourshelf.repository.user_details;

import com.oak.bookyourshelf.model.Review;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserDetailsReviewRepository extends CrudRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.reviewId = ?1")
    Review findReviewById(int id);

    @Modifying
    @Transactional
    @Query(value = "delete from product_reviews where reviews_review_id like ?1", nativeQuery = true)
    void removeProductReviewByReviewId(int id);

    @Modifying
    @Transactional
    @Query(value = "delete from user_reviews where reviews_review_id like ?1", nativeQuery = true)
    void removeUserReviewByReviewId(int id);
}
