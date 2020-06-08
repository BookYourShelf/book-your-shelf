package com.oak.bookyourshelf.service.product_details;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.UserRepository;
import com.oak.bookyourshelf.repository.product_details.ProductDetailsInformationRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailsInformationService {

    final ProductDetailsInformationRepository productDetailsInformationRepository;
    final UserRepository userRepository;

    public ProductDetailsInformationService(ProductDetailsInformationRepository productDetailsInformationRepository,
                                            UserRepository userRepository) {
        this.productDetailsInformationRepository = productDetailsInformationRepository;
        this.userRepository = userRepository;
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
        userRepository.removeAllWishlistByProductId(id);
        // TODO cart item
//        userRepository.removeAllShoppingCartProductId(id);
        productDetailsInformationRepository.deleteById(id);
    }
}
