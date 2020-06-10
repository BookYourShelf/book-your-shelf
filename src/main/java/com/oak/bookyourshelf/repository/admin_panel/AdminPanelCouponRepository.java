package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Coupon;
import com.oak.bookyourshelf.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelCouponRepository extends CrudRepository<Coupon, Integer> {

    List<Coupon> findAllByOrderByCouponIdAsc();

    List<Coupon> findAllByOrderByCouponIdDesc();

    List<Coupon> findAllByOrderByCouponCodeAsc();

    List<Coupon> findAllByOrderByCouponCodeDesc();

    List<Coupon> findAllByOrderByDiscountRateAsc();

    List<Coupon> findAllByOrderByDiscountRateDesc();

    List<Coupon> findAllByOrderByUploadDateAsc();

    List<Coupon> findAllByOrderByUploadDateDesc();

}
