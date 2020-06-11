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

import java.util.List;

@Controller
public class CheckoutController {

    final CheckoutService checkoutService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final ProfileAddressService profileAddressService;
    Order order;

    public CheckoutController(CheckoutService checkoutService,
                              @Qualifier("customUserDetailsService") AuthService authService,
                              ProfileInformationService profileInformationService,
                              ProfileAddressService profileAddressService) {
        this.checkoutService = checkoutService;
        this.authService = authService;
        this.profileInformationService = profileInformationService;
        this.profileAddressService = profileAddressService;
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public String showCheckout(Model model) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        for (Order o : user.getOrders()) {
            if (o.getPaymentStatus() == Order.PaymentStatus.NULL) {
                order = o;
            }
        }
        model.addAttribute("order", order);
        model.addAttribute("user", user);
        model.addAttribute("allDeliveryAddress", user.getDeliveryAddresses());
        model.addAttribute("allBillingAddress", user.getBillingAddresses());
        return "/checkout";
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> showCheckout(@RequestParam String button, String cargo, Integer billing_address,
                                               Integer delivery_address, Address address) {

        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());

        switch (button) {
            case "add_delivery_address":
                user.getDeliveryAddresses().add(address);
                profileInformationService.save(user);
                break;

            case "add_billing_address":
                user.getBillingAddresses().add(address);
                profileInformationService.save(user);
                break;

            case "update_billing_address":
                Address oldBillingAddress = findAddress(user.getBillingAddresses(), address.getAddressId());
                oldBillingAddress = copyAddress(oldBillingAddress, address);
                profileAddressService.save(oldBillingAddress);
                break;

            case "update_delivery_address":
                Address oldDeliveryAddress = findAddress(user.getDeliveryAddresses(), address.getAddressId());
                oldDeliveryAddress = copyAddress(oldDeliveryAddress, address);
                profileAddressService.save(oldDeliveryAddress);
                break;

            case "delete_billing_address":
                Address toBeDeletedBilling = findAddress(user.getBillingAddresses(), address.getAddressId());
                user.getBillingAddresses().remove(toBeDeletedBilling);
                profileAddressService.delete(address.getAddressId());
                break;

            case "delete_delivery_address":
                Address toBeDeletedDelivery = findAddress(user.getDeliveryAddresses(), address.getAddressId());
                user.getDeliveryAddresses().remove(toBeDeletedDelivery);
                profileAddressService.delete(address.getAddressId());
                break;

            case "payment":
                if (billing_address != null && delivery_address != null) {
                    order.setShippingCompany(cargo);
                    order.setBillingAddress(getSelectedAddress(user.getBillingAddresses(), billing_address).getOrderAddress());
                    order.setDeliveryAddress(getSelectedAddress(user.getDeliveryAddresses(), delivery_address).getOrderAddress());

                    // Save updated order
                    List<Order> orders = user.getOrders();
                    for (int i = 0; i < orders.size(); i++) {
                        if (orders.get(i).getOrderId() == order.getOrderId()) {
                            orders.set(i, order);
                        }
                    }
                    profileInformationService.save(user);

                } else if (billing_address == null) {
                    return ResponseEntity.badRequest().body("Billing address have to be selected.");

                } else {
                    return ResponseEntity.badRequest().body("Delivery address have to be selected.");
                }
                break;

            default:
                return ResponseEntity.badRequest().body("An error occurred.");
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

    public Address getSelectedAddress(List<Address> addresses, int addressId) {
        for (Address a : addresses) {
            if (a.getAddressId() == addressId) {
                return a;
            }
        }
        return null;
    }
}
