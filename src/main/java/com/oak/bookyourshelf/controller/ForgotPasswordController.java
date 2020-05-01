package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Log;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.ForgotPasswordService;
import com.oak.bookyourshelf.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.UUID;

@Controller
public class ForgotPasswordController {

    final ForgotPasswordService forgetPasswordService;
    final LoginService loginService;

    public ForgotPasswordController(ForgotPasswordService forgetPasswordService, LoginService loginService) {
        this.forgetPasswordService = forgetPasswordService;
        this.loginService = loginService;
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public String showForgetPasswordPage(Model model) {
        return "forgot-password";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> forgetPassword(@RequestParam String email, Log log) {
        User existing = loginService.findByEmail(email);
        if (existing == null) {
            return ResponseEntity.badRequest().body("Wrong email address entered.Please enter the correct email address.");
        } else {
            log.setToken(UUID.randomUUID().toString());
            log.setEmail(email);
            log.setTime(new Timestamp(System.currentTimeMillis()));
            forgetPasswordService.save(log);
        }
        return ResponseEntity.ok("");
    }
}