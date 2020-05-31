package com.oak.bookyourshelf.repository.profile;

import com.oak.bookyourshelf.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProfileReviewRepository extends CrudRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.reviewId = ?1")
    Review findReviewById(int id);
}
