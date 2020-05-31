package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserDetailsSearchController {

    final UserDetailsSearchService userDetailsSearchService;

    public UserDetailsSearchController(UserDetailsSearchService userDetailsSearchService) {
        this.userDetailsSearchService = userDetailsSearchService;
    }


    @RequestMapping(value = "/user-details/search", method = RequestMethod.GET)
    public String tab(@RequestParam("id") int id, @RequestParam("size") Optional<Integer> size,
                      @RequestParam("page") Optional<Integer> page,  Model model) {

        User user = userDetailsSearchService.get(id);

        List<String> keys = new ArrayList<>(user.getSearchHistory().keySet());
        System.out.println("jbk");
        Globals.getPageNumbers(page, size, (List) keys, model, "keyPage");

        model.addAttribute("user", user);
        return "user_details/_search";
    }

    @RequestMapping(value = "/user-details/search", method = RequestMethod.POST)
    @ResponseBody
    public String users() {
        return "redirect:/user-details";
    }
}




