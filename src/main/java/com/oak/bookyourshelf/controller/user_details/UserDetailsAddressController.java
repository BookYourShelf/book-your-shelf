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


    public UserDetailsAddressController(UserDetailsAddressService userDetailsAddressService, UserDetailsInformationService userDetailsInformationService){
        this.userDetailsAddressService = userDetailsAddressService;
        this.userDetailsInformationService = userDetailsInformationService;
    }

    @RequestMapping(value = "/user-details/address/{id}", method = RequestMethod.GET)
    public String tab(Model model, @PathVariable int id) {
        User user = userDetailsInformationService.get(id);

        Address address = new Address();
        System.out.println(id);
        model.addAttribute("user", user);
        model.addAttribute("address",address);
        model.addAttribute("allDeliveryAddress",user.getDeliveryAddresses());
        model.addAttribute("allBillingAddress", user.getBillingAddresses());
        System.out.println("hello");
        return "/user_details/_address";
    }

    @RequestMapping(value = "/user-details/address/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> AddressUpdate(@RequestParam String button_2, @PathVariable int id, Address address) {
        System.out.println(button_2);
        System.out.println(id);
        User user = userDetailsInformationService.get(id);
        /*ADD BILLING ADDRESS*/
        if(button_2.equals("add_billing_address")){
            System.out.println("I am inside add billing address");

            userDetailsAddressService.save(address);
            user.getBillingAddresses().add(address);
            userDetailsInformationService.save(user);
        }
        /*ADD DELIVERY ADDRESS*/
        else if(button_2.equals("add_delivery_address")){

            userDetailsAddressService.save(address);
            user.getDeliveryAddresses().add(address);
            userDetailsInformationService.save(user);
        }

        else if(button_2.equals("update_billing_address")){
            Address oldAddress = findAddress(user.getBillingAddresses(), address.getAddressId());

            System.out.println("I am updating");
            System.out.println(address.getAddressTitle());
            System.out.println(address.getAddressId());
            System.out.println(address.getZipCode());

            oldAddress.setName(address.getName());
            oldAddress.setSurname(address.getSurname());
            oldAddress.setCountry(address.getCountry());
            oldAddress.setCity(address.getCity());
            oldAddress.setZipCode(address.getZipCode());  // update checking using zip code
            oldAddress.setNeighborhood(address.getNeighborhood());
            oldAddress.setProvince(address.getProvince());
            oldAddress.setAddressTitle(address.getAddressTitle());
            oldAddress.setFullAddress(address.getFullAddress());
            oldAddress.setPhoneNumber(address.getPhoneNumber());
            userDetailsAddressService.save(oldAddress);

        }

        else if(button_2.equals("update_delivery_address")){
            Address oldAddress = findAddress(user.getDeliveryAddresses(), address.getAddressId());

            System.out.println("I am updatings");
            System.out.println(address.getAddressTitle());
            System.out.println(address.getAddressId());
            System.out.println(address.getZipCode());
            // TODO Will put inside function
            oldAddress.setName(address.getName());
            oldAddress.setSurname(address.getSurname());
            oldAddress.setCountry(address.getCountry());
            oldAddress.setCity(address.getCity());
            oldAddress.setZipCode(address.getZipCode());  // update checking using zip code
            oldAddress.setNeighborhood(address.getNeighborhood());
            oldAddress.setProvince(address.getProvince());
            oldAddress.setAddressTitle(address.getAddressTitle());
            oldAddress.setFullAddress(address.getFullAddress());
            oldAddress.setPhoneNumber(address.getPhoneNumber());
            userDetailsAddressService.save(oldAddress);

        }

      return ResponseEntity.ok("");
    }

    public  Address findAddress(List<Address> addressList, int Id){
            for(Address add: addressList){
                if(add.getAddressId()== Id){
                    System.out.println(add.getAddressTitle());
                    return add;
                }
            }
            return null;
        }
}
