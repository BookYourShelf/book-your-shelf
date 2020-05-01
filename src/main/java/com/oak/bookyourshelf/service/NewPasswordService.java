package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Log;
import com.oak.bookyourshelf.repository.NewPasswordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewPasswordService {

    final NewPasswordRepository newPasswordRepository;

    public NewPasswordService(NewPasswordRepository newPasswordRepository) {
        this.newPasswordRepository = newPasswordRepository;
    }

    public Log findByToken(String token) {
        return newPasswordRepository.findByToken(token);
    }

    public void save(Log log) {
        newPasswordRepository.save(log);
    }
}
