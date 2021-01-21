package com.example.demo.Controller;

import com.example.demo.DTO.User;
import com.example.demo.Service.OrdersService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/login", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Boolean login(@RequestBody User user) {
        return userService.login(user);
    }

    @GetMapping("/getListUser")
    public List<User> getListUser() {
        try {
            return userService.getUser();

        }catch (NullPointerException e){
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }
}
