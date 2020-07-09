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
public class ElectronicBookReaderCaseController {
    final ProductService productService;

    public ElectronicBookReaderCaseController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/ebookreadercases", method = RequestMethod.GET)
    public String showCategory(@RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               @RequestParam("brands") Optional<String> brands,
                               @RequestParam("readerbrands") Optional<String> readerbrands,
                               @RequestParam("readermodels") Optional<String> readermodels,
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
        List<String> readerbrandList = new ArrayList<>(Arrays.asList(readerbrands.orElse("").split(",")));
        List<String> readermodelList = new ArrayList<>(Arrays.asList(readermodels.orElse("").split(",")));
        List<String> colorList = new ArrayList<>(Arrays.asList(colors.orElse("").split(",")));
        List<String> discountList = new ArrayList<>(Arrays.asList(discount.orElse("").split(",")));
        List<String> starList = new ArrayList<>(Arrays.asList(stars.orElse("").split(",")));
        List<String> minPriceList = new ArrayList<>(Arrays.asList(minPrice.orElse("").split(",")));
        List<String> maxPriceList = new ArrayList<>(Arrays.asList(maxPrice.orElse("").split(",")));

        // Get books and create return books object
        List<Product> products = (List<Product>) productService.filterProducts((List<Product>) productService.getAllProduct(), "E-Book Reader Case");
        List<ElectronicBookReaderCase> electronicBookReaderCases = new ArrayList<>();
        for (Product product : products) {
            electronicBookReaderCases.add((ElectronicBookReaderCase) product);
        }

        sortProducts(electronicBookReaderCases, currentSort);

        float minP = Float.MAX_VALUE;
        float maxP = -1;

        // Find min and max prices
        for (Product p : electronicBookReaderCases) {
            float price = p.getPrice();
            if (price < minP) {
                minP = price;
            }
            if (price > maxP) {
                maxP = price;
            }
        }

        // Filter brands
        List<ElectronicBookReaderCase> productsBrand = new ArrayList<>();
        if (!brandList.get(0).equals("")) {
            for (ElectronicBookReaderCase electronicBookReaderCase : electronicBookReaderCases) {
                for (String brand : brandList) {
                    if (electronicBookReaderCase.getCaseBrand() != null) {
                        if (electronicBookReaderCase.getCaseBrand().equals(brand)) {
                            productsBrand.add(electronicBookReaderCase);
                        }
                    }
                }
            }
        } else {
            productsBrand.addAll(electronicBookReaderCases);
        }

        // Filter reader brands
        List<ElectronicBookReaderCase> productsReaderBrand = new ArrayList<>();
        if (!readerbrandList.get(0).equals("")) {
            for (ElectronicBookReaderCase electronicBookReaderCase : productsBrand) {
                for (String readerbrand : readerbrandList) {
                    if (electronicBookReaderCase.getReaderBrand() != null) {
                        if (electronicBookReaderCase.getReaderBrand().equals(readerbrand)) {
                            productsReaderBrand.add(electronicBookReaderCase);
                        }
                    }
                }
            }
        } else {
            productsReaderBrand.addAll(productsBrand);
        }

        // Filter reader models
        List<ElectronicBookReaderCase> productsReaderModel = new ArrayList<>();
        if (!readermodelList.get(0).equals("")) {
            for (ElectronicBookReaderCase electronicBookReaderCase : productsReaderBrand) {
                for (String readermodel : readermodelList) {
                    if (electronicBookReaderCase.getReaderModel() != null) {
                        if (electronicBookReaderCase.getReaderModel().equals(readermodel)) {
                            productsReaderModel.add(electronicBookReaderCase);
                        }
                    }
                }
            }
        } else {
            productsReaderModel.addAll(productsReaderBrand);
        }

        // Filter colors
        List<ElectronicBookReaderCase> productsColor = new ArrayList<>();
        if (!colorList.get(0).equals("")) {
            for (ElectronicBookReaderCase electronicBookReaderCase : productsReaderModel) {
                for (String color : colorList) {
                    if (electronicBookReaderCase.getCaseColor() != null) {
                        if (electronicBookReaderCase.getCaseColor().equals(color)) {
                            productsColor.add(electronicBookReaderCase);
                        }
                    }
                }
            }
        } else {
            productsColor.addAll(productsReaderModel);
        }

        // Filter discount
        List<ElectronicBookReaderCase> productsDiscount = new ArrayList<>();
        if (!discountList.get(0).equals("")) {
            for (ElectronicBookReaderCase electronicBookReaderCase : productsColor) {
                for (String d : discountList) {
                    if (d.equals("No Discount") && !electronicBookReaderCase.isOnDiscount()) {
                        productsDiscount.add(electronicBookReaderCase);
                    } else if (d.equals("Discount") && electronicBookReaderCase.isOnDiscount()) {
                        productsDiscount.add(electronicBookReaderCase);
                    }
                }
            }
        } else {
            productsDiscount.addAll(productsColor);
        }

        // Filter stars and create star counts
        List<ElectronicBookReaderCase> productsStar = new ArrayList<>();
        if (!starList.get(0).equals("")) {
            for (ElectronicBookReaderCase electronicBookReaderCase : productsDiscount) {
                for (String s : starList) {
                    double star = Double.parseDouble(s);

                    if (electronicBookReaderCase.getScoreOutOf5() < star + 1 && electronicBookReaderCase.getScoreOutOf5() >= star) {
                        productsStar.add(electronicBookReaderCase);
                    }
                }
            }
        } else {
            productsStar.addAll(productsDiscount);
        }

        // Set min and max values if parameters given
        List<ElectronicBookReaderCase> productsMin = new ArrayList<>();
        if (!minPriceList.get(0).equals("")) {
            minP = Float.parseFloat(minPriceList.get(0));
            for (ElectronicBookReaderCase electronicBookReaderCase : productsStar) {
                if (electronicBookReaderCase.getPrice() >= minP) {
                    productsMin.add(electronicBookReaderCase);
                }
            }
        } else {
            productsMin.addAll(productsStar);
        }

        List<ElectronicBookReaderCase> productsMax = new ArrayList<>();
        if (!maxPriceList.get(0).equals("")) {
            maxP = Float.parseFloat(maxPriceList.get(0));
            for (ElectronicBookReaderCase electronicBookReaderCase : productsMin) {
                if (electronicBookReaderCase.getPrice() <= maxP) {
                    productsMax.add(electronicBookReaderCase);
                }
            }
        } else {
            productsMax.addAll(productsMin);
        }


        // Find counts of languages, publishers and authors
        HashMap<String, Integer> brandCount = new HashMap<>();
        HashMap<String, Integer> readerbrandCount = new HashMap<>();
        HashMap<String, Integer> readermodelCount = new HashMap<>();
        HashMap<String, Integer> colorCount = new HashMap<>();
        HashMap<String, Integer> starCount = new HashMap<>();

        for (ElectronicBookReaderCase electronicBookReaderCase : productsMax) {
            brandCount.merge(electronicBookReaderCase.getCaseBrand(), 1, Integer::sum);
            readerbrandCount.merge(electronicBookReaderCase.getReaderBrand(), 1, Integer::sum);
            readermodelCount.merge(electronicBookReaderCase.getReaderModel(), 1, Integer::sum);
            colorCount.merge(electronicBookReaderCase.getCaseColor(), 1, Integer::sum);

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

                if (electronicBookReaderCase.getScoreOutOf5() < star + 1 && electronicBookReaderCase.getScoreOutOf5() >= star) {
                    starCount.merge(s, 1, Integer::sum);
                }
            }
        }
        Globals.getPageNumbers(page, size, productsMax, model, "ebookreadercaseProducts");
        model.addAttribute("sort", currentSort);
        model.addAttribute("brands", brandCount);
        model.addAttribute("readerbrands", readerbrandCount);
        model.addAttribute("readermodels", readermodelCount);
        model.addAttribute("colors", colorCount);
        model.addAttribute("stars", starCount);
        model.addAttribute("minPrice", minP);
        model.addAttribute("maxPrice", maxP);

        return "ebookreadercase";
    }

    private void sortProducts(List<ElectronicBookReaderCase> products, String sort) {
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
