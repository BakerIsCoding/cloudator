package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.entity.UserAccess;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    User findByUsername(String username);

    List<UserDto> findAllUsers();

    UserAccess findById(Integer id);

    //Antiguo, revisar:

    User getUserById(Long id);

    void deleteUser(Long id);

    User fetchUser(String user);

    
}
