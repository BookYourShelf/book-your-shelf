package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Payment;
import com.oak.bookyourshelf.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {

    final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Iterable<Payment> listAll() {
        return paymentRepository.findAll();
    }

    public void save(Payment product) {
        paymentRepository.save(product);
    }

    public Payment get(int id) {
        return paymentRepository.findById(id).get();
    }

    public void delete(int id) {
        paymentRepository.deleteById(id);
    }

    public Payment findByOrderId(int orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
