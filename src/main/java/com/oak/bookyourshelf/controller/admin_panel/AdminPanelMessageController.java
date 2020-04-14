package com.oak.bookyourshelf.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelMessageController {
    @GetMapping("/admin-panel/message")
    public String tab() {

        return "admin_panel/_message";
    }
}
