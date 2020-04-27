package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.ProductDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailsService {

    final ProductDetailsRepository productDetailsRepository;

    public ProductDetailsService(ProductDetailsRepository productDetailsRepository) {
        this.productDetailsRepository = productDetailsRepository;
    }

    public Product get(int id) {
        return productDetailsRepository.findById(id).get();
    }
}
