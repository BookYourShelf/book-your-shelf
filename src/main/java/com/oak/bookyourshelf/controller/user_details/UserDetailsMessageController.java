package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.model.Message;
import com.oak.bookyourshelf.service.user_details.UserDetailsMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserDetailsMessageController {

    final UserDetailsMessageService userDetailsMessageService;

    public UserDetailsMessageController(UserDetailsMessageService userDetailsMessageService) {
        this.userDetailsMessageService = userDetailsMessageService;
    }

    @RequestMapping(value = "/user-details/message/{id}", method = RequestMethod.GET)
    public String tab(Model model, @PathVariable int id) {

        List<Integer> receiver = new ArrayList<>(
                List.of(id));
        Message message = new Message();
        message.setReceivers(receiver);
        model.addAttribute("PersonalMessage", message);

        return "user_details/_message";
    }

    @RequestMapping(value = "/user-details/message/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveMessage(@RequestParam String submit, @PathVariable int id, Message PersonalMessage) {

        List<Integer> receiver = new ArrayList<>(
                List.of(id));

        switch (submit) {
            case "sms":
                PersonalMessage.setSms(true);
                PersonalMessage.setMail(false);
                break;
            case "mail":
                PersonalMessage.setSms(false);
                PersonalMessage.setMail(true);
                break;
            case "SmsAndMail":
                PersonalMessage.setSms(true);
                PersonalMessage.setMail(true);
                break;
        }

        PersonalMessage.setReceivers(receiver);
        userDetailsMessageService.save(PersonalMessage);
        return ResponseEntity.ok("");
    }
}
