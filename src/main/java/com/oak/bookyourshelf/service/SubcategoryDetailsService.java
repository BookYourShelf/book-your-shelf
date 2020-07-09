package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.repository.CategoryRepository;
import com.oak.bookyourshelf.repository.SubcategoryDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubcategoryDetailsService {

    final SubcategoryDetailsRepository subcategoryDetailsRepository;
    final CategoryRepository categoryRepository;

    public SubcategoryDetailsService(SubcategoryDetailsRepository subcategoryDetailsRepository, CategoryRepository categoryRepository) {
        this.subcategoryDetailsRepository = subcategoryDetailsRepository;
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Subcategory> listAll() {
        return subcategoryDetailsRepository.findAll();
    }

    public Subcategory get(int id) {
        if (subcategoryDetailsRepository.findById(id).isPresent())
            return subcategoryDetailsRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        Category c = categoryRepository.getCategoryBySubcategories(get(id));
        c.getSubcategories().removeIf(subcategory -> subcategory.getId() == id);
        categoryRepository.save(c);
    }

    public void save(Subcategory user) {
        subcategoryDetailsRepository.save(user);
    }
}
