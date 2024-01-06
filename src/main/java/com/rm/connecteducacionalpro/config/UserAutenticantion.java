package com.rm.connecteducacionalpro.config;


import com.rm.connecteducacionalpro.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAutenticantion {

    public static User authenticated() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            return null;
        }
    }
}