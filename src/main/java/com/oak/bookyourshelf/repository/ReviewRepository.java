package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
}
