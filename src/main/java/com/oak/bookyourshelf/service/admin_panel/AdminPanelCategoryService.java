package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.repository.admin_panel.AdminPanelCategoryRepository;
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
