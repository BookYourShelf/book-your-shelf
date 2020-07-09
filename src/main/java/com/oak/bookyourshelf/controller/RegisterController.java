package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.RegisterService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    final RegisterService registerService;

    public final PasswordEncoder passwordEncoder;

    public RegisterController(RegisterService registerService, PasswordEncoder passwordEncoder) {
        this.registerService = registerService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegisterPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(RedirectAttributes redirectAttributes, @ModelAttribute User user, Model model, HttpServletRequest request) {
        User dbUser = registerService.findByEmail(user.getEmail());

        if (dbUser != null) {
            user.setPassword("");
            model.addAttribute("lastUser", user);
//            model.addAttribute("name", user.getName());
//            model.addAttribute("surname", user.getSurname());
//            model.addAttribute("email", user.getEmail());
            return "register";
        }
        String pw = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(User.Roles.USER.getRole());
        registerService.save(user);

        try {
            request.login(user.getEmail(), pw);
        } catch (ServletException e) {
            System.out.println(e);
        }
        return "redirect:/";
    }
}