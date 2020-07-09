package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserDetailsInformationController {

    final UserDetailsInformationService userDetailsInformationService;
    public final PasswordEncoder passwordEncoder;

    public UserDetailsInformationController(UserDetailsInformationService userDetailsInformationService, PasswordEncoder passwordEncoder) {
        this.userDetailsInformationService = userDetailsInformationService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/user-details/information/{id}", method = RequestMethod.GET)
    public String tab(Model model, @PathVariable int id) {

        User user = userDetailsInformationService.get(id);
        model.addAttribute("user", user);

        return "user_details/_information";
    }

    @RequestMapping(value = "/user-details/information/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> UserUpdate(@RequestParam String button, @PathVariable int id, User newUser) {

        User user = userDetailsInformationService.get(id);

        switch (button) {
            case "updateUser":
                user.setName(newUser.getName());
                user.setSurname(newUser.getSurname());

                if (!user.getEmail().equals(newUser.getEmail())) {
                    if (userDetailsInformationService.getByEmail(newUser.getEmail()) == null) {
                        user.setEmail(newUser.getEmail());
                    } else {
                        return ResponseEntity.badRequest().body("This email address is already exist");
                    }
                }

                if (!newUser.getBirthDate().equals("")) {
                    user.setBirthDate(newUser.getBirthDate());
                } else {
                    user.setBirthDate(null);
                }

                if (!newUser.getPhoneNumber().equals("")) {
                    user.setPhoneNumber(newUser.getPhoneNumber());
                } else {
                    user.setPhoneNumber(null);
                }

                if (newUser.getPassword() != null) {
                    user.setPassword(passwordEncoder.encode(newUser.getPassword()));
                }

                user.setUserId(id);
                userDetailsInformationService.save(user);
                break;

            case "deleteUser":
                userDetailsInformationService.delete(user.getUserId());
                break;
        }
        return ResponseEntity.ok("");
    }
}
