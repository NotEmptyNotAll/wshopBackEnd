package com.example.demo.DTO;

import java.util.Objects;

public class Order {
    private Integer id;
    private String orderNum;
    private Integer customerId;
    private String date;
    private String jobsSum;
    private String componentsSum;

    public Order() {
    }

    public Order(Integer id, String orderNum, Integer customerId, String date, String jobsSum, String componentsSum) {
        this.id = id;
        this.orderNum = orderNum;
        this.customerId = customerId;
        this.date = date;
        this.jobsSum = jobsSum;
        this.componentsSum = componentsSum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJobsSum() {
        return jobsSum;
    }

    public void setJobsSum(String jobsSum) {
        this.jobsSum = jobsSum;
    }

    public String getComponentsSum() {
        return componentsSum;
    }

    public void setComponentsSum(String componentsSum) {
        this.componentsSum = componentsSum;
    }
}
