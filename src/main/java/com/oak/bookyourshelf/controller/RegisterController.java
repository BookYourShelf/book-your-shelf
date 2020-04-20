package com.oak.bookyourshelf.controller;


import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;


@Controller
public class RegisterController {

    final
    RegisterService registerService;

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
    public String registerUser(RedirectAttributes redirectAttributes, @ModelAttribute User user, Model model) {
        User dbUser = registerService.findByEmail(user.getEmail());

        if (dbUser != null) {
            user.setPassword("");
            model.addAttribute("lastUser", user);
//            model.addAttribute("name", user.getName());
//            model.addAttribute("surname", user.getSurname());
//            model.addAttribute("email", user.getEmail());
            return "/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(User.Roles.USER.getRole());
        registerService.save(user);
        return "redirect:/";
    }
}