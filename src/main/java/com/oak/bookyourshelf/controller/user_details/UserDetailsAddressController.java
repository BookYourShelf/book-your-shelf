package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.model.Address;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsAddressService;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UserDetailsAddressController {

    final UserDetailsAddressService userDetailsAddressService;
    final UserDetailsInformationService userDetailsInformationService;


    public UserDetailsAddressController(UserDetailsAddressService userDetailsAddressService, UserDetailsInformationService userDetailsInformationService) {
        this.userDetailsAddressService = userDetailsAddressService;
        this.userDetailsInformationService = userDetailsInformationService;
    }

    @RequestMapping(value = "/user-details/address/{id}", method = RequestMethod.GET)
    public String tab(Model model, @PathVariable int id) {
        User user = userDetailsInformationService.get(id);
        Address address = new Address();
        model.addAttribute("user", user);
        model.addAttribute("address", address);
        List<Address> userBilling = user.getBillingAddresses().stream().sorted(Comparator.comparing(Address::getTime).reversed()).collect(Collectors.toList());
        List<Address> userDelivery = user.getDeliveryAddresses().stream().sorted(Comparator.comparing(Address::getTime).reversed()).collect(Collectors.toList());
        model.addAttribute("allDeliveryAddress", userDelivery);
        model.addAttribute("allBillingAddress", userBilling);
        return "user_details/_address";
    }

    @RequestMapping(value = "/user-details/address/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateAddress(@RequestParam String button, @PathVariable int id, Address address) {

        User user = userDetailsInformationService.get(id);

        if (button.equals("add_billing_address")) {
            address.setTime(new Timestamp(System.currentTimeMillis()));
            userDetailsAddressService.save(address);
            user.getBillingAddresses().add(address);
            userDetailsInformationService.save(user);

        } else if (button.equals("add_delivery_address")) {
            address.setTime(new Timestamp(System.currentTimeMillis()));
            userDetailsAddressService.save(address);
            user.getDeliveryAddresses().add( address);
            userDetailsInformationService.save(user);

        } else if (button.equals("update_billing_address")) {
            Address oldBillingAddress = findAddress(user.getBillingAddresses(), address.getAddressId());
            oldBillingAddress = copyAddress(oldBillingAddress, address);
            userDetailsAddressService.save(oldBillingAddress);

        } else if (button.equals("update_delivery_address")) {
            Address oldDeliveryAddress = findAddress(user.getDeliveryAddresses(), address.getAddressId());
            oldDeliveryAddress = copyAddress(oldDeliveryAddress, address);
            userDetailsAddressService.save(oldDeliveryAddress);

        } else if (button.equals("delete_billing_address")) {
            Address toBeDeleted = findAddress(user.getBillingAddresses(), address.getAddressId());
            user.getBillingAddresses().remove(toBeDeleted);
            userDetailsAddressService.delete(address.getAddressId());

        } else {
            Address toBeDeleted = findAddress(user.getDeliveryAddresses(), address.getAddressId());
            user.getDeliveryAddresses().remove(toBeDeleted);
            userDetailsAddressService.delete(address.getAddressId());
        }

        return ResponseEntity.ok("");
    }

    public Address findAddress(Set<Address> addressList, int Id) {
        for (Address add : addressList) {
            if (add.getAddressId() == Id) {
                return add;
            }
        }
        return null;
    }

    public Address copyAddress(Address oldAddress, Address address) {
        oldAddress.setName(address.getName());
        oldAddress.setSurname(address.getSurname());
        oldAddress.setCountry(address.getCountry());
        oldAddress.setCity(address.getCity());
        oldAddress.setZipCode(address.getZipCode());
        oldAddress.setTown(address.getTown());
        oldAddress.setAddressTitle(address.getAddressTitle());
        oldAddress.setFullAddress(address.getFullAddress());
        oldAddress.setPhoneNumber(address.getPhoneNumber());
        return oldAddress;
    }
}
