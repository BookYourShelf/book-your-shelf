package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.dto.HeaderCategoryDTO;
import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CampaignDetailsService;
import com.oak.bookyourshelf.service.HotListDetailsService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
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
    final ProfileInformationService profileInformationService;
    final AuthService authService;

    public HeaderController(AdminPanelCategoryService categoryDetailsService, CampaignDetailsService campaignDetailsService,
                            HotListDetailsService hotListDetailsService, ProfileInformationService profileInformationService,
                            @Qualifier("customUserDetailsService") AuthService authService) {
        this.categoryDetailsService = categoryDetailsService;
        this.campaignDetailsService = campaignDetailsService;
        this.hotListDetailsService = hotListDetailsService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
    }

    @RequestMapping(value = "/header", method = RequestMethod.GET)
    public Map<String, List> header(Model model) {
        List<HeaderCategoryDTO> bookCategories = categoryDetailsService.getAllHeaderCategoryByCategory("Book");
        List<HeaderCategoryDTO> ebookCategories = categoryDetailsService.getAllHeaderCategoryByCategory("E-Book");
        List<HeaderCategoryDTO> audioBookCategories = categoryDetailsService.getAllHeaderCategoryByCategory("Audio Book");

        List<Campaign> bookCampaigns = campaignDetailsService.getAllByProductType(Category.ProductType.BOOK);
        List<Campaign> ebookCampaigns = campaignDetailsService.getAllByProductType(Category.ProductType.E_BOOK);
        List<Campaign> audiobookCampaigns = campaignDetailsService.getAllByProductType(Category.ProductType.AUDIO_BOOK);
        List<Campaign> ebookReaderCampaigns = campaignDetailsService.getAllByProductType(Category.ProductType.E_BOOK_READER);
        List<Campaign> ebookReaderCaseCampaigns = campaignDetailsService.getAllByProductType(Category.ProductType.E_BOOK_READER_CASE);
        List<Campaign> bookCaseCampaigns = campaignDetailsService.getAllByProductType(Category.ProductType.BOOK_CASE);

        List<HotList> bookHotlists = hotListDetailsService.getAllByProductType(Category.ProductType.BOOK);
        List<HotList> ebookHotlists = hotListDetailsService.getAllByProductType(Category.ProductType.E_BOOK);
        List<HotList> audiobookHotlists = hotListDetailsService.getAllByProductType(Category.ProductType.AUDIO_BOOK);

        UserDetails userDetails = authService.getUserDetails();

        if (userDetails != null) {
            User user = profileInformationService.getByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        HashMap<String, List> ret = new HashMap<>();
        ret.put("bookCategories", bookCategories);
        ret.put("ebookCategories", ebookCategories);
        ret.put("audioBookCategories", audioBookCategories);

        ret.put("bookCampaigns", bookCampaigns);
        ret.put("ebookCampaigns", ebookCampaigns);
        ret.put("audiobookCampaigns", audiobookCampaigns);
        ret.put("ebookReaderCampaigns", ebookReaderCampaigns);
        ret.put("ebookReaderCaseCampaigns", ebookReaderCaseCampaigns);
        ret.put("bookCaseCampaigns", bookCaseCampaigns);

        ret.put("bookHotlists", bookHotlists);
        ret.put("ebookHotlists", ebookHotlists);
        ret.put("audiobookHotlists", audiobookHotlists);

        return ret;
    }
}
