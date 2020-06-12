package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Coupon;
import org.springframework.data.repository.CrudRepository;

public interface CouponDetailsRepository extends CrudRepository<Coupon, Integer> {
    Coupon findByCouponCode(String code);
}
