package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class ElectronicBookReaderController {
    final ProductService productService;

    public ElectronicBookReaderController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/ebookreaders", method = RequestMethod.GET)
    public String showCategory(@RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               @RequestParam("brands") Optional<String> brands,
                               @RequestParam("colors") Optional<String> colors,
                               @RequestParam("sort") Optional<String> sort,
                               @RequestParam("discount") Optional<String> discount,
                               @RequestParam("stars") Optional<String> stars,
                               @RequestParam("minPrice") Optional<String> minPrice,
                               @RequestParam("maxPrice") Optional<String> maxPrice,
                               Model model) {

        String currentSort = sort.orElse("date-desc");

        // Parse parameters
        List<String> brandList = new ArrayList<>(Arrays.asList(brands.orElse("").split(",")));
        List<String> colorList = new ArrayList<>(Arrays.asList(colors.orElse("").split(",")));
        List<String> discountList = new ArrayList<>(Arrays.asList(discount.orElse("").split(",")));
        List<String> starList = new ArrayList<>(Arrays.asList(stars.orElse("").split(",")));
        List<String> minPriceList = new ArrayList<>(Arrays.asList(minPrice.orElse("").split(",")));
        List<String> maxPriceList = new ArrayList<>(Arrays.asList(maxPrice.orElse("").split(",")));

        // Get books and create return books object
        List<Product> products = (List<Product>) productService.filterProducts((List<Product>) productService.getAllProduct(), "E-Book Reader");
        List<ElectronicBookReader> electronicBookReaders = new ArrayList<>();
        for (Product product : products) {
            electronicBookReaders.add((ElectronicBookReader) product);
        }

        sortProducts(electronicBookReaders, currentSort);

        float minP = Float.MAX_VALUE;
        float maxP = -1;

        // Find min and max prices
        for (Product p : electronicBookReaders) {
            float price = p.getPrice();
            if (price < minP) {
                minP = price;
            }
            if (price > maxP) {
                maxP = price;
            }
        }

        // Filter brands
        List<ElectronicBookReader> productsBrand = new ArrayList<>();
        if (!brandList.get(0).equals("")) {
            for (ElectronicBookReader electronicBookReader : electronicBookReaders) {
                for (String brand : brandList) {
                    if (electronicBookReader.getReaderBrand() != null) {
                        if (electronicBookReader.getReaderBrand().equals(brand)) {
                            productsBrand.add(electronicBookReader);
                        }
                    }
                }
            }
        } else {
            productsBrand.addAll(electronicBookReaders);
        }

        // Filter colors
        List<ElectronicBookReader> productsColor = new ArrayList<>();
        if (!colorList.get(0).equals("")) {
            for (ElectronicBookReader electronicBookReader : productsBrand) {
                for (String color : colorList) {
                    if (electronicBookReader.getReaderColor() != null) {
                        if (electronicBookReader.getReaderColor().equals(color)) {
                            productsColor.add(electronicBookReader);
                        }
                    }
                }
            }
        } else {
            productsColor.addAll(productsBrand);
        }


        // Filter discount
        List<ElectronicBookReader> productsDiscount = new ArrayList<>();
        if (!discountList.get(0).equals("")) {
            for (ElectronicBookReader electronicBookReader : productsColor) {
                for (String d : discountList) {
                    if (d.equals("No Discount") && !electronicBookReader.isOnDiscount()) {
                        productsDiscount.add(electronicBookReader);
                    } else if (d.equals("Discount") && electronicBookReader.isOnDiscount()) {
                        productsDiscount.add(electronicBookReader);
                    }
                }
            }
        } else {
            productsDiscount.addAll(productsColor);
        }

        // Filter stars and create star counts
        List<ElectronicBookReader> productsStar = new ArrayList<>();
        if (!starList.get(0).equals("")) {
            for (ElectronicBookReader electronicBookReader : productsDiscount) {
                for (String s : starList) {
                    double star = Double.parseDouble(s);

                    if (electronicBookReader.getScoreOutOf5() < star + 1 && electronicBookReader.getScoreOutOf5() >= star) {
                        productsStar.add(electronicBookReader);
                    }
                }
            }
        } else {
            productsStar.addAll(productsDiscount);
        }

        // Set min and max values if parameters given
        List<ElectronicBookReader> productsMin = new ArrayList<>();
        if (!minPriceList.get(0).equals("")) {
            minP = Float.parseFloat(minPriceList.get(0));
            for (ElectronicBookReader electronicBookReader : productsStar) {
                if (electronicBookReader.getPrice() >= minP) {
                    productsMin.add(electronicBookReader);
                }
            }
        } else {
            productsMin.addAll(productsStar);
        }

        List<ElectronicBookReader> productsMax = new ArrayList<>();
        if (!maxPriceList.get(0).equals("")) {
            maxP = Float.parseFloat(maxPriceList.get(0));
            for (ElectronicBookReader electronicBookReader : productsMin) {
                if (electronicBookReader.getPrice() <= maxP) {
                    productsMax.add(electronicBookReader);
                }
            }
        } else {
            productsMax.addAll(productsMin);
        }


        // Find counts of brands, colors and stars
        HashMap<String, Integer> brandCount = new HashMap<>();
        HashMap<String, Integer> colorCount = new HashMap<>();
        HashMap<String, Integer> starCount = new HashMap<>();

        for (ElectronicBookReader electronicBookReader : productsMax) {
            brandCount.merge(electronicBookReader.getReaderBrand(), 1, Integer::sum);
            colorCount.merge(electronicBookReader.getReaderColor(), 1, Integer::sum);

            if (starList.get(0).equals("")) {
                starList.remove(0);
                starList.add("0");
                starList.add("1");
                starList.add("2");
                starList.add("3");
                starList.add("4");
                starList.add("5");
            }
            for (String s : starList) {
                double star = Double.parseDouble(s);

                if (electronicBookReader.getScoreOutOf5() < star + 1 && electronicBookReader.getScoreOutOf5() >= star) {
                    starCount.merge(s, 1, Integer::sum);
                }
            }
        }
        Globals.getPageNumbers(page, size, productsMax, model, "ebookreaderProducts");
        model.addAttribute("sort", currentSort);
        model.addAttribute("brands", brandCount);
        model.addAttribute("colors", colorCount);
        model.addAttribute("stars", starCount);
        model.addAttribute("minPrice", minP);
        model.addAttribute("maxPrice", maxP);

        return "ebookreader";
    }

    private void sortProducts(List<ElectronicBookReader> products, String sort) {
        switch (sort) {
            case "date-desc":
                products.sort((o1, o2) -> (int) (o1.getUploadDate().getTime() - o2.getUploadDate().getTime()));
                Collections.reverse(products);
                break;

            case "date-asc":
                products.sort((o1, o2) -> (int) (o1.getUploadDate().getTime() - o2.getUploadDate().getTime()));
                break;

            case "name-desc":
                products.sort(Comparator.comparing(Product::getProductName).reversed());
                break;

            case "name-asc":
                products.sort(Comparator.comparing(Product::getProductName));
                break;

            case "price-desc":
                products.sort((o1, o2) -> Float.compare(o1.getPrice(), o2.getPrice()));  // Handle discount
                Collections.reverse(products);
                break;

            case "price-asc":
                products.sort((o1, o2) -> Float.compare(o1.getPrice(), o2.getPrice()));  // Handle discount
                break;

            case "rate-desc":
                products.sort(Comparator.comparingDouble(Product::getScoreOutOf5));
                Collections.reverse(products);
                break;

            case "rate-asc":
                products.sort(Comparator.comparingDouble(Product::getScoreOutOf5));
                break;

            default:
                products.sort((o1, o2) -> (int) (o1.getUploadDate().getTime() - o2.getUploadDate().getTime()));
                break;
        }
    }
}
