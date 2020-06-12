package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.dto.HeaderCategoryDTO;
import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.service.CampaignDetailsService;
import com.oak.bookyourshelf.service.HotListDetailsService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HeaderController {
    final AdminPanelCategoryService categoryDetailsService;
    final CampaignDetailsService campaignDetailsService;
    final HotListDetailsService hotListDetailsService;

    public HeaderController(AdminPanelCategoryService categoryDetailsService, CampaignDetailsService campaignDetailsService, HotListDetailsService hotListDetailsService) {
        this.categoryDetailsService = categoryDetailsService;
        this.campaignDetailsService = campaignDetailsService;
        this.hotListDetailsService = hotListDetailsService;
    }

    @RequestMapping(value = "/header", method = RequestMethod.GET)
    public Map<String, List> header() {
        List<HeaderCategoryDTO> bookCategories = categoryDetailsService.getAllHeaderCategoryByCategory("Book");
        List<HeaderCategoryDTO> ebookCategories = categoryDetailsService.getAllHeaderCategoryByCategory("E-Book");
        List<HeaderCategoryDTO> audioBookCategories = categoryDetailsService.getAllHeaderCategoryByCategory("Audio Book");

        List<Campaign> bookCampaigns = campaignDetailsService.getAllByProductType(Category.ProductType.BOOK);
        List<Campaign> ebookCampaigns = campaignDetailsService.getAllByProductType(Category.ProductType.E_BOOK);
        List<Campaign> audiobookCampaigns = campaignDetailsService.getAllByProductType(Category.ProductType.AUDIO_BOOK);

        List<HotList> bookHotlists = hotListDetailsService.getAllByProductType(Category.ProductType.BOOK);
        List<HotList> ebookHotlists = hotListDetailsService.getAllByProductType(Category.ProductType.E_BOOK);
        List<HotList> audiobookHotlists = hotListDetailsService.getAllByProductType(Category.ProductType.AUDIO_BOOK);

        HashMap<String, List> ret = new HashMap<>();
        ret.put("bookCategories", bookCategories);
        ret.put("ebookCategories", ebookCategories);
        ret.put("audioBookCategories", audioBookCategories);

        ret.put("bookCampaigns", bookCampaigns);
        ret.put("ebookCampaigns", ebookCampaigns);
        ret.put("audiobookCampaigns", audiobookCampaigns);

        ret.put("bookHotlists", bookHotlists);
        ret.put("ebookHotlists", ebookHotlists);
        ret.put("audiobookHotlists", audiobookHotlists);

        return ret;
    }
}
