package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oak.bookyourshelf.repository.RegisterRepository;

@Service
@Transactional
public class RegisterService {

    final RegisterRepository registerRepository;

    public RegisterService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public Iterable<User> listAll() {
        return registerRepository.findAll();
    }

    public void save(User user) {
        registerRepository.save(user);
    }

    public User findByEmail(String email) {
        return registerRepository.findByEmail(email);
    }

    public User get(int id) {
        return registerRepository.findById(id).get();
    }

    public void delete(int id) {
        registerRepository.deleteById(id);
    }
}
