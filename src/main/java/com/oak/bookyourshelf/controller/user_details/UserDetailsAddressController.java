package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.model.Address;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsAddressService;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserDetailsAddressController {

    final UserDetailsAddressService userDetailsAddressService;
    final UserDetailsInformationService userDetailsInformationService;

    public UserDetailsAddressController(UserDetailsAddressService userDetailsAddressService, UserDetailsInformationService userDetailsInformationService){
        this.userDetailsAddressService = userDetailsAddressService;
        this.userDetailsInformationService = userDetailsInformationService;
    }

    @RequestMapping(value = "/user-details/address/{id}", method = RequestMethod.GET)
    public String tab(Model model, @PathVariable int id) {
        User user = userDetailsInformationService.get(id);
        model.addAttribute("user", user);


        System.out.println("hello");
        return "/user_details/_address";
    }

    @RequestMapping(value = "/user-details/address/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> AddressUpdate(@RequestParam String button_2, @PathVariable int id, User newUser) {
        System.out.println(button_2);
        if(button_2.equals("add_billing_address")){
            System.out.println();
        }
      return null;
    }
}
