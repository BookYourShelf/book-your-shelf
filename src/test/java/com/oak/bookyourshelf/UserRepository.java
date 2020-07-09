package com.oak.bookyourshelf;

import com.oak.bookyourshelf.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
