package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.LoginRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {

    final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public Iterable<User> listAll() {
        return loginRepository.findAll();
    }

    public void save(User user) {
        loginRepository.save(user);
    }

    public User findByEmail(String email) {
        return loginRepository.findByEmail(email);
    }

    public User get(int id) {
        return loginRepository.findById(id).get();
    }

    public void delete(int id) {
        loginRepository.deleteById(id);
    }

    public User update(User user) {
        return loginRepository.save(user);
    }
}
