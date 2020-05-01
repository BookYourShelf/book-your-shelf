package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Log;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.ForgotPasswordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ForgotPasswordService {

    final ForgotPasswordRepository forgetPasswordRepository;

    public ForgotPasswordService(ForgotPasswordRepository forgetPasswordRepository) {
        this.forgetPasswordRepository = forgetPasswordRepository;
    }

    public User findByEmail(String email) {
        return forgetPasswordRepository.findByEmail(email);
    }

    public void save(Log log) {
        forgetPasswordRepository.save(log);
    }
}
