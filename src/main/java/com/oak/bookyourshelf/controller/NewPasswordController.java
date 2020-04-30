package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.Log;
import com.oak.bookyourshelf.model.PasswordChange;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.LoginService;
import com.oak.bookyourshelf.service.NewPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;

@Controller
public class NewPasswordController {

    final NewPasswordService newPasswordService;
    final LoginService loginService;
    public final PasswordEncoder passwordEncoder;
    String tokens;

    public NewPasswordController(NewPasswordService newPasswordService, LoginService loginService, PasswordEncoder passwordEncoder) {
        this.newPasswordService = newPasswordService;
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/new-password", method = RequestMethod.GET)
    public String showNewPasswordPage(@RequestParam("token") String token, Model model) {
        Log log = newPasswordService.findByToken(token);

        if (log == null) {
            model.addAttribute("error", "Couldn't find token");
            return "redirect:/forgot-password";
        } else {
            /*Link Expiration Check in minutes*/
            long currInMilliSecond = new Timestamp(System.currentTimeMillis()).getTime();
            long timeInMilliSecond = log.getTime().getTime();
            int expire = (int) ((currInMilliSecond - timeInMilliSecond) / (60 * 1000));

            if (expire > 60) {
                model.addAttribute("error", "Token has expired");
                return "redirect:/forgot-password";
            } else if (log.getToken() == null) {
                return "redirect:/forgot-password";
            } else {
                model.addAttribute("token", token);
                tokens = token;
            }
        }
        return "new-password";
    }

    @RequestMapping(value = "/new-password", method = RequestMethod.POST)
    public ResponseEntity<String> setNewPassword(@RequestParam String newPassword, @RequestParam String confirmPassword, PasswordChange passwordChange) {
        Log log = newPasswordService.findByToken(tokens);
        User user = loginService.findByEmail(log.getEmail());

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("Passwords don't match. Please enter your password again.");
        } else {
            if (passwordEncoder.matches(confirmPassword, user.getPassword())) {

                return ResponseEntity.badRequest().body("New password can't match old password.Please enter a new password.");
            }
            log.setToken(null);
            user.setPassword(passwordEncoder.encode(confirmPassword));
            newPasswordService.save(log);
            loginService.save(user);
        }
        return ResponseEntity.ok("");
    }
}
