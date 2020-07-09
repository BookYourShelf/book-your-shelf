package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LoginRepository extends CrudRepository<User, Integer> {
    @Query("SELECT t FROM User t WHERE t.email = ?1")
    User findByEmail(String email);
}
