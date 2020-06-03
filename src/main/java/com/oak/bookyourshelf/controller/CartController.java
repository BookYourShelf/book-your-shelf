package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Order;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.CartRepository;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CartService;
import com.oak.bookyourshelf.service.ProductService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CartController {

    final CartService cartService;
    final AuthService authService;
    final ProfileInformationService profileInformationService;
    final ProductDetailsInformationService productDetailsInformationService;
    

    public CartController(CartService cartService, @Qualifier("customUserDetailsService") AuthService authService,
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
        model.addAttribute("cartProducts", user.getShoppingCart());
        return "/cart";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public ResponseEntity<String> showCartList(@RequestParam String button, @RequestParam Optional<Integer> productID,
                                               @RequestParam Optional<String> totalAmount, @RequestParam Optional<String> shipping,
                                               @RequestParam Optional<String> coupon, @RequestParam Optional<Integer> quantity) {

        UserDetails userDetails = authService.getUserDetails();
        User user = profileInformationService.getByEmail(userDetails.getUsername());

        if (button.equals("add_to_wish_list")) {
            Product product = productDetailsInformationService.get(productID.get());

            if (!user.getWishList().contains(product)) {
                user.getShoppingCart().remove(product);
                profileInformationService.save(user);
                user.getWishList().add(product);
                profileInformationService.save(user);
            } else {
                user.getShoppingCart().remove(product);
                profileInformationService.save(user);
            }

        } else if (button.equals("delete_product")) {
            Product product = productDetailsInformationService.get(productID.get());
            user.getShoppingCart().remove(product);
            profileInformationService.save(user);

        } else if (button.equals("clear_list")) {
            user.getShoppingCart().clear();
            profileInformationService.save(user);

        } else if (button.equals("checkout")) {
            Order order = new Order();

            if (!user.getShoppingCart().isEmpty()) {
                if (checkStock(user.getShoppingCart())) {

                    List<Integer> productIds = user.getShoppingCart().stream().map(Product::getProductId).collect(Collectors.toList());
                    order.setProductId(productIds);
                    order.setTotalAmount(Float.parseFloat(totalAmount.get()));
                    productDiscountMap(user.getShoppingCart(), order); // discount Map Saver
                    if (shipping.get().equals("0")) {
                        order.setShippingMethod("Free");
                    } else {
                        order.setShippingMethod("Next day delivery");
                    }

                } else {
                    return ResponseEntity.badRequest().body("Shopping cart product out of stock.");
                }
            } else {
                return ResponseEntity.badRequest().body("Please add products to shopping cart.");
            }

           for (Order o : user.getOrders()) {
               if (o.getPaymentStatus() == null) {

                    ResponseEntity.badRequest(); // TODO: There is another order ongoing. Handle that
                }
            }

            order.setUserId(user.getUserId());
            user.getOrders().add(order);
            profileInformationService.save(user);

        } else if (button.equals("qty_add")) {
            Product product = productDetailsInformationService.get(productID.get());
            product.getProductQuantity().put(product.getProductId(), quantity.get());
            productDetailsInformationService.save(product);

        } else {
            Product product = productDetailsInformationService.get(productID.get());
            product.getProductQuantity().put(product.getProductId(), quantity.get());
            productDetailsInformationService.save(product);

        }
        return ResponseEntity.ok("");
    }

    public boolean checkStock(List<Product> cart) {
        for (Product p : cart) {
            System.out.println(p.getProductQuantity().get(p.getProductId()));
            System.out.println(p.getStock());
            if (p.getStock() != 0) {
                if (p.getStock() >= p.getProductQuantity().get(p.getProductId())) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void productDiscountMap(List<Product> shoppingCart, Order order) {
        for (Product p : shoppingCart) {
            if (p.getDiscountRate() != 0) {
                order.getProductDiscount().clear(); // Map Set to Zero
                order.getProductDiscount().put(p.getProductId(), p.getDiscountRate());
            }
        }
    }


}