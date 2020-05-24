package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.service.CampaignDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CampaignDetailsController {

    final CampaignDetailsService campaignDetailsService;

    public CampaignDetailsController(CampaignDetailsService campaignDetailsService) {
        this.campaignDetailsService = campaignDetailsService;
    }

    @RequestMapping("/campaign-details/{id}")
    public String showCampaignDetailsPage(@PathVariable int id, Model model) {
        Campaign campaign = campaignDetailsService.get(id);
        if (campaign == null) {
            return "redirect:/admin-panel";
        }
        model.addAttribute("campaign", campaign);

        return "campaign-details";
    }
}
