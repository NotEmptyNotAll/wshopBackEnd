package com.example.demo.payload.Response;

public class SimpleDataResponse {
    private String name;
    private  Integer id;

    public SimpleDataResponse() {
    }

    public SimpleDataResponse(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
