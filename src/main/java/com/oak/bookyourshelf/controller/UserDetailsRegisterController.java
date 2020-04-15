package com.oak.bookyourshelf.controller;


import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.UserDetailsRegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.Timestamp;

@Controller
public class UserDetailsRegisterController {

    final
    UserDetailsRegisterService registerService;

    public UserDetailsRegisterController(UserDetailsRegisterService registerService) { this.registerService = registerService; }

    @RequestMapping("/register")
    public String showRegisterPage(Model model){
        User newUser = new User();
        model.addAttribute("newUser",newUser);
        return "register";
    }

    @RequestMapping(value = "/register", method= RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("newUser") User newUser, BindingResult error, Model model){
        User user = new User();
        User existing = registerService.findByEmail(newUser.getEmail());
        if (existing != null) {
            error.rejectValue("email", null, "There is already an account registered with that email");
        }

        if(!error.hasErrors()){
            user.setName(newUser.getName());
            user.setSurname(newUser.getSurname());
            user.setEmail(newUser.getEmail());
            registerService.save(user);
            return "index";
        }
        return "register";
    }






}
