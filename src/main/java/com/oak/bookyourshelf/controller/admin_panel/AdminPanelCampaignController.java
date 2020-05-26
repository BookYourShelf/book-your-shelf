package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCampaignService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        System.out.println("get method");

        Page<Campaign> campaignPage = adminPanelCampaignService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        Campaign campaign = new Campaign();
        model.addAttribute("campaign", campaign);
        model.addAttribute("campaignPage", campaignPage);
        model.addAttribute("allCampaigns", adminPanelCampaignService.listAll());
        model.addAttribute("categoryService", adminPanelCategoryService);
        model.addAttribute("currentPage", currentPage);
        int totalPages = campaignPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }


        return "admin_panel/_campaign";
    }

    @RequestMapping(value = "/admin-panel/campaign", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(@RequestParam String name , String ctg, Campaign campaign) {
        System.out.println("post method");
        System.out.println(ctg);
        String[] category = ctg.split("-");
        System.out.println(category[0]);
        System.out.println(category[1]);
        System.out.println(category.length);
      if( adminPanelCampaignService.getByName(name) != null)
      {
          return ResponseEntity.badRequest().body("This campaign name exist");
      }

        adminPanelCampaignService.save(campaign);
        return ResponseEntity.ok("");
    }
}
