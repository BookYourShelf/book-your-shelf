package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdminPanelCategoryRepository extends CrudRepository<Category, Integer> {
    @Query("SELECT t FROM Category t WHERE t.name = ?1")
    Category findCategoryByName(String name);
}
