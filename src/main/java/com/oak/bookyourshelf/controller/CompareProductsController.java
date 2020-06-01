package com.oak.bookyourshelf.controller;


import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.service.CompareProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CompareProductsController {
    final CompareProductsService compareProductsService;

    public CompareProductsController(CompareProductsService compareProductsService) {
        this.compareProductsService = compareProductsService;
    }

    @RequestMapping(value = "/compare/{id1}/{id2}", method = RequestMethod.GET)
    public String tab(@PathVariable int id1 , @PathVariable int id2, Model model) {

        System.out.println(id1);
        System.out.println(id2);
        Product p = compareProductsService.get(id1);
        Product p2= compareProductsService.get(id2);
        System.out.println(p.getProductName());
        System.out.println(p2.getProductName());
        model.addAttribute("mainProduct",p);
        model.addAttribute("comparedProduct",p2);

        return "compare";
    }
}
