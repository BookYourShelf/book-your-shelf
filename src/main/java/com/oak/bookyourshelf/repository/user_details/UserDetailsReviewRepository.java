package com.oak.bookyourshelf.repository.user_details;

import com.oak.bookyourshelf.model.Review;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailsReviewRepository extends CrudRepository<Review, Integer> {
}
