package com.devinberkani.blogpress.controller;

import com.devinberkani.blogpress.dto.RegistrationDto;
import com.devinberkani.blogpress.service.UserService;
import jakarta.validation.Valid;
import com.devinberkani.blogpress.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // handler method to handle user login request

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        // this object holds form data
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submission request
    @PostMapping("/register/save")
    public String submitRegistrationForm(@Valid @ModelAttribute("user") RegistrationDto user, BindingResult result, Model model) {
        User existingUser = userService.findByEmail(user.getEmail()); // represents an existing user IF email already exists
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) { // if existing user is NOT empty, that means it must match, throw an error letting visitor know email is already in use
            result.rejectValue("email", null, "There is already a user with same email id");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success"; // passing ?success parameter
    }

}
