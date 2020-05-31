package com.oak.bookyourshelf.service.subcategory_details;

import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.repository.subcategory_details.SubcategoryDetailsInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubcategoryDetailsInformationService {
    final SubcategoryDetailsInformationRepository subcategoryDetailsInformationRepository;

    public SubcategoryDetailsInformationService(SubcategoryDetailsInformationRepository subcategoryDetailsInformationRepository) {
        this.subcategoryDetailsInformationRepository = subcategoryDetailsInformationRepository;
    }

    public Iterable<Subcategory> listAll() {
        return subcategoryDetailsInformationRepository.findAll();
    }

    public Subcategory get(int id) {
        if (subcategoryDetailsInformationRepository.findById(id).isPresent())
            return subcategoryDetailsInformationRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        subcategoryDetailsInformationRepository.deleteById(id);
    }

    public void save(Subcategory subcategory) {
        subcategoryDetailsInformationRepository.save(subcategory);
    }
}
