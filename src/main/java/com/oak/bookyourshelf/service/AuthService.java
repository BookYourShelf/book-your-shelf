package com.oak.bookyourshelf.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("customUserDetailsService")
public class AuthService implements UserDetailsService {

    final LoginService loginService;

    public AuthService(LoginService userService) {
        this.loginService = userService;
    }

    public UserDetails getUserDetails() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj instanceof UserDetails) {
            return (UserDetails) obj;
        } else {
            return null;
        }
    }

    public List<GrantedAuthority> getAuthorities(Integer role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if (role == com.oak.bookyourshelf.model.User.Roles.USER.getRole()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (role == com.oak.bookyourshelf.model.User.Roles.ADMIN.getRole()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        /* TODO: expire, credientials expire and lock can be added to user */
        com.oak.bookyourshelf.model.User user = loginService.findByEmail(s);
        if (user != null) {
            return new User(user.getEmail(), user.getPassword(), getAuthorities(user.getRole()));
        }
        throw new UsernameNotFoundException("Username not found");
    }
}
