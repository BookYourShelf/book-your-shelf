package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);
        User user = userDetailsSearchService.get(id);
        System.out.println("jbk");
        Page<String> keyPage = userDetailsSearchService.findPaginated(PageRequest.of(currentPage - 1, pageSize), user);
        model.addAttribute("keyPage", keyPage);
        int totalPages = keyPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("user", user);
        model.addAttribute("currentPage", currentPage);

        return "user_details/_search";
    }

    @RequestMapping(value = "/user-details/search", method = RequestMethod.POST)
    @ResponseBody
    public String users() {
        return "redirect:/user-details";
    }
}




