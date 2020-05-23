package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCampaignService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminPanelCampaignController {
    final AdminPanelCampaignService adminPanelCampaignService;
    final AdminPanelCategoryService adminPanelCategoryService;

    public AdminPanelCampaignController(AdminPanelCampaignService adminPanelCampaignService, AdminPanelCategoryService adminPanelCategoryService) {
        this.adminPanelCampaignService = adminPanelCampaignService;
        this.adminPanelCategoryService = adminPanelCategoryService;
    }

    @RequestMapping(value = "/admin-panel/campaign", method = RequestMethod.GET)
    public String tab(Model model) {
        System.out.println("get method");
        Campaign campaign = new Campaign();
        model.addAttribute("campaign", campaign);
        model.addAttribute("allCampaigns", adminPanelCampaignService.listAll());
        model.addAttribute("categoryService", adminPanelCategoryService);

        return "admin_panel/_campaign";
    }

    @RequestMapping(value = "/admin-panel/campaign", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(@RequestParam String name, Campaign campaign) {
        System.out.println("post method");
      if( adminPanelCampaignService.getByName(name) != null)
      {
          return ResponseEntity.badRequest().body("This campaign name exist");
      }

        adminPanelCampaignService.save(campaign);
        return ResponseEntity.ok("");
    }
}
