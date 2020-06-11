package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Coupon;
import com.oak.bookyourshelf.repository.CouponDetailsRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CouponDetailsService {

    final CouponDetailsRepository couponDetailsRepository;

    public CouponDetailsService(CouponDetailsRepository couponDetailsRepository) {
        this.couponDetailsRepository = couponDetailsRepository;
    }

    public void delete(Coupon coupon) {
        couponDetailsRepository.delete(coupon);
    }

    public Coupon get(int id) {
        return couponDetailsRepository.findById(id).get();
    }

    public void save(Coupon coupon) {
        couponDetailsRepository.save(coupon);
    }

    public Iterable<Coupon> listAll() {
        return couponDetailsRepository.findAll();
    }

    public Coupon getCouponByCode(String code) {
        return couponDetailsRepository.findByCouponCode(code);
    }
}
