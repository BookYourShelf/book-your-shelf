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
                return removeAdmin(adminPanelUserRepository.findAllByOrderByUserIdDesc());

            case "ID-asc":
                return removeAdmin(adminPanelUserRepository.findAllByOrderByUserIdAsc());

            case "Name-desc":
                return removeAdmin(adminPanelUserRepository.findAllByOrderByNameDesc());

            case "Name-asc":
                return removeAdmin(adminPanelUserRepository.findAllByOrderByNameAsc());

            case "Surname-desc":
                return removeAdmin(adminPanelUserRepository.findAllByOrderBySurnameDesc());

            case "Surname-asc":
                return removeAdmin(adminPanelUserRepository.findAllByOrderBySurnameAsc());

            case "Email-desc":
                return removeAdmin(adminPanelUserRepository.findAllByOrderByEmailDesc());

            case "Email-asc":
                return removeAdmin(adminPanelUserRepository.findAllByOrderByEmailAsc());
            default:        // id
                return removeAdmin(adminPanelUserRepository.findAllByOrderByUserIdAsc());

        }
    }

    List<User> removeAdmin(List<User> users)
    {
        for(User u :users)
        {
            if(u.getRole() == 1)
            {
                users.remove(u);
                return users;
            }
        }
        return users;
    }


}
