package com.oak.bookyourshelf.controller.profile;

import com.oak.bookyourshelf.model.Address;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.profile.ProfileAddressService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProfileAddressController {

    final ProfileAddressService profileAddressService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;


    public ProfileAddressController(ProfileAddressService profileAddressService, @Qualifier("customUserDetailsService") AuthService authService,
                                    ProfileInformationService profileInformationService) {

        this.profileAddressService = profileAddressService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
    }

    @RequestMapping(value = "/profile/address", method = RequestMethod.GET)
    public String showAddress(Model model) {
        UserDetails userDetails = authService.getUserDetails();
        if (userDetails != null) {
            User user = profileInformationService.getByEmail(userDetails.getUsername());
            Address address = new Address();
            model.addAttribute("address", address);
            model.addAttribute("user", user);
            List<Address> userBilling = user.getBillingAddresses().stream().sorted(Comparator.comparing(Address::getTime).reversed()).collect(Collectors.toList());
            List<Address> userDelivery = user.getDeliveryAddresses().stream().sorted(Comparator.comparing(Address::getTime).reversed()).collect(Collectors.toList());
            model.addAttribute("allBillingAddress", userBilling);
            model.addAttribute("allDeliveryAddress", userDelivery);
            return "profile/_address";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/profile/address", method = RequestMethod.POST)
    public ResponseEntity<String> updateAddress(@RequestParam String button, Address address) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());


        if (button.equals("add_delivery_address")) {
            address.setTime(new Timestamp(System.currentTimeMillis()));
            profileAddressService.save(address);
            user.getDeliveryAddresses().add(address);
            profileInformationService.save(user);

        } else if (button.equals("add_billing_address")) {
            address.setTime(new Timestamp(System.currentTimeMillis()));
            profileAddressService.save(address);
            user.getBillingAddresses().add(address);
            profileInformationService.save(user);

        } else if (button.equals("update_billing_address")) {
            Address oldBillingAddress = findAddress(user.getBillingAddresses(), address.getAddressId());
            oldBillingAddress = copyAddress(oldBillingAddress, address);
            profileAddressService.save(oldBillingAddress);

        } else if (button.equals("update_delivery_address")) {
            Address oldDeliveryAddress = findAddress(user.getDeliveryAddresses(), address.getAddressId());
            oldDeliveryAddress = copyAddress(oldDeliveryAddress, address);
            profileAddressService.save(oldDeliveryAddress);

        } else if (button.equals("delete_billing_address")) {
            Address toBeDeleted = findAddress(user.getBillingAddresses(), address.getAddressId());
            user.getBillingAddresses().remove(toBeDeleted);
            profileAddressService.delete(address.getAddressId());

        } else {
            Address toBeDeleted = findAddress(user.getDeliveryAddresses(), address.getAddressId());
            user.getDeliveryAddresses().remove(toBeDeleted);
            profileAddressService.delete(address.getAddressId());
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
