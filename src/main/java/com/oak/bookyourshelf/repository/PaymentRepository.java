package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    Payment findByOrderId(int orderId);
}
