package com.devinberkani.blogpress.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class SecurityUtils {

    public static User getCurrentUser() {
        // from spring security - contains username, password and roles
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle instanceof User) {
            return (User) principle; // if the principle is an instance of User, cast principle to User and return it, otherwise return null
        }
        return null;
    }

    /*
    Retrieves the current user's role by first calling the getCurrentUser method to retrieve the current user, and then accessing the user's authorities property, which represents the roles assigned to the user. The method uses a for-each loop to iterate through the authorities collection, and returns the authority of the first item in the collection.
    In this code, GrantedAuthority is an interface provided by the Spring Security framework that represents the role assigned to a user. The getAuthority method returns the string representation of the role, which is typically stored as a string in the database.
    */

    public static String getRole() {
        User user = getCurrentUser();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            return authority.getAuthority();
        }
        return null;
    }

}
