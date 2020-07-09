package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Category> listAll() {
        return categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public Category get(int id) {
        return categoryRepository.findById(id).get();
    }

    public Category get(String name) {
        return categoryRepository.getCategoryByName(name);
    }

    public void delete(int id) {
        categoryRepository.deleteById(id);
    }
}
