package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Subcategory;
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

    @RequestMapping(value = "/admin-panel/campaign/category", method = RequestMethod.GET)
    @ResponseBody
    public List<Category> findAllCategories(@RequestParam String category) {

        if (category.equals("BOOK")) {
            System.out.println(adminPanelCategoryService.getAllByCategory("Book"));
            return (List<Category>) adminPanelCategoryService.getAllByCategory("Book");
        } else if (category.equals("E_BOOK"))
            return (List<Category>) adminPanelCategoryService.getAllByCategory("E-Book");
        else if (category.equals("AUDIO_BOOK"))
            return (List<Category>) adminPanelCategoryService.getAllByCategory("Audio Book");
        else return null;

    }

    @RequestMapping(value = "/admin-panel/campaign/subcategory", method = RequestMethod.GET)
    @ResponseBody
    public List<Subcategory> findAllSubcategories(@RequestParam String category) {
        return Globals.getAllChildSubcategories(adminPanelCategoryService.getByName(category));
    }

    @RequestMapping(value = "/admin-panel/campaign", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(@RequestParam String name, String category, String subctgry, String ptype, Campaign campaign) {
        List<Campaign> sameType = adminPanelCampaignService.findAllByProductType(campaign.getProductType());
        List<Category> newCategoryList = new ArrayList<>();
        List<Subcategory> newSubcategories = new ArrayList<>();
        if (ptype.equals("BOOK") || ptype.equals("E_BOOK") || ptype.equals("AUDIO_BOOK")) {
            Category newCategory = adminPanelCategoryService.getByName(category);

            newCategoryList.add(newCategory);
            if (subctgry.equals("")) {
                for (Campaign i : sameType) {
                    Category campaignCategory = i.getCategories().get(0);
                    if (campaignCategory.getName().equals(category)) {

                        return ResponseEntity.badRequest().body("There is a campaign in " + category + " category . Please change your selection");

                    }
                }
                campaign.setCategories(newCategoryList);
            } else {
                String[] subcategory = subctgry.split("-");
                List<String> subcategories = Arrays.asList(subcategory);
                for (String s : subcategories) {
                    Subcategory newSubcategory = adminPanelCategoryService.getSubcategory(newCategory, s);
                    if (newCategory != null) {
                        newSubcategories.add(newSubcategory);
                    }
                }


                for (Campaign i : sameType) {
                    Category campaignCategory = i.getCategories().get(0);
                    if (campaignCategory.getName().equals(category)) {
                        for (Subcategory s : newSubcategories) {
                            if (s.isInCampaign())
                                return ResponseEntity.badRequest().body("There is a campaign in " + s.getName() + " subcategory . Please change your selection");
                        }
                    }
                }
                /*adminPanelCampaignService.setProductsRate(adminPanelCampaignService.createProductSet(newSubcategories), campaign.getRate());*/

                campaign.setSubcategories(newSubcategories);
                campaign.setCategories(newCategoryList);
            }

        } else {
            if (sameType.size() > 0)
                return ResponseEntity.badRequest().body("There is a campaign in " + ptype + " product type . Please change your selection");

            else {
                campaign.setSubcategories(newSubcategories);
                campaign.setCategories(newCategoryList);
            }
        }

        adminPanelCampaignService.save(campaign);


        return ResponseEntity.ok("");


    }
}
