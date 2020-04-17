package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.model.Message;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelMessageService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class AdminPanelMessageController {

    final AdminPanelMessageService adminPanelMessageService;


    public AdminPanelMessageController(AdminPanelMessageService adminPanelMessageService) {
        this.adminPanelMessageService = adminPanelMessageService;
    }

    @RequestMapping(value = "/admin-panel/message", method = RequestMethod.GET)
    public String tab(Model model) {
        Message message = new Message();
        model.addAttribute("adminmessage", message);
        return "admin_panel/_message";
    }


    @RequestMapping(value = "/admin-panel/message", method = RequestMethod.POST)
    public String sendMessage(@Valid @ModelAttribute Message adminmessage, @RequestParam("submit") String submit, Model model, @RequestParam("textarea") String input) {


        List<User> users = Collections.emptyList();
        /*user sevice ile user idlerini al*/
        Message message = new Message();

        message.setMessage(input);
        message.setSubject(adminmessage.getSubject());
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

        adminPanelMessageService.save(message);
        System.out.println("sms sended");
        return ("redirect:/admin-panel#message");
    }


}
