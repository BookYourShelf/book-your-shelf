package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.repository.AdminPanelCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminPanelCategoryService {
    final
    AdminPanelCategoryRepository adminPanelCategoryRepository;

    public AdminPanelCategoryService(AdminPanelCategoryRepository adminPanelCategoryRepository) {
        this.adminPanelCategoryRepository = adminPanelCategoryRepository;
    }
}
