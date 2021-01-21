package com.example.demo.payload.Request;

public class CustomerRequest {
    private String name;

    private Integer sizeResponse;

    public CustomerRequest() {
    }

    public CustomerRequest(String name, Integer sizeResponse) {
        this.name = name;
        this.sizeResponse = sizeResponse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSizeResponse() {
        return sizeResponse;
    }

    public void setSizeResponse(Integer sizeResponse) {
        this.sizeResponse = sizeResponse;
    }
}
