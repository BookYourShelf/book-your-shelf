package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserDetailsSearchController {

    final UserDetailsSearchService userDetailsSearchService;

    public UserDetailsSearchController(UserDetailsSearchService userDetailsSearchService) {
        this.userDetailsSearchService = userDetailsSearchService;
    }

    @RequestMapping(value = "/user-details/search", method = RequestMethod.GET)
    public String tab(@RequestParam("id") int id,
                      @RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      @RequestParam("sort") Optional<String> sort, Model model) {

        User user = userDetailsSearchService.get(id);
        String currentSort = sort.orElse("Total-Search-desc");

        Globals.getPageNumbers(page, size, userDetailsSearchService.sortSearchKey(currentSort, user), model, "keyPage");
        model.addAttribute("sort", currentSort);
        model.addAttribute("user", user);
        model.addAttribute("searchValuesListEmpty", user.getSearchHistory().isEmpty());
        return "user_details/_search";
    }

    @RequestMapping(value = "/user-details/search", method = RequestMethod.POST)
    @ResponseBody
    public String users() {
        return "redirect:/user-details";
    }
}




