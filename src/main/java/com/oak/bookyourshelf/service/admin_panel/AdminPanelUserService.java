package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminPanelUserService {

    final AdminPanelUserRepository adminPanelUserRepository;

    public AdminPanelUserService(AdminPanelUserRepository adminPanelUserRepository) {
        this.adminPanelUserRepository = adminPanelUserRepository;
    }

    public Iterable<User> listAllCustomers() {
        return adminPanelUserRepository.findAllCustomers();
    }

    public User get(int id) {
        return adminPanelUserRepository.findById(id).get();
    }

    public void delete(int id) {
        adminPanelUserRepository.deleteById(id);
    }

    public void save(User user) {
        adminPanelUserRepository.save(user);
    }
}
