package com.oak.bookyourshelf.service.user_details;


import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.user_details.UserDetailsSearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserDetailsSearchService {

    final UserDetailsSearchRepository userDetailsSearchRepository;

    public UserDetailsSearchService(UserDetailsSearchRepository userDetailsSearchRepository) {
        this.userDetailsSearchRepository = userDetailsSearchRepository;
    }

    public Iterable<User> listAll() {
        return userDetailsSearchRepository.findAll();
    }

    public User get(int id) {
        return userDetailsSearchRepository.findById(id).get();
    }

    public void delete(int id) {
        userDetailsSearchRepository.deleteById(id);
    }

    public void save(User user) {
        userDetailsSearchRepository.save(user);
    }

    public User getByEmail(String email) {
        return userDetailsSearchRepository.findUserByEmail(email);
    }

    public List<Integer> findAllIds() {
        return userDetailsSearchRepository.findAllIds();
    }
}
