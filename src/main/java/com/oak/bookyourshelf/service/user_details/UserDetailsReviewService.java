package com.oak.bookyourshelf.service.user_details;

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
}
