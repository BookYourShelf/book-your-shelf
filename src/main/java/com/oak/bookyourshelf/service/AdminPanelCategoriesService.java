package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.repository.AdminPanelCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminPanelCategoriesService {
    final
    AdminPanelCategoryRepository adminPanelCategoryRepository;

    public AdminPanelCategoriesService(AdminPanelCategoryRepository adminPanelCategoryRepository) {
        this.adminPanelCategoryRepository = adminPanelCategoryRepository;
    }
}
