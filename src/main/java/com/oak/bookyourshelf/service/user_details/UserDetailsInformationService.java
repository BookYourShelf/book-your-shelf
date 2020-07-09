package com.oak.bookyourshelf.service.user_details;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.user_details.UserDetailsInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserDetailsInformationService {
    final UserDetailsInformationRepository userDetailsInformationRepository;

    public UserDetailsInformationService(UserDetailsInformationRepository userDetailsInformationRepository) {
        this.userDetailsInformationRepository = userDetailsInformationRepository;
    }

    public Iterable<User> listAll() {
        return userDetailsInformationRepository.findAll();
    }

    public User get(int id) {
        return userDetailsInformationRepository.findById(id).get();
    }

    public void delete(int id) {
        userDetailsInformationRepository.deleteById(id);
    }

    public void save(User user) {
        userDetailsInformationRepository.save(user);
    }

    public User getByEmail(String email) {
        return userDetailsInformationRepository.findUserByEmail(email);
    }

    public List<Integer> findAllIds() {
        return userDetailsInformationRepository.findAllIds();
    }
}
