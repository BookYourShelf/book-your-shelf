package com.oak.bookyourshelf.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelCouponController {
    @GetMapping("/admin-panel/coupon")
    public String tab() {

        return "admin_panel/_coupon";
    }
}
