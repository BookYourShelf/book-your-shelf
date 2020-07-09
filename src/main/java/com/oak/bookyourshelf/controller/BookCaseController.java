package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Book;
import com.oak.bookyourshelf.model.BookCase;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class BookCaseController {
    final ProductService productService;

    public BookCaseController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/bookcases", method = RequestMethod.GET)
    public String showCategory(@RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               @RequestParam("sort") Optional<String> sort,
                               @RequestParam("brands") Optional<String> brands,
                               @RequestParam("colors") Optional<String> colors,
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
        List<Product> products = productService.filterProducts((List<Product>) productService.getAllProduct(), "Book Case");
        List<BookCase> bookCases = new ArrayList<>();
        for (Product product : products) {
            bookCases.add((BookCase) product);
        }


        sortProducts(bookCases, currentSort);

        float minP = Float.MAX_VALUE;
        float maxP = -1;

        // Find min and max prices
        for (Product p : bookCases) {
            float price = p.getPrice();
            if (price < minP) {
                minP = price;
            }
            if (price > maxP) {
                maxP = price;
            }
        }

        // Filter brands
        List<BookCase> productsBrand = new ArrayList<>();
        if (!brandList.get(0).equals("")) {
            for (BookCase bookCase : bookCases) {
                for (String brand : brandList) {
                    if (bookCase.getCaseBrand() != null) {
                        if (bookCase.getCaseBrand().equals(brand)) {
                            productsBrand.add(bookCase);
                        }
                    }
                }
            }
        } else {
            productsBrand.addAll(bookCases);
        }

        // Filter colors
        List<BookCase> productsColor = new ArrayList<>();
        if (!colorList.get(0).equals("")) {
            for (BookCase bookCase : productsBrand) {
                for (String color : colorList) {
                    if (bookCase.getCaseColor() != null) {
                        if (bookCase.getCaseColor().equals(color)) {
                            productsColor.add(bookCase);
                        }
                    }
                }
            }
        } else {
            productsColor.addAll(productsBrand);
        }

        // Filter discount
        List<BookCase> productsDiscount = new ArrayList<>();
        if (!discountList.get(0).equals("")) {
            for (BookCase bookCase : productsColor) {
                for (String d : discountList) {
                    if (d.equals("No Discount") && !bookCase.isOnDiscount()) {
                        productsDiscount.add(bookCase);
                    } else if (d.equals("Discount") && bookCase.isOnDiscount()) {
                        productsDiscount.add(bookCase);
                    }
                }
            }
        } else {
            productsDiscount.addAll(productsColor);
        }

        // Filter stars and create star counts
        List<BookCase> productsStar = new ArrayList<>();
        if (!starList.get(0).equals("")) {
            for (BookCase bookCase : productsDiscount) {
                for (String s : starList) {
                    double star = Double.parseDouble(s);

                    if (bookCase.getScoreOutOf5() < star + 1 && bookCase.getScoreOutOf5() >= star) {
                        productsStar.add(bookCase);
                    }
                }
            }
        } else {
            productsStar.addAll(productsDiscount);
        }

        // Set min and max values if parameters given
        List<BookCase> productsMin = new ArrayList<>();
        if (!minPriceList.get(0).equals("")) {
            minP = Float.parseFloat(minPriceList.get(0));
            for (BookCase bookCase : productsStar) {
                if (bookCase.getPrice() >= minP) {
                    productsMin.add(bookCase);
                }
            }
        } else {
            productsMin.addAll(productsStar);
        }

        List<BookCase> productsMax = new ArrayList<>();
        if (!maxPriceList.get(0).equals("")) {
            maxP = Float.parseFloat(maxPriceList.get(0));
            for (BookCase bookCase : productsMin) {
                if (bookCase.getPrice() <= maxP) {
                    productsMax.add(bookCase);
                }
            }
        } else {
            productsMax.addAll(productsMin);
        }


        // Find counts of brands, colors and stars
        HashMap<String, Integer> brandCount = new HashMap<>();
        HashMap<String, Integer> colorCount = new HashMap<>();
        HashMap<String, Integer> starCount = new HashMap<>();

        for (BookCase bookCase : productsMax) {
            brandCount.merge(bookCase.getCaseBrand(), 1, Integer::sum);
            colorCount.merge(bookCase.getCaseColor(), 1, Integer::sum);

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

                if (bookCase.getScoreOutOf5() < star + 1 && bookCase.getScoreOutOf5() >= star) {
                    starCount.merge(s, 1, Integer::sum);
                }
            }
        }
        Globals.getPageNumbers(page, size, productsMax, model, "bookcaseProducts");
        model.addAttribute("sort", currentSort);
        model.addAttribute("brands", brandCount);
        model.addAttribute("colors", colorCount);
        model.addAttribute("stars", starCount);
        model.addAttribute("minPrice", minP);
        model.addAttribute("maxPrice", maxP);

        return "bookcase";
    }

    private void sortProducts(List<BookCase> products, String sort) {
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
