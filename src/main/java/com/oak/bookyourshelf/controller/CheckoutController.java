package com.oak.bookyourshelf.controller;


import com.oak.bookyourshelf.model.Address;
import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CheckoutService;
import com.oak.bookyourshelf.service.profile.ProfileAddressService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CheckoutController {

    final CheckoutService checkoutService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final ProfileAddressService profileAddressService;
    Order order = CartController.order;


    public CheckoutController(CheckoutService checkoutService, @Qualifier("customUserDetailsService") AuthService authService,
                              ProfileInformationService profileInformationService, ProfileAddressService profileAddressService) {
        this.checkoutService = checkoutService;
        this.authService = authService;
        this.profileInformationService = profileInformationService;
        this.profileAddressService = profileAddressService;
    }


    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public String showCheckout(Model model) {

        System.out.println(order.getTotalAmount());
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        model.addAttribute("order", order);
        model.addAttribute("user", user);
        model.addAttribute("allDeliveryAddress", user.getDeliveryAddresses());
        model.addAttribute("allBillingAddress", user.getBillingAddresses());
        return "/checkout";
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> showCheckout(@RequestParam String button, @RequestParam Optional<String> cargo,
                                               @RequestParam Optional<Integer> billing_address, @RequestParam Optional<Integer> delivery_address,
                                               Address address) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());

        if (button.equals("add_delivery_address")) {
            user.getDeliveryAddresses().add(address);
            profileInformationService.save(user);

        } else if (button.equals("add_billing_address")) {
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

        } else if (button.equals("delete_delivery_address")) {
            Address toBeDeleted = findAddress(user.getDeliveryAddresses(), address.getAddressId());
            user.getDeliveryAddresses().remove(toBeDeleted);
            profileAddressService.delete(address.getAddressId());
        } else {

            order.setShippingCompany(cargo.get());
            order.setBillingAddress(billing_address.get().toString());
            order.setCustomerAddress(delivery_address.get().toString());

        }


        return ResponseEntity.ok("");

    }

    public Address findAddress(List<Address> addressList, int Id) {
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