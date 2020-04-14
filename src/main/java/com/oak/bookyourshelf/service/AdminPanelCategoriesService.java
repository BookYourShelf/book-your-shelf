package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.repository.AdminPanelCategoriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminPanelCategoriesService {
    final
    AdminPanelCategoriesRepository adminPanelCategoriesRepository;

    public AdminPanelCategoriesService(AdminPanelCategoriesRepository adminPanelCategoriesRepository) {
        this.adminPanelCategoriesRepository = adminPanelCategoriesRepository;
    }
}
