package com.example.demo.DTO;

import java.util.Objects;

public class User {
    private Integer id;
    private String name;
    private String password;
    private Integer role;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name) {
        this.name = name;
    }


    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {
    }
    public User(Integer id, String name, Integer role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public User(Integer id, String name, String password, Integer role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
