package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.service.CampaignDetailsService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CampaignDetailsController {

    final CampaignDetailsService campaignDetailsService;
    final AdminPanelCategoryService adminPanelCategoryService;

    public CampaignDetailsController(CampaignDetailsService campaignDetailsService, AdminPanelCategoryService adminPanelCategoryService) {
        this.campaignDetailsService = campaignDetailsService;
        this.adminPanelCategoryService = adminPanelCategoryService;
    }

    @RequestMapping("/campaign-details/{id}")
    public String showCampaignDetailsPage(@PathVariable int id, Model model) {
        Campaign campaign = campaignDetailsService.get(id);
        if (campaign == null) {
            return "redirect:/admin-panel";
        }
        model.addAttribute("campaign", campaign);
        model.addAttribute("categoryService", adminPanelCategoryService);
        return "campaign-details";
    }

    @RequestMapping(value = "/campaign-details/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateCampaign(@PathVariable int id, @RequestParam String button, Campaign newCampaign) {

        Campaign campaign = campaignDetailsService.get(id);
        System.out.println("update");
        switch (button) {
            case "update_campaign":
                campaign.setCategories(newCampaign.getCategories());
                campaign.setRate(newCampaign.getRate());
                campaign.setEndDate(newCampaign.getEndDate());
                campaign.setName(newCampaign.getName());
                campaign.setStartDate(newCampaign.getStartDate());
                campaign.setProductType(newCampaign.getProductType());

                campaign.setId(id);
                campaignDetailsService.save(campaign);
                return ResponseEntity.ok("");
            case "delete_hotList":
                campaignDetailsService.delete(id);
                return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body("An error occurred.");
    }
}
