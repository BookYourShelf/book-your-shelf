package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface AdminPanelCategoryRepository extends CrudRepository<Category, Integer> {
}
