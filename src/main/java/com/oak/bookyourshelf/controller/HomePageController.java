package com.oak.bookyourshelf.controller;


import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Image;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.Review;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.CompareProductsService;
import com.oak.bookyourshelf.service.HomePageService;
import com.oak.bookyourshelf.service.ProductService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomePageController {

    final AuthService authService;
    final ProfileInformationService profileInformationService;
    final HomePageService homePageService;
    final CompareProductsService compareProductsService;
    final ProductService productService;
    final ProductDetailsInformationService productDetailsInformationService;


    public HomePageController(AuthService authService, ProfileInformationService profileInformationService, HomePageService homePageService, CompareProductsService compareProductsService, ProductService productService, ProductDetailsInformationService productDetailsInformationService) {
        this.authService = authService;
        this.profileInformationService = profileInformationService;
        this.homePageService = homePageService;
        this.compareProductsService = compareProductsService;
        this.productService = productService;
        this.productDetailsInformationService = productDetailsInformationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showUser(Model model) {

        UserDetails userDetails = authService.getUserDetails();
        List<Product> ourPicksForYou = new ArrayList<>();
        if (userDetails != null) {
            /*List<Image> bestSellerImages= createImageList(homePageService.createBestSellerList(0,8));*/
            model.addAttribute("bestSeller", homePageService.createBestSellerList(0, 8));
            /*model.addAttribute("bestSellerImages", Globals.encodeAllImages(bestSellerImages));*/

            /*List<Image> newReleasesImages= createImageList(homePageService.createNewProductsList(0,8));*/
            model.addAttribute("newProducts", homePageService.createNewProductsList(0, 8));
            /*model.addAttribute("newReleasesImages",Globals.encodeAllImages(newReleasesImages));*/

            /*List<Image> moreBestSellerImages = createImageList(homePageService.createBestSellerList(8,15));*/
            model.addAttribute("moreBestSeller", homePageService.createBestSellerList(8, 15));
            /*model.addAttribute("moreBestSellerImages",Globals.encodeAllImages(moreBestSellerImages));*/

            /*List<Image> moreNewReleasesImages = createImageList(homePageService.createNewProductsList(8,15));*/
            model.addAttribute("moreNewProducts", homePageService.createNewProductsList(8, 15));
            /*model.addAttribute("moreNewReleasesImages",Globals.encodeAllImages(moreNewReleasesImages));*/

            /*List<Image> ourPicksImages_1 = createImageList(homePageService.createBestSellerList(0,4));*/
            model.addAttribute("ourPicks_1", homePageService.createBestSellerList(0, 4));
           /* model.addAttribute("ourPicksImages_1",Globals.encodeAllImages(ourPicksImages_1));*/

            /*List<Image> ourPicksImages_2 = createImageList(homePageService.createBestSellerList(4,8));*/
            model.addAttribute("ourPicks_2", homePageService.createBestSellerList(4, 8));
           /* model.addAttribute("ourPicksImages_2",Globals.encodeAllImages(ourPicksImages_2));*/

            /*List<Image> ourPicksImages_3 = createImageList(homePageService.createBestSellerList(8,12));*/
            model.addAttribute("ourPicks_3", homePageService.createBestSellerList(8, 12));
            /*model.addAttribute("ourPicksImages_3",Globals.encodeAllImages(ourPicksImages_3));*/

            model.addAttribute("user", profileInformationService.getByEmail(userDetails.getUsername()));

        } else {
           /* List<Image> bestSellerImages= createImageList(homePageService.createBestSellerList(0,8));*/
            model.addAttribute("bestSeller", homePageService.createBestSellerList(0, 8));
            /*model.addAttribute("bestSellerImages", Globals.encodeAllImages(bestSellerImages));*/

           /* List<Image> newReleasesImages= createImageList(homePageService.createNewProductsList(0,8));*/
            model.addAttribute("newProducts", homePageService.createNewProductsList(0, 8));
            /*model.addAttribute("newReleasesImages",Globals.encodeAllImages(newReleasesImages));*/

           /* List<Image> moreBestSellerImages = createImageList(homePageService.createBestSellerList(8,15));*/
            model.addAttribute("moreBestSeller", homePageService.createBestSellerList(8, 15));
            /*model.addAttribute("moreBestSellerImages",Globals.encodeAllImages(moreBestSellerImages));*/

           /* List<Image> moreNewReleasesImages = createImageList(homePageService.createNewProductsList(8,15));*/
            model.addAttribute("moreNewProducts", homePageService.createNewProductsList(8, 15));
           /* model.addAttribute("moreNewReleasesImages",Globals.encodeAllImages(moreNewReleasesImages));*/

            model.addAttribute("ourPicks_1", ourPicksForYou);
        }
        model.addAttribute("compareProductsService", compareProductsService);
        return "/index";
    }


    @RequestMapping(value = "/index/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addProductToList(@RequestParam String type, @PathVariable int id) {

        UserDetails userDetails = authService.getUserDetails();
        Product product = productService.get(id);

        if (userDetails != null) {
            User user = profileInformationService.getByEmail(userDetails.getUsername());

            switch (type) {
                case "wish-list":
                    if (!user.getWishList().contains(product)) {
                        user.addToWishList(product);
                        profileInformationService.save(user);
                    }
                    // already in wish list
                    return ResponseEntity.ok("");

                case "cart":
                    if (!user.getShoppingCart().contains(product)) {
                        if (product.getStock() > 0) {
                            product.getProductQuantity().put(product.getProductId(), 1);
                            user.addToCart(product);
                            productDetailsInformationService.save(product);
                            profileInformationService.save(user);
                            return ResponseEntity.ok("");
                        } else {
                            return ResponseEntity.badRequest().body("There is no stock.");
                        }
                    }

                    product.getProductQuantity().put(product.getProductId(), product.getProductQuantity().get(product.getProductId()) + 1);
                    productDetailsInformationService.save(product);
                    // already in cart
                    return ResponseEntity.ok("");


            }
        } else {
            // direct to login page
            return ResponseEntity.badRequest().body("");
        }

        return ResponseEntity.badRequest().body("An error occurred.");
    }

    public List<Image> createImageList( List<Product> products)
    {
        List<Image> images = new ArrayList<>();
        for(Product p:products)
        {
            images.add(p.getCoverImage());
        }
        return images;

    }


}
