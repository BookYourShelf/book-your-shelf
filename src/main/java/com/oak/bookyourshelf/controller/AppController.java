package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.service.LoginService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AppController {
    User loggedInUser;

    final LoginService loginService;

    public AppController(LoginService userService) {
        this.loginService = userService;
    }

    User getLoginUser() {
        if (loggedInUser == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            loggedInUser = loginService.findByEmail(auth.getName());
        }
        return loggedInUser;
    }
}
