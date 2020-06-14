package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Book;
import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.service.CampaignDetailsService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCampaignService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CampaignDetailsController {

    final CampaignDetailsService campaignDetailsService;
    final AdminPanelCategoryService adminPanelCategoryService;
    final AdminPanelCampaignService adminPanelCampaignService;

    public CampaignDetailsController(CampaignDetailsService campaignDetailsService, AdminPanelCategoryService adminPanelCategoryService, AdminPanelCampaignService adminPanelCampaignService) {
        this.campaignDetailsService = campaignDetailsService;
        this.adminPanelCategoryService = adminPanelCategoryService;
        this.adminPanelCampaignService = adminPanelCampaignService;
    }

    @RequestMapping("/campaign-details/{id}")
    public String showCampaignDetailsPage(@PathVariable int id, Model model) {
        Campaign campaign = campaignDetailsService.get(id);
        if (campaign == null) {
            return "redirect:/admin-panel";
        }
        model.addAttribute("campaign", campaign);
        model.addAttribute("categoryService", adminPanelCategoryService);
        model.addAttribute("productType", adminPanelCampaignService.createProductType(campaign));
        return "campaign-details";
    }

    @RequestMapping(value = "/campaign-details/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateCampaign(@PathVariable int id, @RequestParam String button, Campaign newCampaign) {

        Campaign campaign = campaignDetailsService.get(id);
        switch (button) {
            case "update_campaign":

                String[] start = newCampaign.getStartDate().split("/");
                String[] end = newCampaign.getEndDate().split("/");
                List<String> startDate = Arrays.asList(start);
                List<String> endDate = Arrays.asList(end);

                if (!adminPanelCampaignService.isDateValid(startDate))
                    return ResponseEntity.badRequest().body("Start date is not valid");
                else if (!adminPanelCampaignService.isDateValid(endDate))
                    return ResponseEntity.badRequest().body("End date is not valid");
                else {
                    if (!adminPanelCampaignService.isDateCorrect(endDate, startDate))
                        return ResponseEntity.badRequest().body("End date cannot be smaller than start date");
                }

                campaign.setRate(newCampaign.getRate());
                campaign.setEndDate(newCampaign.getEndDate());
                campaign.setName(newCampaign.getName());
                campaign.setStartDate(newCampaign.getStartDate());
                campaign.setId(id);
                campaignDetailsService.save(campaign);
                return ResponseEntity.ok("");
            case "delete_campaign":
                if (campaign.getProductType() == Category.ProductType.BOOK || campaign.getProductType() == Category.ProductType.E_BOOK || campaign.getProductType() == Category.ProductType.AUDIO_BOOK) {
                    campaign.setSubcategories(campaignDetailsService.removeDiscount(campaign.getSubcategories()));
                    campaignDetailsService.save(campaign);
                    List<Subcategory> subcategories = new ArrayList<>(campaign.getSubcategories());

                    for (Book book : campaign.getCategories().get(0).getBookSet()) {
                        book.setDiscountRate(0);
                        book.setOnDiscount(false);
                    }

                    for (Subcategory subcategory : subcategories) {
                        subcategory.setInCampaign(false);
                        for (Book b : subcategory.getBooks()) {
                            b.setDiscountRate(0);
                            b.setOnDiscount(false);
                        }
                    }

                    campaignDetailsService.delete(campaign.getId());
                } else {
                    campaignDetailsService.removeDiscountOtherProducts(campaign);
                    campaignDetailsService.deleteReaderOrCase(id);
                }
                System.out.println("xxx");
                return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body("An error occurred.");
    }
}
