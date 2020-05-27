package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCampaignService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AdminPanelCampaignController {
    final AdminPanelCampaignService adminPanelCampaignService;
    final AdminPanelCategoryService adminPanelCategoryService;

    public AdminPanelCampaignController(AdminPanelCampaignService adminPanelCampaignService, AdminPanelCategoryService adminPanelCategoryService) {
        this.adminPanelCampaignService = adminPanelCampaignService;
        this.adminPanelCategoryService = adminPanelCategoryService;
    }

    @RequestMapping(value = "/admin-panel/campaign", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size, Model model) {

        Globals.getPageNumbers(page, size, (List) adminPanelCampaignService.listAll(), model, "campaignPage");

        Campaign campaign = new Campaign();
        model.addAttribute("campaign", campaign);
        model.addAttribute("allCampaigns", adminPanelCampaignService.listAll());
        model.addAttribute("categoryService", adminPanelCategoryService);

        return "admin_panel/_campaign";
    }

    @RequestMapping(value = "/admin-panel/campaign", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(@RequestParam String name, String ctg, Campaign campaign) {
        System.out.println("post method");
        System.out.println(ctg);
        String[] category = ctg.split("-");
        List<String> categories = Arrays.asList(category);
        Map<String, Boolean> campaignCheck = new HashMap<String, Boolean>();
        for (String m : categories) {
            campaignCheck.put(m, true);
        }

        List<Campaign> sameType = adminPanelCampaignService.findAllByProductType(campaign.getProductType());
        for (Campaign i : sameType) {
            for (String s : categories) {
                if (i.getCategories().contains(adminPanelCategoryService.getByName(s))) {
                    campaignCheck.replace(s, false);
                }
            }
        }
        int categoryNum = 0;
        List<Category> newCategories = new ArrayList<Category>();
        for (String s : campaignCheck.keySet()) {
            if (campaignCheck.get(s)) {
                Category c = adminPanelCategoryService.getByName(s);
                newCategories.add(c);
                categoryNum++;
            }
        }
        if (categoryNum == 0)
            return ResponseEntity.badRequest().body("There is a campaign in your all select category of selected product type");
        else {
            campaign.setCategories(newCategories);
            adminPanelCampaignService.save(campaign);
        }

        return ResponseEntity.ok("");
    }
}
