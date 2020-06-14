package com.oak.bookyourshelf.repository.profile;

import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfileReviewRepository extends CrudRepository<Review, Integer> {

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

    List<Review> findByUserOrderByUploadDateDesc(User user);

    List<Review> findByUserOrderByUploadDateAsc(User user);

    List<Review> findByUserOrderByReviewTitleDesc(User user);

    List<Review> findByUserOrderByReviewTitleAsc(User user);

    List<Review> findByUserOrderByReviewContentDesc(User user);

    List<Review> findByUserOrderByReviewContentAsc(User user);

    List<Review> findByUserOrderByScoreOutOf5Desc(User user);

    List<Review> findByUserOrderByScoreOutOf5Asc(User user);
}
