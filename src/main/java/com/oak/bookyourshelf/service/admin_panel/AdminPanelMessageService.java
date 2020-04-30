package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.Message;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminPanelMessageService {
    final AdminPanelMessageRepository adminPanelMessageRepository;

    public AdminPanelMessageService(AdminPanelMessageRepository adminPanelMessageRepository) {
        this.adminPanelMessageRepository = adminPanelMessageRepository;
    }

    public Iterable<Message> listAll() {
        return adminPanelMessageRepository.findAll();
    }

    public Message get(int id) {
        return adminPanelMessageRepository.findById(id).get();
    }

    public void delete(int id) {
        adminPanelMessageRepository.deleteById(id);
    }

    public void save(Message message) {
        adminPanelMessageRepository.save(message);
    }
}
