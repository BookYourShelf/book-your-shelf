package com.oak.bookyourshelf.controller.profile;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.AuthService;
import com.oak.bookyourshelf.service.profile.ProfileInformationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileInformationController {

    final ProfileInformationService profileInformationService;
    final AuthService authService;
    final PasswordEncoder passwordEncoder;




    public ProfileInformationController(ProfileInformationService profileInformationService, @Qualifier("customUserDetailsService") AuthService authService, PasswordEncoder passwordEncoder) {
        this.profileInformationService = profileInformationService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping(value = "/profile/information", method = RequestMethod.GET)
    public String showUser(Model model ) {

        User user =new User();
        String mail = authService.getUserDetails().getUsername();
        if(mail != null) {
            model.addAttribute("user", profileInformationService.getByEmail(mail));
            System.out.println(profileInformationService.getByEmail(mail).getUserId());
            System.out.println("bbnbnb");
            return "profile/_information";
        }
        return "/";
    }

    @RequestMapping(value = "/profile/information", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> UpdateInformations(@RequestParam String button,@RequestParam String newPassword, @RequestParam String currentPassword, @RequestParam String newPasswordAgain , User NewUser) {
        System.out.println("update");
        String mail = authService.getUserDetails().getUsername();
        User user = new User();
        if(mail != null) {
            user = profileInformationService.getByEmail(mail);
            System.out.println(user.getUserId());
            System.out.println("jhkjh");
        }
        else
            return ResponseEntity.badRequest().body("There is no logged in user");
        if(button.equals("updateInformations")) {

                user.setName(NewUser.getName());
                user.setSurname(NewUser.getSurname());

                if (!NewUser.getEmail().equals(user.getEmail())) {
                    if (profileInformationService.getByEmail(NewUser.getEmail()) == null) {
                        user.setEmail(NewUser.getEmail());
                    } else
                        return ResponseEntity.badRequest().body("This email address is already exist");
                }

            if (!NewUser.getBirthDate().equals(""))
                user.setBirthDate(NewUser.getBirthDate());
            else
                user.setBirthDate("null");
            if (!NewUser.getPhoneNumber().equals(""))
                user.setPhoneNumber(NewUser.getPhoneNumber());
            else
                user.setPhoneNumber(null);
            profileInformationService.save(user);

        }
        else
        {
            if (!newPassword.equals(newPasswordAgain)) {
                return ResponseEntity.badRequest().body("Passwords don't match. Please enter your password again.");
            } else {

                if (passwordEncoder.matches(newPasswordAgain, currentPassword)) {

                    return ResponseEntity.badRequest().body("New password can't match old password.Please enter a new password.");
                }

                user.setPassword(passwordEncoder.encode(newPasswordAgain));
                profileInformationService.save(user);

            }

        }
        return ResponseEntity.ok("");
    }
}
