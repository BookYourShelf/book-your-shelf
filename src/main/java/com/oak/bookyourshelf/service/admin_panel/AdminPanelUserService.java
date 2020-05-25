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

    public Page<User> findPaginated(Pageable pageable) {
        List users = (List) adminPanelUserRepository.findAllCustomers();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;

        if (users.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }

        Page<User> userPage
                = new PageImpl<User>(list, PageRequest.of(currentPage, pageSize), users.size());

        return userPage;
    }
}
