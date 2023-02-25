package com.devinberkani.blogpress.security;

import com.devinberkani.blogpress.repository.UserRepository;
import com.devinberkani.blogpress.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


/*

    This code defines a custom implementation of the UserDetailsService interface in Spring Security. This implementation is used to retrieve user information and perform authentication for a web application.

    Here's a breakdown of what the code is doing:

    Defining a service: The code defines a Spring service called CustomUserDetailsService. This service implements the UserDetailsService interface in Spring Security.

    Retrieving user information: The loadUserByUsername() method is implemented to retrieve user information from a UserRepository object. The userRepository.findByEmail(email) method is called to retrieve a user by their email address.

    Creating an authenticated user object: If the user is found in the repository, the method creates an authenticated user object of the UserDetails interface, which is returned to Spring Security for authentication. The authenticated user object is created using the User class from Spring Security and the user's email address, password, and roles.

    Mapping user roles: The user's roles are mapped to SimpleGrantedAuthority objects, which are added to the authenticated user object.

    Handling errors: If the user is not found in the repository, the method throws a UsernameNotFoundException with an error message.

    In summary, this code defines a custom implementation of the UserDetailsService interface in Spring Security that retrieves user information from a repository, creates an authenticated user object, maps user roles, and handles errors. This implementation can be used to perform authentication in a web application.

*/

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            org.springframework.security.core.userdetails.User authenticatedUser =
                    new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            user.getRoles().stream()
                                    .map((role) -> new SimpleGrantedAuthority(role.getName()))
                                    .collect(Collectors.toList())
                    );
            return authenticatedUser;
        } else {
            throw new UsernameNotFoundException("Invalid username and password");
        }
    }
}
