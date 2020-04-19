package com.oak.bookyourshelf.controller;


import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


@Controller
public class RegisterController {

    final
    RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegisterPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestParam String email, User user) {
        if (email != null) {
            User existing = registerService.findByEmail(email);
            if (existing != null) {
                return ResponseEntity.badRequest().body("This email address already exists. Please try another one.");
            }
        }
        registerService.save(user);
        return ResponseEntity.ok("");
    }
}