package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Coupon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelCouponRepository extends CrudRepository<Coupon, Integer> {

    @Query("SELECT t FROM Coupon t WHERE t.couponCode = ?1")
    Coupon findByCouponCode(String couponCode);

    List<Coupon> findAllByOrderByCouponIdAsc();

    List<Coupon> findAllByOrderByCouponIdDesc();

    List<Coupon> findAllByOrderByCouponCodeAsc();

    List<Coupon> findAllByOrderByCouponCodeDesc();

    List<Coupon> findAllByOrderByDiscountRateAsc();

    List<Coupon> findAllByOrderByDiscountRateDesc();

    List<Coupon> findAllByOrderByUploadDateAsc();

    List<Coupon> findAllByOrderByUploadDateDesc();
}
