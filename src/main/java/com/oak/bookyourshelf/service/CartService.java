package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.CartRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CartService {
    final CartRepository cartRepository;

    public CartService(CartRepository cartRepository){ this.cartRepository= cartRepository; }



}
