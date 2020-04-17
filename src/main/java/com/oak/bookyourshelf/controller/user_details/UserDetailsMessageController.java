package com.oak.bookyourshelf.controller.user_details;

import com.oak.bookyourshelf.model.Message;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.user_details.UserDetailsMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    public String sendMessage(@Valid @ModelAttribute Message personalmessage, @RequestParam("submit") String submit, Model model, @RequestParam("textarea") String input) {

        List<Integer> receiver = new ArrayList<>(
                List.of(111));
        /*user idsi al*/


        personalmessage.setMessage(input);

        personalmessage.setReceivers(receiver);
        if (submit.equals("Send SMS")) {
            personalmessage.setMail(false);
            personalmessage.setSms(true);
        } else if (submit.equals("Send E-mail")) {
            personalmessage.setMail(true);
            personalmessage.setSms(false);
        } else {
            personalmessage.setMail(true);
            personalmessage.setSms(true);
        }

        userDetailsMessageService.save(personalmessage);
        System.out.println("sms sended");
        return ("redirect:/user-details#message");
    }
}
