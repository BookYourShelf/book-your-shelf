package com.oak.bookyourshelf.service.profile;

import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.repository.profile.ProfileReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileReviewService {
    final ProfileReviewRepository profileReviewRepository;

    public ProfileReviewService(ProfileReviewRepository profileReviewRepository) {
        this.profileReviewRepository = profileReviewRepository;
    }

    public Iterable<Review> listAll() {
        return profileReviewRepository.findAll();
    }

    public Review findReviewById(int id) {
        return profileReviewRepository.findReviewById(id);
    }

    public void delete(int id) {
        profileReviewRepository.removeProductReviewByReviewId(id);
        profileReviewRepository.removeUserReviewByReviewId(id);
        profileReviewRepository.deleteById(id);
    }

    public void save(Review review) {
        profileReviewRepository.save(review);
    }
}
