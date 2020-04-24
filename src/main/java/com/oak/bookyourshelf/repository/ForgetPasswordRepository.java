package com.oak.bookyourshelf.repository;


import com.oak.bookyourshelf.model.Log;
import com.oak.bookyourshelf.model.User;
import org.springframework.data.repository.CrudRepository;

public interface ForgetPasswordRepository extends CrudRepository<Log, String> {
    User findByEmail(String email);
}
