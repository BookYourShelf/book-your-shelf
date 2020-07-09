package com.oak.bookyourshelf.service.profile;

import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.profile.ProfileReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Review> sortReviews(String sortType, User user) {
        switch (sortType) {
            case "date-asc":
                return profileReviewRepository.findByUserOrderByUploadDateAsc(user);

            case "title-desc":
                return profileReviewRepository.findByUserOrderByReviewTitleDesc(user);

            case "title-asc":
                return profileReviewRepository.findByUserOrderByReviewTitleAsc(user);

            case "content-desc":
                return profileReviewRepository.findByUserOrderByReviewContentDesc(user);

            case "content-asc":
                return profileReviewRepository.findByUserOrderByReviewContentAsc(user);

            case "rate-desc":
                return profileReviewRepository.findByUserOrderByScoreOutOf5Desc(user);

            case "rate-asc":
                return profileReviewRepository.findByUserOrderByScoreOutOf5Asc(user);

            default:        // date-desc
                return profileReviewRepository.findByUserOrderByUploadDateDesc(user);
        }
    }
}
