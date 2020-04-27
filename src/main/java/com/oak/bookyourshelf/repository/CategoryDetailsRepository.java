package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryDetailsRepository extends CrudRepository<Category, Integer> {
}
