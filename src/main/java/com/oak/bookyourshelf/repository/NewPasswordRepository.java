package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Log;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NewPasswordRepository extends CrudRepository<Log, String>{
    @Query("SELECT t FROM Log t WHERE t.token = ?1")
    Log findByToken(String token);
}
