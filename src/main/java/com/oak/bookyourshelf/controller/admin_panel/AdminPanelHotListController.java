package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelCategoryService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelHotListService;
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
public class AdminPanelHotListController {
    final AdminPanelCategoryService adminPanelCategoryService;
    final AdminPanelHotListService adminPanelHotListService;

    public AdminPanelHotListController(AdminPanelCategoryService adminPanelCategoryService, AdminPanelHotListService adminPanelHotListService) {
        this.adminPanelCategoryService = adminPanelCategoryService;
        this.adminPanelHotListService = adminPanelHotListService;
    }

    @RequestMapping(value = "/admin-panel/hotList", method = RequestMethod.GET)
    public String tab(@RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort,
                      @RequestParam("filter") Optional<String> filter, Model model) {


        String currentSort = sort.orElse("ID-desc");
        String currentFilter = filter.orElse("all");
        Globals.getPageNumbers(page, size, filterHotLists(adminPanelHotListService.sortHotlists(currentSort), currentFilter),
                model, "hotListPage");

        HotList hotList = new HotList();
        model.addAttribute("hotList", hotList);
        model.addAttribute("adminPanelHotListService", adminPanelHotListService);
        model.addAttribute("categoryService", adminPanelCategoryService);
        model.addAttribute("sort", currentSort);
        model.addAttribute("filter", currentFilter);
        model.addAttribute("hotListListEmpty", ((List) adminPanelHotListService.listAll()).isEmpty());


        return "admin_panel/_hotList";
    }


    @RequestMapping(value = "/admin-panel/hotList/category", method = RequestMethod.GET)
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

    @RequestMapping(value = "/admin-panel/hotList/subcategory", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findAllSubcategories(@RequestParam String category) {
        return Globals.getAllSubcategories(adminPanelCategoryService.getByName(category));
    }


    @RequestMapping(value = "/admin-panel/hotList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveCategory(String category, String subctgry, String ptype, String htype, HotList hotList) {
        System.out.println("post method");
        List<HotList> sameType = adminPanelHotListService.findAllByProductType(hotList.getProductType());
        List<Category> newCategoryList = new ArrayList<>();
        Set<Subcategory> newSubcategories = new HashSet<>();

        String[] start = hotList.getStartDate().split("/");
        String[] end = hotList.getEndDate().split("/");
        String[] start_time = hotList.getStartTime().split(":");
        String[] end_time = hotList.getEndTime().split(":");

        List<String> startTime = Arrays.asList(start_time);
        List<String> endTime = Arrays.asList(end_time);
        List<String> startDate = Arrays.asList(start);
        List<String> endDate = Arrays.asList(end);

        if (!adminPanelHotListService.isDateValid(startDate))
            return ResponseEntity.badRequest().body("Start date is not valid");
        if (!adminPanelHotListService.isDateValid(endDate))
            return ResponseEntity.badRequest().body("End date is not valid");
        if (adminPanelHotListService.isDateValid(startDate) && adminPanelHotListService.isDateValid(endDate)) {
            if (!adminPanelHotListService.isDateCorrect(endDate, startDate))
                return ResponseEntity.badRequest().body("End date cannot be smaller than start date");
            else {
                if (!adminPanelHotListService.isTimeValid(startTime))
                    return ResponseEntity.badRequest().body("Start time is not valid");
                if (!adminPanelHotListService.isTimeValid(endTime))
                    return ResponseEntity.badRequest().body("End time is not valid");
                if (startDate.get(0).equals(endDate.get(0)) && startDate.get(1).equals(endDate.get(1)) && startDate.get(2).equals(endDate.get(2))) {
                    if (!adminPanelHotListService.isTimeCorrect(startTime, endTime))
                        return ResponseEntity.badRequest().body("If dates are the same , end time can not be smaller than start time");
                }
            }
        }
        if(!adminPanelHotListService.isDateInPast(startDate))
            return ResponseEntity.badRequest().body("Hot list start date can not be earlier than today");


        if (ptype.equals("BOOK") || ptype.equals("E_BOOK") || ptype.equals("AUDIO_BOOK")) {
            Category newCategory = adminPanelCategoryService.getByName(category);

            newCategoryList.add(newCategory);
            if (subctgry.equals("")) {
                for (HotList i : sameType) {
                    if (i.getCategories().size() != 0) {
                        Category hotListCategory = i.getCategories().get(0);
                        if (hotListCategory.getName().equals(category)) {

                            return ResponseEntity.badRequest().body("There is a hot list in " + category + " category . Please change your selection");

                        }
                    } else {
                        return ResponseEntity.badRequest().body("There is a hot list in " + Globals.productTypeNames.get(i.getProductType().toString()) + " . Please change your selection");
                    }
                }
                hotList.setCategories(newCategoryList);
            } else {
                String[] subcategory = subctgry.split("-");
                List<String> subcategories = Arrays.asList(subcategory);

                for (String s : subcategories) {
                    Subcategory newSubcategory = adminPanelCategoryService.getSubcategory(newCategory, s);
                    if (newSubcategory != null) {
                        newSubcategories.add(newSubcategory);
                    }
                }

                for (HotList i : sameType) {
                    Category hotListCategory = i.getCategories().get(0);
                    if (hotListCategory.getName().equals(category)) {
                        for (Subcategory s : newSubcategories) {
                            if (s.isInHotList())
                                return ResponseEntity.badRequest().body("There is a hot list in " + s.getName() + " subcategory . Please change your selection");
                        }
                    }
                }

                hotList.setSubcategories(newSubcategories);
                hotList.setCategories(newCategoryList);
                Set<Book> book = adminPanelHotListService.createProductSet(newSubcategories);
                book.addAll(newCategory.getBookSet());
                adminPanelHotListService.setProductByType(hotList, book);
            }
        } else {
            if (sameType.size() > 0)
                return ResponseEntity.badRequest().body("There is a hot list in " + ptype + " product type . Please change your selection");

            else {
                hotList.setSubcategories(newSubcategories);
                hotList.setCategories(newCategoryList);
                adminPanelHotListService.setProductByTypeOtherTypes(hotList);
            }
        }
        adminPanelHotListService.setProductByType(hotList, adminPanelHotListService.createProductSet(newSubcategories));
        adminPanelHotListService.save(hotList);


        return ResponseEntity.ok("");

    }


    public List<HotList> filterHotLists(List<HotList> hotLists, String productType) {
        switch (productType) {
            case "book":
                return hotLists.stream().filter(p -> p.getProductType() == Category.ProductType.BOOK).collect(Collectors.toList());
            case "e-book":
                return hotLists.stream().filter(p -> p.getProductType() == Category.ProductType.E_BOOK).collect(Collectors.toList());
            case "audio-book":
                return hotLists.stream().filter(p -> p.getProductType() == Category.ProductType.AUDIO_BOOK).collect(Collectors.toList());
            case "e-book-reader":
                return hotLists.stream().filter(p -> p.getProductType() == Category.ProductType.E_BOOK_READER).collect(Collectors.toList());
            case "e-book-reader-case":
                return hotLists.stream().filter(p -> p.getProductType() == Category.ProductType.E_BOOK_READER_CASE).collect(Collectors.toList());
            case "book-case":
                return hotLists.stream().filter(p -> p.getProductType() == Category.ProductType.BOOK_CASE).collect(Collectors.toList());
            default:
                return hotLists;
        }
    }


}
