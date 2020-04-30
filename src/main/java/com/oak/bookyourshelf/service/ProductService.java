package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product get(int id) {
        return productRepository.findById(id).get();
    }
}
