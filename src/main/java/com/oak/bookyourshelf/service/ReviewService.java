package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {
    final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Iterable<Review> listAll() {
        return reviewRepository.findAll();
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public Review get(int id) {
        return reviewRepository.findById(id).get();
    }

    public void delete(int id) {
        reviewRepository.deleteById(id);
    }

    public Review checkUserReviewsForProduct(Product product, User user) {
        return reviewRepository.getReviewByProductAndUser(product, user);
    }
}
