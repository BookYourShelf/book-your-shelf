package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Log;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;

public interface NewPasswordRepository extends CrudRepository<Log, String>{
    Log findByToken(String token);
}
