package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Coupon;
import com.oak.bookyourshelf.service.CouponDetailsService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class CouponDetailsController {

    final CouponDetailsService couponDetailsService;
    final AdminPanelCouponService adminPanelCouponService;

    public CouponDetailsController(CouponDetailsService couponDetailsService, AdminPanelCouponService adminPanelCouponService) {
        this.couponDetailsService = couponDetailsService;
        this.adminPanelCouponService = adminPanelCouponService;
    }

    @RequestMapping(value = "/coupon-details/{id}", method = RequestMethod.GET)
    public String showCouponDetailsPage(@PathVariable int id, Model model) {
        Coupon coupon = couponDetailsService.get(id);
        model.addAttribute("coupon", coupon);
        return "coupon-details";
    }

    @RequestMapping(value = "/coupon-details/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> showCouponDetailsPage(@PathVariable int id) {
        Coupon coupon = couponDetailsService.get(id);
        List<Coupon> coupons = (List<Coupon>) adminPanelCouponService.listAll();
        coupons.remove(coupon);
        couponDetailsService.delete(coupon);

        return ResponseEntity.ok("");
    }
}
