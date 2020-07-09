package com.oak.bookyourshelf.service.user_details;


import com.oak.bookyourshelf.repository.user_details.UserDetailsWishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserDetailsWishListService {
    final UserDetailsWishListRepository userDetailsWishListRepository;

    public UserDetailsWishListService(UserDetailsWishListRepository userDetailsWishListRepository){
        this.userDetailsWishListRepository = userDetailsWishListRepository;
    }


}
