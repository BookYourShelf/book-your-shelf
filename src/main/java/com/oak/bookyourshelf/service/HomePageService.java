package com.oak.bookyourshelf.service;


import com.oak.bookyourshelf.model.Book;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.HomePageRepository;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class HomePageService {
    final HomePageRepository homePageRepository;
    final AdminPanelProductRepository adminPanelProductRepository;
    final ProductService productService;

    public HomePageService(HomePageRepository homePageRepository, AdminPanelProductRepository adminPanelProductRepository, ProductService productService) {
        this.homePageRepository = homePageRepository;
        this.adminPanelProductRepository = adminPanelProductRepository;
        this.productService = productService;
    }

    public List<Product> createBestSellerList(int from, int to) {
        List<Product> allProducts = adminPanelProductRepository.findAllByOrderBySalesNumDesc();
        if (to > allProducts.size())
            return allProducts;
        else
            return allProducts.subList(from, to);
    }

    public List<Product> createNewProductsList(int from, int to) {
        List<Product> allProducts = adminPanelProductRepository.findAllByOrderByUploadDateDesc();
        if (to > allProducts.size())
            return allProducts;
        else
            return allProducts.subList(from, to);
    }

    public List<? extends Product> createOurPicsForYouList(User user) {
        List<Product> allProducts = adminPanelProductRepository.findAllByOrderBySalesNumDesc();
        Set<Book> searchProducts = new HashSet<>();
        searchProducts = getSearchResultProducts(user, allProducts);
        Set<Product> result = new java.util.HashSet<>(Collections.emptySet());
        result.addAll(searchProducts);

        if (result.size() < 12) {

            if (user.getProductsPurchased().size() > 0) {
                for (Product p : user.getProductsPurchased()) {
                    if (result.size() < 12) {
                        result.addAll(productService.createOurPicsForYou(p));
                    }
                }

                if (result.size() >= 12) {
                    List<Product> finalList = new ArrayList<>();
                    finalList.addAll(result);
                    return finalList.subList(0, 12);
                }
            }

            if (user.getWishList().size() > 0) {
                for (Product p : user.getWishList()) {
                    if (result.size() < 12) {
                        result.addAll(productService.createOurPicsForYou(p));
                    }
                }
                if (result.size() >= 12) {
                    List<Product> finalList = new ArrayList<>();
                    finalList.addAll(result);
                    return finalList.subList(0, 12);
                }
            }
            if (searchProducts.size() > 0) {

                for (Product p : searchProducts) {
                    if (result.size() < 12) {
                        result.addAll(productService.createOurPicsForYou(p));
                    }
                }
                if (result.size() >= 12) {
                    List<Product> finalList = new ArrayList<>();
                    finalList.addAll(result);
                    return finalList.subList(0, 12);
                }
            }

            if (allProducts.size() > result.size()) {
                int i = 0;
                while (result.size() < 12 && i < allProducts.size()) {
                    result.add(allProducts.get(i));
                    ++i;
                }
            }
            List<Product> finalList = new ArrayList<>();
            finalList.addAll(result);
            return finalList;


        } else {
            List<Product> finalList = new ArrayList<>();
            finalList.addAll(result);
            return finalList.subList(0, 12);
        }


    }


    public Set<Book> getSearchResultProducts(User user, List<Product> products) {
        List<Book> books = new ArrayList<>();
        List<Book> searchName = new ArrayList<>();
        for (Product product : products) {
            if (product.getProductTypeName().equals("Book") ||
                    product.getProductTypeName().equals("E-Book") ||
                    product.getProductTypeName().equals("Audio Book")) {
                Book book = (Book) product;
                books.add(book);
            }
        }

        Set<Book> searchResults = new HashSet<>();

        for (String searchValue : user.getSearchHistory().keySet()) {
            searchName = books.stream().filter(book -> book.getProductName().toLowerCase().contains(searchValue)).collect(Collectors.toList());
            searchResults.addAll(searchName);
        }
        return searchResults;
    }


}
