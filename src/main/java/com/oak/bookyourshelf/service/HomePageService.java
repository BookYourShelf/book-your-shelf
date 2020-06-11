package com.oak.bookyourshelf.service;


import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.HomePageRepository;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    public List<Product> createOurPicsForYouList(User user)
    {
        List<Product> allProducts  = adminPanelProductRepository.findAllByOrderBySalesNumDesc();
        List<Product> searchProducts = new ArrayList<>();
        Set<Product> result = new java.util.HashSet<>(Collections.emptySet());

        if(searchProducts.size() == 0)
        {
            result = (Set<Product>) getWishListProducts(user, 12);

            if(result.size()<12)
            {
                if(user.getProductsPurchased().size()>0)
                {
                    for(Product p : user.getProductsPurchased())
                    {
                        if(result.size()<12)
                        {
                            result.addAll(productService.createOurPicsForYou(p));
                        }
                    }

                    if(result.size()>=12) {
                        List<Product> finalList = (List<Product>) result;
                        return finalList.subList(0,12);
                    }
                }
                if (user.getWishList().size()>0)
                {
                    for(Product p : user.getWishList())
                    {
                        if(result.size()<12)
                        {
                            result.addAll(productService.createOurPicsForYou(p));
                        }
                    }
                    if(result.size()>=12) {
                        List<Product> finalList = (List<Product>) result;
                        return finalList.subList(0,12);
                    }
                }
                int i = 0;
                while (result.size()<12)
                {
                    result.add(allProducts.get(i));
                    ++i;
                }
                List<Product> finalList = (List<Product>) result;
                return finalList;

            }
            else
            {
                List<Product> finalList = (List<Product>) result;
                return finalList.subList(0,12);
            }
        }
        else if(searchProducts.size()< 12)
        {

                searchProducts.subList(0,searchProducts.size());
                int need = 12-searchProducts.size();
                result.addAll(searchProducts);
                result.addAll(getWishListProducts(user,need));
                if(result.size()<12)
                {
                    if(user.getProductsPurchased().size()>0)
                    {
                        for(Product p : user.getProductsPurchased())
                        {
                            if(result.size()<12)
                            {
                                result.addAll(productService.createOurPicsForYou(p));
                            }
                        }
                        if(result.size()>=12) {
                            List<Product> finalList = (List<Product>) result;
                            return finalList.subList(0,12);
                        }


                    }
                    for(Product p : searchProducts) {
                        if (result.size() < 12) {
                            result.addAll(productService.createOurPicsForYou(p));
                        }
                    }
                    if(result.size()>=12)
                    {
                        List<Product> finalList = (List<Product>) result;
                        return finalList.subList(0,12);
                    }

                    if (user.getWishList().size()>0)
                    {
                        for(Product p : user.getWishList())
                        {
                            if(result.size()<12)
                            {
                                result.addAll(productService.createOurPicsForYou(p));
                            }
                        }

                        if(result.size()>=12) {
                            List<Product> finalList = (List<Product>) result;
                            return finalList.subList(0,12);
                        }
                    }
                    int i = 0;
                    while (result.size()<12)
                    {
                        result.add(allProducts.get(i));
                        ++i;
                    }
                    List<Product> finalList = (List<Product>) result;
                    return finalList;


                }
                else
                {
                    List<Product> finalList = (List<Product>) result;
                    return finalList.subList(0,12);
                }

        }
        else
            return searchProducts.subList(0,12);


    }

    public List<Product> getWishListProducts(User user , int need)
    {
        List<Product> wishListProducts = (List<Product>) user.getWishList();
        if(wishListProducts.size()>need){

            return wishListProducts;
        }
        else return wishListProducts;
    }


}
