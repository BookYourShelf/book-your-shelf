package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Subcategory;
import org.springframework.data.repository.CrudRepository;

public interface SubcategoryDetailsRepository extends CrudRepository<Subcategory, Integer> {
}
