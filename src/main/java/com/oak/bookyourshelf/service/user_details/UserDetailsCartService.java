package com.oak.bookyourshelf.service.user_details;

import com.oak.bookyourshelf.repository.user_details.UserDetailsCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserDetailsCartService {

    final UserDetailsCartRepository userDetailsCartRepository;

    public UserDetailsCartService(UserDetailsCartRepository userDetailsCartRepository){
        this.userDetailsCartRepository = userDetailsCartRepository;
    }
}
