package com.rm.connecteducacionalpro.util;

import com.rm.connecteducacionalpro.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAutentication {

    public static User authenticated() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            return null;
        }
    }
}
