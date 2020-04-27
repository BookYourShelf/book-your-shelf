package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.model.Address;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsAddressService;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserDetailsAddressController {

    final UserDetailsAddressService userDetailsAddressService;
    final UserDetailsInformationService userDetailsInformationService;
    int userId;

    public UserDetailsAddressController(UserDetailsAddressService userDetailsAddressService, UserDetailsInformationService userDetailsInformationService){
        this.userDetailsAddressService = userDetailsAddressService;
        this.userDetailsInformationService = userDetailsInformationService;
    }

    @RequestMapping(value = "/user-details/address/{id}", method = RequestMethod.GET)
    public String tab(Model model, @PathVariable int id) {
        User user = userDetailsInformationService.get(id);
        userId=id;
        System.out.println(id);
        model.addAttribute("user", user);
        System.out.println("hello");
        return "/user_details/_address";
    }

    @RequestMapping(value = "/user-details/address/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> AddressUpdate(@RequestParam String button_2,@RequestParam String button_3, @PathVariable int id, Address address) {
        System.out.println(button_2);
        System.out.println(id);
        User user = userDetailsInformationService.get(id);
        /*ADD BILLING ADDRESS*/
        if(button_2.equals("add_billing_address")){
        System.out.println("I am inside add billing address");
        System.out.println(address.getCountry());
        if(!user.getName().equals(address.getName())){
            return ResponseEntity.badRequest().body("User's name must match with the name used to register to the system.");
        }
        if(!user.getSurname().equals(address.getSurname())){
            return ResponseEntity.badRequest().body("User's surname must match with the surname used to register to the system.");
        }
        userDetailsAddressService.save(address);
        user.getBillingAddresses().add(address);

        System.out.println(address.getAddressId());
        }

      return ResponseEntity.ok("");
    }
}
