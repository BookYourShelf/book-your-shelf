package com.oak.bookyourshelf.service;


import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.WishListRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class WishListService {
    final WishListRepository wishListRepository;

    public WishListService(WishListRepository wishListRepository){
        this.wishListRepository= wishListRepository;
    }

    public Product get(int id) {
        return wishListRepository.findById(id).get();
    }

    public void delete(int id) {
        wishListRepository.deleteById(id);
    }

    public void save(Product product) {
        wishListRepository.save(product);
    }

}
