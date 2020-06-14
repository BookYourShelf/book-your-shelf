package com.oak.bookyourshelf.service.product_details;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.CartItemRepository;
import com.oak.bookyourshelf.repository.CategoryRepository;
import com.oak.bookyourshelf.repository.ReviewRepository;
import com.oak.bookyourshelf.repository.UserRepository;
import com.oak.bookyourshelf.repository.product_details.ProductDetailsInformationRepository;
import com.oak.bookyourshelf.service.user_details.UserDetailsReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailsInformationService {

    final ProductDetailsInformationRepository productDetailsInformationRepository;
    final UserRepository userRepository;
    final CartItemRepository cartItemRepository;
    final UserDetailsReviewService reviewService;
    final CategoryRepository categoryRepository;
    final ReviewRepository reviewRepository;


    public ProductDetailsInformationService(ProductDetailsInformationRepository productDetailsInformationRepository,
                                            UserRepository userRepository,
                                            CartItemRepository cartItemRepository,
                                            UserDetailsReviewService reviewService, CategoryRepository categoryRepository, ReviewRepository reviewRepository) {
        this.productDetailsInformationRepository = productDetailsInformationRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.reviewService = reviewService;
        this.categoryRepository = categoryRepository;

        this.reviewRepository = reviewRepository;
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

        List<User> userList = (List<User>) userRepository.findAll();
        for (User user : userList) {
            user.getShoppingCart().removeIf(cartItem -> cartItem.getProduct().getProductId() == id);
            user.getOrders().forEach(order -> order.getProducts().removeIf(cartItem -> cartItem.getProduct().getProductId() == id));
            user.getReviews().removeIf(x -> x.getProduct().getProductId() == id);
            userRepository.save(user);
        }

        cartItemRepository.removeAllByProductId(id);
        reviewRepository.removeAllReviewsProductId(id);

        List<Category> categories = (List<Category>) categoryRepository.findAll();
        for (Category c : categories) {
            c.getBookSet().removeIf(book -> book.getProductId() == id);
            for (Subcategory s : c.getSubcategories()) {
                s.getBooks().removeIf(book -> book.getProductId() == id);
                traverse(s, id);
            }
            categoryRepository.save(c);
        }
    }

    void traverse(Subcategory subcategory, int id) {
        for (Subcategory s : subcategory.getSubcategories()) {
            s.getBooks().removeIf(book -> book.getProductId() == id);
            traverse(s, id);
        }
    }

}
