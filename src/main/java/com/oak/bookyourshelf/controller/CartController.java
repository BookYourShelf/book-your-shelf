package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.CartItem;
import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CartService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class CartController {

    final CartService cartService;
    final AuthService authService;
    final ProfileInformationService profileInformationService;
    final ProductDetailsInformationService productDetailsInformationService;

    public CartController(CartService cartService,
                          @Qualifier("customUserDetailsService") AuthService authService,
                          ProfileInformationService profileInformationService,
                          ProductDetailsInformationService productDetailsInformationService) {
        this.cartService = cartService;
        this.authService = authService;
        this.profileInformationService = profileInformationService;
        this.productDetailsInformationService = productDetailsInformationService;
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String showCart(Model model) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("cartItems", user.getShoppingCart());
        return "/cart";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> showCartList(@RequestParam String button, Integer productID, String totalAmount,
                                               String shipping, String coupon, Integer quantity) {

        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());

        switch (button) {
            case "add_to_wish_list": {
                Product product = productDetailsInformationService.get(productID);

                if (!containsProduct(user.getWishList(), productID)) {
                    deleteCartItemFromCart(user.getShoppingCart(), productID);
                    profileInformationService.save(user);
                    user.getWishList().add(product);

                } else {
                    deleteCartItemFromCart(user.getShoppingCart(), productID);
                }

                profileInformationService.save(user);
                break;
            }
            case "delete_product": {
                deleteCartItemFromCart(user.getShoppingCart(), productID);
                profileInformationService.save(user);
                break;
            }
            case "clear_list":
                user.getShoppingCart().clear();
                profileInformationService.save(user);
                break;

            case "checkout":
                removeUnpayedOrder(user.getOrders());
                Order order = new Order();

                if (!user.getShoppingCart().isEmpty()) {
                    Product underStocked = getUnderStockedProduct(user.getShoppingCart());

                    if (underStocked == null) {
                        order.setProductId(getProductIdsOfCartItems(user.getShoppingCart()));
                        order.setTotalAmount(Float.parseFloat(totalAmount));
                        setOrderProductsDiscounts(user.getShoppingCart());

                        if (shipping.equals("0")) {
                            order.setShippingMethod(Order.ShippingMethod.FREE);
                        } else {
                            order.setShippingMethod(Order.ShippingMethod.NEXT_DAY_DELIVERY);
                        }

                        order.setUserId(user.getUserId());
                        user.getOrders().add(order);
                        profileInformationService.save(user);
                        break;

                    } else {
                        return ResponseEntity.badRequest().body(underStocked.getProductName() + " out of stock.");
                    }

                } else {
                    return ResponseEntity.badRequest().body("Please add products to the shopping cart.");
                }

            case "qty_add":
            case "qty_sub":
                for (CartItem c : user.getShoppingCart()) {
                    if (c.getProduct().getProductId() == productID) {
                        c.setQuantity(quantity);
                    }
                }
                profileInformationService.save(user);
                break;

            default:
                return ResponseEntity.badRequest().body("An error occurred.");
        }
        return ResponseEntity.ok("");
    }

    public Product getUnderStockedProduct(Set<CartItem> cart) {
        for (CartItem c : cart) {
            if (productDetailsInformationService.get(c.getProduct().getProductId()).getStock() < c.getQuantity()) {
                return productDetailsInformationService.get(c.getProduct().getProductId());
            }
        }
        return null;
    }

    public void setOrderProductsDiscounts(Set<CartItem> cart) {
        for (CartItem c : cart) {
            c.setDiscountRate(productDetailsInformationService.get(c.getProduct().getProductId()).getDiscountRate());
        }
    }

    public void deleteCartItemFromCart(Set<CartItem> set, int productId) {
        set.removeIf(c -> c.getProduct().getProductId() == productId);
    }

    public boolean containsProduct(Set<Product> set, int productId) {
        for (Product p : set) {
            if (p.getProductId() == productId) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> getProductIdsOfCartItems(Set<CartItem> cart) {
        List<Integer> productIds = new ArrayList<Integer>();
        for (CartItem c : cart) {
            productIds.add(c.getProduct().getProductId());
        }
        return productIds;
    }

    public void removeUnpayedOrder(List<Order> orders) {
        orders.removeIf(o -> o.getPaymentStatus() == null);
    }
}