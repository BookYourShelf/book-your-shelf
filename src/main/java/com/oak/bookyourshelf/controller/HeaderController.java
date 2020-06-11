package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.dto.HeaderCategoryDTO;
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

    public HeaderController(AdminPanelCategoryService categoryDetailsService) {
        this.categoryDetailsService = categoryDetailsService;
    }

    @RequestMapping(value = "/header", method = RequestMethod.GET)
    public Map<String, List<HeaderCategoryDTO>> header() {
        List<HeaderCategoryDTO> bookCategories = categoryDetailsService.getAllHeaderCategoryByCategory("Book");
        List<HeaderCategoryDTO> ebookCategories = categoryDetailsService.getAllHeaderCategoryByCategory("E-Book");
        List<HeaderCategoryDTO> audioBookCategories = categoryDetailsService.getAllHeaderCategoryByCategory("Audio Book");

        HashMap<String, List<HeaderCategoryDTO>> ret = new HashMap<>();
        ret.put("bookCategories", bookCategories);
        ret.put("ebookCategories", ebookCategories);
        ret.put("audioBookCategories", audioBookCategories);
        return ret;
    }
}
