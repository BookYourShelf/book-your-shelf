package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserDetailsSearchController {

    final UserDetailsSearchService userDetailsSearchService;

    public UserDetailsSearchController(UserDetailsSearchService userDetailsSearchService) {
        this.userDetailsSearchService = userDetailsSearchService;
    }


    @RequestMapping(value = "/user-details/search/{id}", method = RequestMethod.GET)
    public String tab(Model model, @PathVariable("id") int id) {

        User user = userDetailsSearchService.get(id);
        model.addAttribute("searchValues", user.getSearchHistory());

        return "/user_details/_search";
    }
}
