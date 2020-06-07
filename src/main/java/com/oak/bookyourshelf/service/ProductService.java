package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.repository.ProductRepository;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    final ProductRepository productRepository;
    final AdminPanelProductRepository adminPanelProductRepository;

    public ProductService(ProductRepository productRepository, AdminPanelProductRepository adminPanelProductRepository) {
        this.productRepository = productRepository;
        this.adminPanelProductRepository = adminPanelProductRepository;
    }

    public Product get(int id) {
        return productRepository.findById(id).get();
    }

    public Iterable<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> filterProducts(List<Product> products, String productType) {
        switch (productType) {
            case "Book":
                return products.stream().filter(p -> p instanceof PhysicalBook).collect(Collectors.toList());
            case "E-Book":
                return products.stream().filter(p -> p instanceof ElectronicBook).collect(Collectors.toList());
            case "Audio Book":
                return products.stream().filter(p -> p instanceof AudioBook).collect(Collectors.toList());
            case "E-Book Reader":
                return products.stream().filter(p -> p instanceof ElectronicBookReader).collect(Collectors.toList());
            case "E-Book Reader Case":
                return products.stream().filter(p -> p instanceof ElectronicBookReaderCase).collect(Collectors.toList());
            case "Book Case":
                return products.stream().filter(p -> p instanceof PhysicalBookCase).collect(Collectors.toList());
            default:
                return products;
        }
    }

    public List<Book> findProductsSameCategory(List<Category> categories, List<Book> books) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getCategory().get(0).getName().equals(categories.get(0).getName())) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> findProductsSameSubcategory(List<Subcategory> subcategories, List<Book> books) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getSubcategory().get(0).getName().equals(subcategories.get(0).getName())) {
                result.add(b);
            }
        }
        return result;

    }

    public List<? extends Product> createOurPicsForYou(Product product) {
        List<Product> allProducts = adminPanelProductRepository.findAllByOrderBySalesNumDesc();
        List<Product> sameType = filterProducts(allProducts, (String) product.getProductTypeName());
        System.out.println(product.getProductTypeName());
        sameType.remove(product);
        if (sameType.size() == 0) {
            return null;
        } else {
            if (product.getProductTypeName().equals("Book") || product.getProductTypeName().equals("E-Book") || product.getProductTypeName().equals("Audio Book")) {
                Book b = (Book) product;
                List<Book> books = new ArrayList<>();
                for (Product p : sameType) {
                    books.add((Book) p);
                }
                List<Book> sameCategory = findProductsSameCategory(b.getCategory(), books);
                if (sameCategory.size() < 4)
                    return sameType.subList(0, 4);

                else {
                    List<Book> sameSubcategory = findProductsSameSubcategory(b.getSubcategory(), books);
                    if (sameSubcategory.size() < 4)
                        return sameCategory.subList(0, 4);
                    else
                        return sameSubcategory.subList(0, 4);

                }
            } else {
                if (sameType.size() < 4)
                    return allProducts.subList(0, 4);
                else
                    return sameType.subList(0, 4);
            }
        }
    }
}
