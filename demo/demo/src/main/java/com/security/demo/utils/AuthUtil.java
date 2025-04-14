package com.security.demo.utils;

import com.security.demo.model.User;
import com.security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public String getLoggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName()).orElseThrow(
                () -> new RuntimeException("User Not Found with username: " + authentication.getName())
        );
        return user.getEmail();
    }

    public Long getLoggedInUSerId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName()).orElseThrow(
                () -> new RuntimeException("User Not Found with username: " + authentication.getName())
        );
        return user.getUserId();
    }

    public User getLoggdInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userData = userRepository.findByUserName(authentication.getName()).orElseThrow(
                () -> new RuntimeException("User Not Found with username: " + authentication.getName())
        );
        return userData;
    }
}
