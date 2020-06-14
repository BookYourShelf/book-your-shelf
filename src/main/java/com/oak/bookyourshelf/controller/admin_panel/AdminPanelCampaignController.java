package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
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
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort,
                      @RequestParam("filter") Optional<String> filter, Model model) {


        String currentSort = sort.orElse("ID-desc");
        String currentFilter = filter.orElse("all");
        Globals.getPageNumbers(page, size, filterCampaigns(adminPanelCampaignService.sortCampaigns(currentSort), currentFilter),
                model, "campaignPage");

        Campaign campaign = new Campaign();
        model.addAttribute("campaign", campaign);
        model.addAttribute("adminPanelCampaignService", adminPanelCampaignService);
        model.addAttribute("campaignListEmpty", ((List) adminPanelCampaignService.listAll()).isEmpty());
        model.addAttribute("categoryService", adminPanelCategoryService);
        model.addAttribute("sort", currentSort);
        model.addAttribute("filter", currentFilter);

        return "admin_panel/_campaign";
    }

    @RequestMapping(value = "/admin-panel/campaign/category", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findAllCategories(@RequestParam String category) {

        List<Category> categories = new ArrayList<>();
        List<String> result = new ArrayList<>();
        if (category.equals("BOOK")) {
            categories.addAll((Collection<? extends Category>) adminPanelCategoryService.getAllByCategory("Book"));

        } else if (category.equals("E_BOOK"))
            categories.addAll((Collection<? extends Category>) adminPanelCategoryService.getAllByCategory("E-Book"));

        else if (category.equals("AUDIO_BOOK"))
            categories.addAll((Collection<? extends Category>) adminPanelCategoryService.getAllByCategory("Audio Book"));

        for (Category c : categories)
            result.add(c.getName());
        return result;

    }

    @RequestMapping(value = "/admin-panel/campaign/subcategory", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findAllSubcategories(@RequestParam String category) {
        return Globals.getAllSubcategories(adminPanelCategoryService.getByName(category));
    }

    @RequestMapping(value = "/admin-panel/campaign", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(@RequestParam String name, String category, String subctgry, String ptype, Campaign campaign) {
        List<Campaign> sameType = adminPanelCampaignService.findAllByProductType(campaign.getProductType());
        List<Category> newCategoryList = new ArrayList<>();
        Set<Subcategory> newSubcategories = new HashSet<>();

        String[] start = campaign.getStartDate().split("/");
        String[] end = campaign.getEndDate().split("/");
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
        if(!adminPanelCampaignService.isDateInPast(startDate))
            return ResponseEntity.badRequest().body("Campaign start date can not be earlier than today");

        if (ptype.equals("BOOK") || ptype.equals("E_BOOK") || ptype.equals("AUDIO_BOOK")) {
            Category newCategory = adminPanelCategoryService.getByName(category);

            newCategoryList.add(newCategory);
            if (subctgry.equals("")) {
                for (Campaign i : sameType) {
                    Category campaignCategory = i.getCategories().stream().findFirst().get();
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
                    if (newSubcategory != null) {
                        newSubcategories.add(newSubcategory);
                    }
                }


                for (Campaign i : sameType) {
                    Category campaignCategory = i.getCategories().stream().findFirst().get();
                    if (campaignCategory.getName().equals(category)) {
                        for (Subcategory s : newSubcategories) {
                            if (s.isInCampaign())
                                return ResponseEntity.badRequest().body("There is a campaign in " + s.getName() + " subcategory . Please change your selection");
                        }
                    }
                }

                campaign.setSubcategories(newSubcategories);
                campaign.setCategories(newCategoryList);
                Set<Book> products = adminPanelCampaignService.createProductSet(newSubcategories);
                products.addAll(newCategory.getBookSet());
                adminPanelCampaignService.setProductsRate(products, campaign.getRate());
            }

        } else {
            if (sameType.size() > 0)
                return ResponseEntity.badRequest().body("There is a campaign in " + ptype + " product type . Please change your selection");

            else {
                campaign.setSubcategories(newSubcategories);
                campaign.setCategories(newCategoryList);

                adminPanelCampaignService.setOtherProductsRate(ptype, campaign.getRate());
            }
        }

        adminPanelCampaignService.save(campaign);


        return ResponseEntity.ok("");


    }

    public List<Campaign> filterCampaigns(List<Campaign> campaigns, String productType) {
        switch (productType) {
            case "book":
                return campaigns.stream().filter(p -> p.getProductType() == Category.ProductType.BOOK).collect(Collectors.toList());
            case "e-book":
                return campaigns.stream().filter(p -> p.getProductType() == Category.ProductType.E_BOOK).collect(Collectors.toList());
            case "audio-book":
                return campaigns.stream().filter(p -> p.getProductType() == Category.ProductType.AUDIO_BOOK).collect(Collectors.toList());
            case "e-book-reader":
                return campaigns.stream().filter(p -> p.getProductType() == Category.ProductType.E_BOOK_READER).collect(Collectors.toList());
            case "e-book-reader-case":
                return campaigns.stream().filter(p -> p.getProductType() == Category.ProductType.E_BOOK_READER_CASE).collect(Collectors.toList());
            case "book-case":
                return campaigns.stream().filter(p -> p.getProductType() == Category.ProductType.BOOK_CASE).collect(Collectors.toList());
            default:
                return campaigns;
        }
    }
}
