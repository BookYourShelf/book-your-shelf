package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


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

    public List<User> sortUsers(String sortType)
    {
        switch (sortType) {
            case "ID-desc":
                return adminPanelUserRepository.findAllByOrderByUserIdDesc();

            case "ID-asc":
                return adminPanelUserRepository.findAllByOrderByUserIdAsc();

            case "Name-desc":
                return adminPanelUserRepository.findAllByOrderByNameDesc();

            case "Name-asc":
                return adminPanelUserRepository.findAllByOrderByNameAsc();

            case "Surname-desc":
                return adminPanelUserRepository.findAllByOrderBySurnameDesc();

            case "Surname-asc":
                return adminPanelUserRepository.findAllByOrderBySurnameAsc();

            case "Email-desc":
                return adminPanelUserRepository.findAllByOrderByEmailDesc();

            case "Email-asc":
                return adminPanelUserRepository.findAllByOrderByEmailAsc();
            default:        // id
                return adminPanelUserRepository.findAllByOrderByUserIdAsc();

        }
    }


}
