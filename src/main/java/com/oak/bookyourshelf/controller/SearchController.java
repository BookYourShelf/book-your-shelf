package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SearchController {

    final AuthService authService;
    final ProfileInformationService profileInformationService;

    public SearchController(@Qualifier("customUserDetailsService") AuthService authService, ProfileInformationService profileInformationService) {
        this.authService = authService;
        this.profileInformationService = profileInformationService;
    }


    @RequestMapping(value = "/search-result", method = RequestMethod.GET)
    public String tab(Model model, @RequestParam String search) {

        User user =new User();
        UserDetails userDetails = authService.getUserDetails();
        if(userDetails != null) {
            user= profileInformationService.getByEmail(userDetails.getUsername());
            if(profileInformationService.findSearchValue(search,user))
            {
                user.getSearchHistory().put(search,(user.getSearchHistory().get(search)+1));
            }
            else
            {
                user.getSearchHistory().put(search,1);
            }
        }
        profileInformationService.save(user);
        model.addAttribute("searchValue",search);

        return "/search-result";
    }




    }
