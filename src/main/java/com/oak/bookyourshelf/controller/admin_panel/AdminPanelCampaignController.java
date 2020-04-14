package com.oak.bookyourshelf.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelCampaignController {
    @GetMapping("/admin-panel/campaign")
    public String tab() {

        return "admin_panel/_campaign";
    }
}
