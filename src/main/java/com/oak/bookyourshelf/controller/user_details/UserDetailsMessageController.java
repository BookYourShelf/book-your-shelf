package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.model.Message;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class UserDetailsMessageController {


    final UserDetailsMessageService userDetailsMessageService;

    public UserDetailsMessageController(UserDetailsMessageService userDetailsMessageService) {
        this.userDetailsMessageService = userDetailsMessageService;
    }


    @RequestMapping(value = "/user-details/message", method = RequestMethod.GET)
    public String tab(Model model) {
        Message message = new Message();
        model.addAttribute("personalmessage", message);
        return "user_details/_message";
    }


    @RequestMapping(value = "/user-details/message", method = RequestMethod.POST)
    public String sendMessage(@Valid @ModelAttribute Message personalmessage,@RequestParam("submit") String submit, Model model, @RequestParam("textarea") String input) {


        /*user idsi al*/
        Message message = new Message();

        message.setMessage(input);
        message.setSubject(personalmessage.getSubject());
        if (submit.equals("Send SMS")) {
            message.setMail(false);
            message.setSms(true);
        } else if (submit.equals("Send E-mail")) {
            message.setMail(true);
            message.setSms(false);
        } else {
            message.setMail(true);
            message.setSms(true);
        }

        userDetailsMessageService.save(message);
        System.out.println("sms sended");
        return ("redirect:/user-details#message");
    }
}
