package com.oak.bookyourshelf.service.subcategory_details;

import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.repository.subcategory_details.SubcategoryDetailsSubcategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubcategoryDetailsSubcategoryService {

    final SubcategoryDetailsSubcategoryRepository subcategoryDetailsSubcategoryRepository;

    public SubcategoryDetailsSubcategoryService(SubcategoryDetailsSubcategoryRepository subcategoryDetailsSubcategoryRepository) {
        this.subcategoryDetailsSubcategoryRepository = subcategoryDetailsSubcategoryRepository;
    }

    public Iterable<Subcategory> listAll() {
        return subcategoryDetailsSubcategoryRepository.findAll();
    }

    public Subcategory get(int id) {
        if (subcategoryDetailsSubcategoryRepository.findById(id).isPresent())
            return subcategoryDetailsSubcategoryRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        subcategoryDetailsSubcategoryRepository.deleteById(id);
    }

    public void save(Subcategory subcategory) {
        subcategoryDetailsSubcategoryRepository.save(subcategory);
    }
}
