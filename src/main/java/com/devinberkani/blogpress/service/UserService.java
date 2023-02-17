package com.devinberkani.blogpress.service;

import com.devinberkani.blogpress.dto.RegistrationDto;
import com.devinberkani.blogpress.entity.User;

public interface UserService {

    void saveUser(RegistrationDto registrationDto);

    User findByEmail(String email);
}
