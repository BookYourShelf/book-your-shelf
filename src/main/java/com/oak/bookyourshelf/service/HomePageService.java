package com.oak.bookyourshelf.service;


import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.HomePageRepository;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HomePageService {
    final HomePageRepository homePageRepository;
    final AdminPanelProductRepository adminPanelProductRepository;

    public HomePageService(HomePageRepository homePageRepository, AdminPanelProductRepository adminPanelProductRepository) {
        this.homePageRepository = homePageRepository;
        this.adminPanelProductRepository = adminPanelProductRepository;
    }

    public List<Product> createBestSellerList(int from,int to){
        List<Product> allProducts = adminPanelProductRepository.findAllByOrderBySalesNumDesc();
        if(to > allProducts.size())
             return allProducts;
        else
            return allProducts.subList(from,to);
    }

    public List<Product> createNewProductsList(int from,int to){
        List<Product> allProducts = adminPanelProductRepository.findAllByOrderByUploadDateDesc();
        if(to > allProducts.size())
            return allProducts;
        else
            return allProducts.subList(from,to);
    }


}
