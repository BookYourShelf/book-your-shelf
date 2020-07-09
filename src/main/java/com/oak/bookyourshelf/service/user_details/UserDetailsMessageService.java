package com.oak.bookyourshelf.service.user_details;

import com.oak.bookyourshelf.repository.user_details.UserDetailsMessageRepository;
import com.oak.bookyourshelf.model.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsMessageService {
    final UserDetailsMessageRepository userDetailsMessageRepository;

    public UserDetailsMessageService(UserDetailsMessageRepository userDetailsMessageRepository) {
        this.userDetailsMessageRepository = userDetailsMessageRepository;
    }

    public Iterable<Message> listAll() {
        return userDetailsMessageRepository.findAll();
    }

    public Message get(int id) {
        return userDetailsMessageRepository.findById(id).get();
    }

    public void delete(int id) {
        userDetailsMessageRepository.deleteById(id);
    }

    public void save(Message message) {
        userDetailsMessageRepository.save(message);
    }
}
