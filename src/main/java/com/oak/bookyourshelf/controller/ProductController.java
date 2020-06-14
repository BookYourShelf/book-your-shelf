package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CompareProductsService;
import com.oak.bookyourshelf.service.ProductService;
import com.oak.bookyourshelf.service.ReviewService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelProductService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import com.oak.bookyourshelf.service.user_details.UserDetailsReviewService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    final ProductService productService;
    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final AdminPanelProductService adminPanelProductService;
    final ReviewService reviewService;
    final UserDetailsReviewService userDetailsReviewService;
    final ProductDetailsInformationService productDetailsInformationService;
    final CompareProductsService compareProductsService;

    public ProductController(ProductService productService,
                             ProfileInformationService profileInformationService,
                             @Qualifier("customUserDetailsService") AuthService authService,
                             AdminPanelProductService adminPanelProductService,
                             ReviewService reviewService, UserDetailsReviewService userDetailsReviewService,
                             ProductDetailsInformationService productDetailsInformationService, CompareProductsService compareProductsService) {

        this.productService = productService;
        this.profileInformationService = profileInformationService;
        this.authService = authService;
        this.adminPanelProductService = adminPanelProductService;
        this.reviewService = reviewService;
        this.userDetailsReviewService = userDetailsReviewService;
        this.productDetailsInformationService = productDetailsInformationService;
        this.compareProductsService = compareProductsService;
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String showProductPage(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @RequestParam("sort") Optional<String> sort,
                                  @RequestParam("ratingFilter") Optional<String> ratingFilter,
                                  @RequestParam("titleFilter") Optional<String> titleFilter, Model model, @PathVariable int id) {

        Product product = productService.get(id);
        List<Image> imageList = product.getImages();
        String currentSort = sort.orElse("date-desc");
        String curRatingFilter = ratingFilter.orElse("all");
        String curTitleFilter = titleFilter.orElse("all");
        Globals.getPageNumbers(page, size, titleFilterReview(
                ratingFilterReview(
                        userDetailsReviewService.sortReviewsOfProduct(currentSort, product),
                        curRatingFilter),
                curTitleFilter), model, "reviewPage");

        UserDetails userDetails = authService.getUserDetails();

        if (userDetails != null) {
            User user = profileInformationService.getByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
            model.addAttribute("alreadyReviewed", isAlreadyReviewed(user.getReviews(), product.getProductId()));
        }

        model.addAttribute("reviewListEmpty", (product.getReviews()).isEmpty());
        model.addAttribute("product", product);
        model.addAttribute("sort", currentSort);
        model.addAttribute("ratingFilter", curRatingFilter);
        model.addAttribute("titleFilter", curTitleFilter);
        model.addAttribute("images", Globals.encodeAllImages(imageList));
        model.addAttribute("similarProducts", productService.createOurPicsForYou(product));
        model.addAttribute("compareProductsService", compareProductsService);
        buildBreadCrumbs(product, model);

        return "product";
    }

    public void buildBreadCrumbs(Product product, Model model) {
        if (product instanceof PhysicalBook || product instanceof ElectronicBook || product instanceof AudioBook) {
            Category category = ((Book) product).getCategory();

            if (((Book) product).getSubcategory() != null) {
                Subcategory subcategory = ((Book) product).getSubcategory();
                List<Subcategory> subcategories = Globals.findPathBetweenSubcategoryAndCategory(category, subcategory);
                model.addAttribute("subcategoriesBreadcrumbs", subcategories);
            } else {
                model.addAttribute("subcategoriesBreadcrumbs", new ArrayList<>());
            }
            model.addAttribute("categoryBreadcrumb", category);
        }
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addProductToList(@RequestParam String type, @PathVariable int id, Review review, Integer reviewId) {

        UserDetails userDetails = authService.getUserDetails();
        Product product = productService.get(id);

        if (userDetails != null) {
            User user = profileInformationService.getByEmail(userDetails.getUsername());

            switch (type) {
                case "wish-list":
                    if (!containsProduct(user.getWishList(), product.getProductId())) {
                        user.addToWishList(product);
                        profileInformationService.save(user);
                    }
                    // already in wish list
                    return ResponseEntity.ok("");

                case "cart":
                    int quantity = containsCartItem(user.getShoppingCart(), product.getProductId());
                    if (quantity == 0) {
                        if (product.getStock() > 0) {
                            CartItem cartItem = new CartItem();
                            cartItem.setProduct(product);
                            cartItem.setQuantity(1);
                            user.addToCart(cartItem);
                            profileInformationService.save(user);
                            return ResponseEntity.ok("");

                        } else {
                            return ResponseEntity.badRequest().body("There is no stock.");
                        }
                    } else {
                        if (quantity + 1 > product.getStock()) {
                            return ResponseEntity.badRequest().body("There is not enough stock.");

                        } else {
                            updateCartItemQuantity(user.getShoppingCart(), product.getProductId());
                            // already in cart, quantity updated, save user
                            profileInformationService.save(user);
                            return ResponseEntity.ok("");
                        }
                    }

                case "reminder":
                    if (product.getStock() > 0) {
                        return ResponseEntity.badRequest().body("Product is already available.");

                    } else {
                        RemindProduct remindProduct = new RemindProduct();
                        remindProduct.setProductId(product.getProductId());
                        remindProduct.setRequestTime(new Timestamp(System.currentTimeMillis()));
                        remindProduct.setProductAvailability(RemindProduct.ProductAvailability.PENDING);
                        remindProduct.setUserId(user.getUserId());
                        product.getRemind().add(remindProduct);
                        productDetailsInformationService.save(product);
                        return ResponseEntity.ok("");
                    }

                case "add_review":
                    if (reviewService.checkUserReviewsForProduct(product, user) != null) {
                        return ResponseEntity.badRequest().body("You already reviewed this product.");

                    } else {
                        if (review.getScoreOutOf5() == 0) {
                            return ResponseEntity.badRequest().body("You must rate product to add review.");
                        }

                        review.setUploadDate(new Timestamp(System.currentTimeMillis()));
                        review.setUser(user);
                        review.setProduct(product);
                        user.addReview(review);
                        product.addReview(review);
                        product.increaseStarNum(review.getScoreOutOf5() - 1);
                        reviewService.save(review);

                        return ResponseEntity.ok("");
                    }

                case "delete_review":
                    Review toBeDeleted = reviewService.get(reviewId);
                    product.decreaseStarNum(toBeDeleted.getScoreOutOf5() - 1);
                    userDetailsReviewService.delete(reviewId);
                    return ResponseEntity.ok("");
            }

        } else {
            // direct to login page
            return ResponseEntity.badRequest().body("");
        }

        return ResponseEntity.badRequest().body("An error occurred.");
    }

    public List<Review> ratingFilterReview(List<Review> reviews, String rate) {
        switch (rate) {
            case "1":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 1).collect(Collectors.toList());
            case "2":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 2).collect(Collectors.toList());
            case "3":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 3).collect(Collectors.toList());
            case "4":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 4).collect(Collectors.toList());
            case "5":
                return reviews.stream().filter(r -> r.getScoreOutOf5() == 5).collect(Collectors.toList());
            default:
                return reviews;
        }
    }

    public List<Review> titleFilterReview(List<Review> reviews, String title) {
        switch (title) {
            case "titled":
                return reviews.stream().filter(r -> !r.getReviewTitle().equals("")).collect(Collectors.toList());
            case "untitled":
                return reviews.stream().filter(r -> r.getReviewTitle().equals("")).collect(Collectors.toList());
            default:
                return reviews;
        }
    }

    public int containsCartItem(Set<CartItem> set, int productId) {
        for (CartItem c : set) {
            if (c.getProduct().getProductId() == productId) {
                return c.getQuantity();
            }
        }
        return 0;
    }

    public void updateCartItemQuantity(Set<CartItem> set, int productId) {
        for (CartItem c : set) {
            if (c.getProduct().getProductId() == productId) {
                c.setQuantity(c.getQuantity() + 1);
            }
        }
    }

    public boolean containsProduct(Set<Product> set, int productId) {
        for (Product p : set) {
            if (p.getProductId() == productId) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlreadyReviewed(Set<Review> reviews, int productId) {
        for (Review r : reviews) {
            if (r.getProduct().getProductId() == productId) {
                return true;
            }
        }
        return false;
    }
}
