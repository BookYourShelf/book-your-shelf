package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.repository.SubcategoryDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubcategoryDetailsService {
    final
    SubcategoryDetailsRepository subcategoryDetailsRepository;

    public SubcategoryDetailsService(SubcategoryDetailsRepository subcategoryDetailsRepository) {
        this.subcategoryDetailsRepository = subcategoryDetailsRepository;
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
        subcategoryDetailsRepository.deleteById(id);
    }

    public void save(Subcategory user) {
        subcategoryDetailsRepository.save(user);
    }
}
