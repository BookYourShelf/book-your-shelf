package com.oak.bookyourshelf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ThankYouController {

    @RequestMapping(value= "/thank-you", method = RequestMethod.GET)
    public String showThankYouPage(Model model) {
        return "thank-you";
    }

}
