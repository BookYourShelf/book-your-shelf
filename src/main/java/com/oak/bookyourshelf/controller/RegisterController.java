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
    private static byte[] key;
    private static SecretKeySpec secretKey;

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
    public ResponseEntity<String> registerUser(@RequestParam String email, User user) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        if (email != null) {
            User existing = registerService.findByEmail(email);
            if (existing != null) {
                return ResponseEntity.badRequest().body("This email address already exists. Please try another one.");
            }
        }

        prepareSecreteKey("secrete");
        Cipher cipher =Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        String newPassword= Base64.getEncoder().encodeToString(cipher.doFinal(user.getPassword().getBytes("UTF-8")));

        user.setPassword(newPassword);


        registerService.save(user);
        return ResponseEntity.ok("");
    }

    public void prepareSecreteKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            key = MessageDigest.getInstance("SHA-1").digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}