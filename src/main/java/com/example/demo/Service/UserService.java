package com.example.demo.Service;

import com.example.demo.DTO.User;

import java.util.List;

public interface UserService {

    List<User> getUser();

    boolean login(User user);
}
