package com.oak.bookyourshelf.service.category_details;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.repository.category_details.CategoryDetailsInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryDetailsInformationService {

    final CategoryDetailsInformationRepository categoryDetailsInformationRepository;

    public CategoryDetailsInformationService(CategoryDetailsInformationRepository categoryDetailsInformationRepository) {
        this.categoryDetailsInformationRepository = categoryDetailsInformationRepository;
    }

    public Iterable<Category> listAll() {
        return categoryDetailsInformationRepository.findAll();
    }

    public Category get(int id) {
        if (categoryDetailsInformationRepository.findById(id).isPresent())
            return categoryDetailsInformationRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        categoryDetailsInformationRepository.deleteById(id);
    }

    public void save(Category user) {
        categoryDetailsInformationRepository.save(user);
    }
}
