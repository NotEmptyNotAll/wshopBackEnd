package com.example.demo.DTO;

public class OrdersTableColumn {

    private String nameColumn;

    private Integer width;


    public OrdersTableColumn() {
    }

    public OrdersTableColumn(String nameColumn, Integer width) {
        this.nameColumn = nameColumn;
        this.width = width;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
