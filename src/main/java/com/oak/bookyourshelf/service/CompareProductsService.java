package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.CompareProductsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CompareProductsService {
    final CompareProductsRepository compareProductsRepository;

    public CompareProductsService(CompareProductsRepository compareProductsRepository) {
        this.compareProductsRepository = compareProductsRepository;
    }
    public Product get(int id) {
        return compareProductsRepository.findById(id).get();
    }


}
