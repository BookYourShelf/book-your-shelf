package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CartService;
import com.oak.bookyourshelf.service.CouponDetailsService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class CartController {

    final CartService cartService;
    final AuthService authService;
    final ProfileInformationService profileInformationService;
    final ProductDetailsInformationService productDetailsInformationService;
    final CouponDetailsService couponDetailsService;

    public CartController(CartService cartService,
                          @Qualifier("customUserDetailsService") AuthService authService,
                          ProfileInformationService profileInformationService,
                          ProductDetailsInformationService productDetailsInformationService,
                          CouponDetailsService couponDetailsService) {
        this.cartService = cartService;
        this.authService = authService;
        this.profileInformationService = profileInformationService;
        this.productDetailsInformationService = productDetailsInformationService;
        this.couponDetailsService = couponDetailsService;
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String showCart(Model model) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("cartItems", user.getShoppingCart());
        model.addAttribute("coupons", couponDetailsService.listAll());
        return "cart";
    }

    @RequestMapping(value = "/cart/coupon", method = RequestMethod.GET)
    @ResponseBody
    public Number showCoupon(@RequestParam String coupon) {
        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());
        Coupon coup = couponDetailsService.getCouponByCode(coupon);

        if (coup == null) {
            return -1;
        }

        if (!coup.getUserId().contains(user.getUserId())) {
            return coup.getDiscountRate();
        } else {
            return -2;
        }
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> showCartList(@RequestParam String button, Integer productID, String subTotal,
                                               String shipping, Float discount, String codes, Integer quantity) {

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
                Order order = getUnpayedOrder(user.getOrders());
                boolean alreadyExist = true;

                if (order == null) {
                    order = new Order();
                    alreadyExist = false;
                }

                if (!user.getShoppingCart().isEmpty()) {
                    Product underStocked = getUnderStockedProduct(user.getShoppingCart());

                    if (underStocked == null) {
                        order.setProducts(new HashSet<>(user.getShoppingCart()));
                        order.setSubTotalAmount(Float.parseFloat(subTotal));
                        order.setPaymentStatus(Order.PaymentStatus.NULL);
                        order.setOrderCode(generateAndCheckOrderCode());
                        setOrderProductPriceAndDiscount(user.getShoppingCart());

                        if (shipping.equals("0")) {
                            order.setShippingMethod(Order.ShippingMethod.FREE);
                        } else {
                            order.setShippingMethod(Order.ShippingMethod.NEXT_DAY_DELIVERY);
                        }

                        if (discount != null && !codes.equals("")) {
                            order.setDiscountRate(discount);
                            order.setCouponCode(codes);
                        }

                        // After setting subtotal and shipping method set total amount
                        order.setTotalAmount(order.calculateTotalAmount());
                        order.setUserId(user.getUserId());

                        if (!alreadyExist) {
                            user.getOrders().add(order);
                        } else {
                            cartService.save(order);
                        }

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

    public void setOrderProductPriceAndDiscount(Set<CartItem> cart) {
        for (CartItem c : cart) {
            c.setUnitPrice(productDetailsInformationService.get(c.getProduct().getProductId()).getPrice());
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

    public Order getUnpayedOrder(Set<Order> orders) {
        for (Order o : orders) {
            if (o.getPaymentStatus() == Order.PaymentStatus.NULL) {
                return o;
            }
        }
        return null;
    }

    public String generateAndCheckOrderCode() {
        boolean uniqueCode = false;
        String generatedCode = null;

        while (!uniqueCode) {
            generatedCode = Globals.generateOrderCode();
            if (cartService.findByOrderCode(generatedCode) == null) {
                uniqueCode = true;
            }
        }
        return generatedCode;
    }
}