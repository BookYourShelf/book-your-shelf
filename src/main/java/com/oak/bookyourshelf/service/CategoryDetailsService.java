package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.repository.CategoryDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryDetailsService {

    final CategoryDetailsRepository categoryDetailsRepository;

    public CategoryDetailsService(CategoryDetailsRepository categoryDetailsRepository) {
        this.categoryDetailsRepository = categoryDetailsRepository;
    }

    public Iterable<Category> listAll() {
        return categoryDetailsRepository.findAll();
    }

    public Category get(int id) {
        if (categoryDetailsRepository.findById(id).isPresent())
            return categoryDetailsRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        categoryDetailsRepository.deleteById(id);
    }

    public void save(Category user) {
        categoryDetailsRepository.save(user);
    }
}
