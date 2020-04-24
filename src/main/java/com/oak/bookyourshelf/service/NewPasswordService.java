package com.oak.bookyourshelf.service;


import com.oak.bookyourshelf.model.Log;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.NewPasswordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional

public class NewPasswordService {

    final
    NewPasswordRepository newPasswordRepository;

    public NewPasswordService(NewPasswordRepository newPasswordRepository) {
        this.newPasswordRepository = newPasswordRepository;
    }

    public Log findByToken(String token) {
        return newPasswordRepository.findByToken(token);
    }

}
