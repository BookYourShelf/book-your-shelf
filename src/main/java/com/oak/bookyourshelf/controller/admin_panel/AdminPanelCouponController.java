package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Coupon;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminPanelCouponController {

    final AdminPanelCouponService adminPanelCouponService;

    public AdminPanelCouponController(AdminPanelCouponService adminPanelCouponService) {
        this.adminPanelCouponService = adminPanelCouponService;
    }

    @RequestMapping(value = "/admin-panel/coupon", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort, Model model) {

        String currentSort = sort.orElse("ID-asc");
        Globals.getPageNumbers(page, size, adminPanelCouponService.sortCoupon(currentSort), model, "couponPage");
        model.addAttribute("sort", currentSort);
        model.addAttribute("couponListEmpty", ((List) adminPanelCouponService.listAll()).isEmpty());
        return "admin_panel/_coupon";
    }

    @RequestMapping(value = "/admin-panel/coupon", method = RequestMethod.POST)
    public ResponseEntity<String> showCoupon(Coupon coupon) {
        Coupon coupons = new Coupon();
        Coupon coup = adminPanelCouponService.findByCouponCode(coupon.getCouponCode());
        if (coup == null) {
            coupons.setCouponCode(coupon.getCouponCode());
            coupons.setDiscountRate(coupon.getDiscountRate() / 100);
            coupons.setUploadDate(new Timestamp(System.currentTimeMillis()));
            adminPanelCouponService.save(coupons);
            return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body("Coupon code already exists. Please enter another code.");
    }
}
