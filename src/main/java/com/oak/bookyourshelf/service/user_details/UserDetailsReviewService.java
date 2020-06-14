package com.oak.bookyourshelf.service.user_details;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.user_details.UserDetailsReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Review> sortReviewsOfUser(String sortType, User user) {
        switch (sortType) {
            case "date-asc":
                return userDetailsReviewRepository.findByUserOrderByUploadDateAsc(user);

            case "title-desc":
                return userDetailsReviewRepository.findByUserOrderByReviewTitleDesc(user);

            case "title-asc":
                return userDetailsReviewRepository.findByUserOrderByReviewTitleAsc(user);

            case "content-desc":
                return userDetailsReviewRepository.findByUserOrderByReviewContentDesc(user);

            case "content-asc":
                return userDetailsReviewRepository.findByUserOrderByReviewContentAsc(user);

            case "rate-desc":
                return userDetailsReviewRepository.findByUserOrderByScoreOutOf5Desc(user);

            case "rate-asc":
                return userDetailsReviewRepository.findByUserOrderByScoreOutOf5Asc(user);

            default:        // date-desc
                return userDetailsReviewRepository.findByUserOrderByUploadDateDesc(user);
        }
    }

    public List<Review> sortReviewsOfProduct(String sortType, Product product) {
        switch (sortType) {
            case "date-asc":
                return userDetailsReviewRepository.findByProductOrderByUploadDateAsc(product);

            case "title-desc":
                return userDetailsReviewRepository.findByProductOrderByReviewTitleDesc(product);

            case "title-asc":
                return userDetailsReviewRepository.findByProductOrderByReviewTitleAsc(product);

            case "content-desc":
                return userDetailsReviewRepository.findByProductOrderByReviewContentDesc(product);

            case "content-asc":
                return userDetailsReviewRepository.findByProductOrderByReviewContentAsc(product);

            case "rate-desc":
                return userDetailsReviewRepository.findByProductOrderByScoreOutOf5Desc(product);

            case "rate-asc":
                return userDetailsReviewRepository.findByProductOrderByScoreOutOf5Asc(product);

            default:        // date-desc
                return userDetailsReviewRepository.findByProductOrderByUploadDateDesc(product);
        }
    }
}
