package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.repository.CartRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CartService {
    final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void save(Order order) {
        cartRepository.save(order);
    }

    public void deleteOrder(int i) {
        cartRepository.deleteById(i);
    }

    public Order findByOrderCode(String orderCode) {
        return cartRepository.findByOrderCode(orderCode);
    }
}
