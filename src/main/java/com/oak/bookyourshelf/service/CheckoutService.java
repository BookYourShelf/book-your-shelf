package com.oak.bookyourshelf.service;


import com.oak.bookyourshelf.repository.CheckoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CheckoutService {

    final CheckoutRepository checkoutRepository;

    public CheckoutService(CheckoutRepository checkoutRepository){ this.checkoutRepository = checkoutRepository; }

}
