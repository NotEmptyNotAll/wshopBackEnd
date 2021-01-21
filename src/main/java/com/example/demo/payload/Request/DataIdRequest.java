package com.example.demo.payload.Request;

import com.example.demo.DTO.User;

public class DataIdRequest {

    private String data;

    private Integer id;

    private String date;

    private User user;

    public DataIdRequest() {
    }

    public DataIdRequest(String data, Integer id, User user) {
        this.data = data;
        this.id = id;
        this.user = user;
    }

    public User getUser() {
        return user;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
