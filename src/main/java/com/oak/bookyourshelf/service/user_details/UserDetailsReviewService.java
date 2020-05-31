package com.oak.bookyourshelf.service.user_details;

import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.repository.user_details.UserDetailsReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsReviewService {
    final UserDetailsReviewRepository userDetailsReviewRepository;

    public UserDetailsReviewService(UserDetailsReviewRepository userDetailsReviewRepository) {
        this.userDetailsReviewRepository = userDetailsReviewRepository;
    }

    public Iterable<Review> listAll() {
        return userDetailsReviewRepository.findAll();
    }

    public Review findReviewById(int id) {
        return userDetailsReviewRepository.findReviewById(id);
    }

    public void delete(int id) {
        userDetailsReviewRepository.removeProductReviewByReviewId(id);
        userDetailsReviewRepository.removeUserReviewByReviewId(id);
        userDetailsReviewRepository.deleteById(id);
    }

    public void save(Review review) {
        userDetailsReviewRepository.save(review);
    }
}
