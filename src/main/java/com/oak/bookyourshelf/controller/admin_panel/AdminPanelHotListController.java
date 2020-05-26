package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelHotListService;
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
public class AdminPanelHotListController {
    final AdminPanelCategoryService adminPanelCategoryService;
    final AdminPanelHotListService adminPanelHotListService;

    public AdminPanelHotListController(AdminPanelCategoryService adminPanelCategoryService, AdminPanelHotListService adminPanelHotListService) {
        this.adminPanelCategoryService = adminPanelCategoryService;
        this.adminPanelHotListService = adminPanelHotListService;
    }

    @RequestMapping(value = "/admin-panel/hotList", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size, Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        System.out.println("get method");

        Page<HotList> hotListPage = adminPanelHotListService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        HotList hotList = new HotList();
        model.addAttribute("hotList", hotList);
        model.addAttribute("hotListPage", hotListPage);
        model.addAttribute("categoryService", adminPanelCategoryService);
        model.addAttribute("currentPage", currentPage);
        int totalPages = hotListPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }


        return "admin_panel/_hotList";
    }

    @RequestMapping(value = "/admin-panel/hotList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(@RequestParam String ctg, HotList hotList) {
        System.out.println("post method");
        System.out.println(ctg);
        adminPanelHotListService.save(hotList);
        return ResponseEntity.ok("");
    }
}
