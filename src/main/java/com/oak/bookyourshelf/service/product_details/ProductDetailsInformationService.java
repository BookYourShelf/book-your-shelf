package com.oak.bookyourshelf.service.product_details;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.product_details.ProductDetailsInformationRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailsInformationService {

    final ProductDetailsInformationRepository productDetailsInformationRepository;

    public ProductDetailsInformationService(ProductDetailsInformationRepository productDetailsInformationRepository) {
        this.productDetailsInformationRepository = productDetailsInformationRepository;
    }

    public Product get(int id) {
        return productDetailsInformationRepository.findById(id).get();
    }

    public void save(Product product) {
        productDetailsInformationRepository.save(product);
    }

    public Product getByBarcode(String barcode) {
        return productDetailsInformationRepository.findProductByBarcode(barcode);
    }

    public Product getByISBN(String isbn) {
        return productDetailsInformationRepository.findBookByISBN(isbn);
    }

    public void deleteProduct(int id) {
        productDetailsInformationRepository.deleteById(id);
    }
}
