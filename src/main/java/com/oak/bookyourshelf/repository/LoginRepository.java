package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.User;
import org.springframework.data.repository.CrudRepository;

public interface LoginRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

}
