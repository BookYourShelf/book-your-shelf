package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.Coupon;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminPanelCouponService {
    final AdminPanelCouponRepository adminPanelCouponRepository;

    public AdminPanelCouponService(AdminPanelCouponRepository adminPanelCouponRepository) {
        this.adminPanelCouponRepository = adminPanelCouponRepository;
    }

    public Iterable<Coupon> listAll() {
        return adminPanelCouponRepository.findAll();
    }

    public void save(Coupon coupon) {
        adminPanelCouponRepository.save(coupon);
    }

    public Coupon findByCouponCode(String couponCode) {
        return adminPanelCouponRepository.findByCouponCode(couponCode);
    }

    public Coupon findById(int id) {
        return adminPanelCouponRepository.findById(id).get();
    }

    public List<Coupon> sortCoupon(String sortType) {
        switch (sortType) {
            case "ID-desc":
                return adminPanelCouponRepository.findAllByOrderByCouponIdDesc();

            case "ID-asc":
                return adminPanelCouponRepository.findAllByOrderByCouponIdAsc();

            case "CouponCode-desc":
                return adminPanelCouponRepository.findAllByOrderByCouponCodeDesc();

            case "CouponCode-asc":
                return adminPanelCouponRepository.findAllByOrderByCouponCodeAsc();

            case "UploadDate-desc":
                return adminPanelCouponRepository.findAllByOrderByUploadDateDesc();

            case "UploadDate-asc":
                return adminPanelCouponRepository.findAllByOrderByUploadDateAsc();

            case "DiscountRate-desc":
                return adminPanelCouponRepository.findAllByOrderByDiscountRateDesc();

            case "DiscountRate-asc":
                return adminPanelCouponRepository.findAllByOrderByDiscountRateAsc();

            default:        // id desc
                return adminPanelCouponRepository.findAllByOrderByCouponIdDesc();
        }
    }
}
