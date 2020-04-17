package com.oak.bookyourshelf.repository.user_details;

import com.oak.bookyourshelf.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailsMessageRepository extends CrudRepository<Message, Integer> {
}
