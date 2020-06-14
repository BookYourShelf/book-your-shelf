package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.repository.ProductRepository;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    public List<Book> findProductsSameCategory(Category categories, List<Book> books) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getCategory().getName().equals(categories.getName())) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> findProductsSameSubcategory(Subcategory subcategories, List<Book> books) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getSubcategory().getName().equals(subcategories.getName())) {
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
            allProducts.remove(product);
            if(allProducts.size()>4)
                return allProducts.subList(0,4);
            else return allProducts;
        } else {
            if (product.getProductTypeName().equals("Book") || product.getProductTypeName().equals("E-Book") || product.getProductTypeName().equals("Audio Book")) {
                Book b = (Book) product;
                List<Book> books = new ArrayList<>();
                for (Product p : sameType) {
                    books.add((Book) p);
                }
                List<Book> sameCategory = findProductsSameCategory(b.getCategory(), books);
                if (sameCategory.size() < 4)
                {
                    if(sameType.size()>4)
                        return sameType.subList(0,4);
                    else return sameType;
                }

                else {
                    List<Book> sameSubcategory = findProductsSameSubcategory(b.getSubcategory(), books);
                    if (sameSubcategory.size() < 4)
                    {
                        if(sameCategory.size()>4)
                            return sameCategory.subList(0,4);
                        else return sameCategory;
                    }
                    else
                        return sameSubcategory.subList(0, 4);

                }
            } else {
                if (sameType.size() < 4)
                {
                    allProducts.remove(product);
                    if(allProducts.size()>4)
                        return allProducts.subList(0,4);
                    else return allProducts;
                }

                else
                    return sameType.subList(0, 4);
            }
        }
    }
}
