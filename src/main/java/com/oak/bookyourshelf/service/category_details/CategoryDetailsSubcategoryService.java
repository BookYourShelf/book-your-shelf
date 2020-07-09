package com.oak.bookyourshelf.service.category_details;

import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.repository.category_details.CategoryDetailsSubcategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryDetailsSubcategoryService {

    final CategoryDetailsSubcategoryRepository categoryDetailsSubcategoryRepository;

    public CategoryDetailsSubcategoryService(CategoryDetailsSubcategoryRepository categoryDetailsSubcategoryRepository) {
        this.categoryDetailsSubcategoryRepository = categoryDetailsSubcategoryRepository;
    }

    public Iterable<Subcategory> listAll() {
        return categoryDetailsSubcategoryRepository.findAll();
    }

    public Subcategory get(int id) {
        if (categoryDetailsSubcategoryRepository.findById(id).isPresent())
            return categoryDetailsSubcategoryRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        categoryDetailsSubcategoryRepository.deleteById(id);
    }

    public void save(Subcategory user) {
        categoryDetailsSubcategoryRepository.save(user);
    }
}
