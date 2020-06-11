package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Category getCategoryByName(String name);
}
