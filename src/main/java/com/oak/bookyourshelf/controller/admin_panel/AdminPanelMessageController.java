package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.model.Message;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelMessageService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
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


        List<Integer> receiver = new ArrayList<>(
                List.of(1, 2, 3, 4));
        /*user sevice ile user idlerini al*/

        adminmessage.setMessage(input);
        adminmessage.setReceivers(receiver);
        if (submit.equals("Send SMS")) {
            adminmessage.setMail(false);
            adminmessage.setSms(true);
        } else if (submit.equals("Send E-mail")) {

            adminmessage.setMail(true);
            adminmessage.setSms(false);
        } else {

            adminmessage.setMail(true);
            adminmessage.setSms(true);
        }


        adminPanelMessageService.save(adminmessage);

        System.out.println("sms sended");
        return ("redirect:/admin-panel#message");
    }


}
