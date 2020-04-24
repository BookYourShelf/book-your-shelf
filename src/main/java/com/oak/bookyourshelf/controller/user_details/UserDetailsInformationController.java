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
    public String showUser(Model model, @PathVariable int id) {

        User user = userDetailsInformationService.get(id);
        model.addAttribute("user", user);

        System.out.println("bbnbnb");
        return "user_details/_information";
    }

    @RequestMapping(value = "/user-details/information/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> UserUpdate(@RequestParam String button, @PathVariable int id, User newUser) {

        System.out.println(button);

        User user = userDetailsInformationService.get(id);

        System.out.println(user.getName());
        switch (button) {
            case "updateUser":
                if (newUser.getName().equals("") || newUser.getName() == null) {
                    return ResponseEntity.badRequest().body("User name cannot be empty");
                } else
                    user.setName(user.getName());
                if (newUser.getSurname().equals("") || newUser.getSurname() == null) {
                    return ResponseEntity.badRequest().body("User surname cannot be empty");
                } else // TODO: Buna dikkat et
                    user.setSurname(newUser.getSurname());
                if (newUser.getEmail().equals("")) {
                    return ResponseEntity.badRequest().body("User email cannot be empty");
                } else {
                    if (!user.getEmail().equals(newUser.getEmail())) {
                        if (userDetailsInformationService.getByEmail(newUser.getEmail()) == null) {
                            user.setEmail(newUser.getEmail());
                        } else
                            return ResponseEntity.badRequest().body("This email address is already exist");
                    }
                }
                if (newUser.getBirthDate() != null)
                    user.setBirthDate(newUser.getBirthDate());
                else // TODO: Bunlari silmeyebilirsin
                    user.setBirthDate(null);
                if (newUser.getPhoneNumber() != null)
                    user.setPhoneNumber(newUser.getPhoneNumber());
                else
                    user.setPhoneNumber(null);
                if (newUser.getPassword() != null)
                    user.setPassword(passwordEncoder.encode(newUser.getPassword()));


                break;
            case "deleteUser":
                userDetailsInformationService.delete(newUser.getUserId());
                break;
        }
        userDetailsInformationService.save(user); // Update
        return ResponseEntity.ok("");
    }


}
