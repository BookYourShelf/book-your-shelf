package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelCategoryRepository extends CrudRepository<Category, Integer> {
    @Query("SELECT t FROM Category t WHERE t.name = ?1")
    Category findCategoryByName(String name);

    @Query("SELECT t from Category t where t.name = ?1")
    List<Category> findAllByName(String name);
}
