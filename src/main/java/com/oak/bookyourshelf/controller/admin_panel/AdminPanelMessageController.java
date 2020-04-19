package com.oak.bookyourshelf.controller.admin_panel;

import com.oak.bookyourshelf.model.Message;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelMessageService;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelUserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
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
        return "admin_panel/_message";
    }

    @RequestMapping(value = "/admin-panel/message", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveMessage(@RequestParam String submit, Message message) {

        List<Integer> receiver = new ArrayList<>(
                List.of(1, 2, 3, 4, 5));
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

        message.setReceivers(receiver);
        adminPanelMessageService.save(message);
        return ResponseEntity.ok("");
    }


}
