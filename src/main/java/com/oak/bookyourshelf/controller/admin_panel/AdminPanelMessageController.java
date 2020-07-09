package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.model.Message;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelMessageService;
import com.oak.bookyourshelf.service.user_details.UserDetailsInformationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

@Controller
public class AdminPanelMessageController {

    final AdminPanelMessageService adminPanelMessageService;
    final UserDetailsInformationService userDetailsInformationService;

    public AdminPanelMessageController(AdminPanelMessageService adminPanelMessageService, UserDetailsInformationService userDetailsInformationService) {
        this.adminPanelMessageService = adminPanelMessageService;
        this.userDetailsInformationService = userDetailsInformationService;
    }

    @RequestMapping(value = "/admin-panel/message", method = RequestMethod.GET)
    public String tab(Model model) {
        return "admin_panel/_message";
    }

    @RequestMapping(value = "/admin-panel/message", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveMessage(@RequestParam String submit, Message message) {

        if (userDetailsInformationService.findAllIds().isEmpty()) {
            return ResponseEntity.badRequest().body("There are no users.");
        } else {

            switch (submit) {
                case "sms":
                    message.setSms(true);
                    message.setMail(false);
                    break;
                case "mail":
                    message.setSms(false);
                    message.setMail(true);
                    break;
                case "SmsAndMail":
                    message.setSms(true);
                    message.setMail(true);
                    break;
            }

            message.setReceivers(userDetailsInformationService.findAllIds());
            adminPanelMessageService.save(message);
            return ResponseEntity.ok("");
        }
    }
}
