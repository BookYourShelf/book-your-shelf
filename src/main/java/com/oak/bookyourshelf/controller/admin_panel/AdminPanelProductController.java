package com.oak.bookyourshelf.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelProductController {
    @GetMapping("/admin-panel/product")
    public String product(Model model) {

        return "admin_panel/_product";
    }
}
