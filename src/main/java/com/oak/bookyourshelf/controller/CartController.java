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
    public static Order order = new Order();


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
                                               @RequestParam Optional<String> subTotal, @RequestParam Optional<String> totalAmount,
                                               @RequestParam Optional<String> shipping, @RequestParam Optional<String> coupon,
                                               @RequestParam Optional<Integer> quantity, @RequestParam Optional<Float> quantityPrice) {

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

            if (!user.getShoppingCart().isEmpty()) {
                if (checkStock(user.getShoppingCart())) {
                    //Todo add product quantity
                    List<Integer> productIds = user.getShoppingCart().stream().map(Product::getProductId).collect(Collectors.toList());
                    List<Float> discountRates = user.getShoppingCart().stream().map(Product::getDiscountRate).collect(Collectors.toList());
                    List<Integer> quantities = user.getShoppingCart().stream().map(Product::getQuantity).collect(Collectors.toList());
                    order.setProductId(productIds);
                    order.setDiscountRate(discountRates);
                    order.setQuantity(quantities);
                    order.setTotalAmount(Float.parseFloat(totalAmount.get()));
                    order.setSubTotal(Float.parseFloat(subTotal.get()));
                    order.setShippingMethod(shipping.get());
                } else {
                    return ResponseEntity.badRequest().body("Shopping cart product out of stock.");
                }
            } else {
                return ResponseEntity.badRequest().body("Please add products to shopping cart.");
            }

        } else if (button.equals("qty_add")) {
            Product product = productDetailsInformationService.get(productID.get());
            quantity(product, quantity.get(), quantityPrice.get());


        } else {
            Product product = productDetailsInformationService.get(productID.get());
            quantity(product, quantity.get(), quantityPrice.get());

        }
        return ResponseEntity.ok("");
    }

    public boolean checkStock(List<Product> cart) {
        for (Product p : cart) {
            if (p.getStock() == 0) {
                return false;
            }
        }
        return true;
    }

    public void quantity(Product product, int quantity, float price) {
        product.setQuantity(quantity);
        product.setQuantityPrice(price);
        productDetailsInformationService.save(product);
    }
}