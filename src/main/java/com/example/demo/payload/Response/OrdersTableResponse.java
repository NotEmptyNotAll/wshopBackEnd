package com.example.demo.payload.Response;


import com.example.demo.DTO.OrdersTableColumn;
import com.example.demo.DTO.OrdersTableRow;

import java.util.ArrayList;
import java.util.List;

public class OrdersTableResponse {
    private List<OrdersTableColumn> ordersTableColumns;
    private List<OrdersTableRow> ordersTableBody;
    private Integer status;

    public OrdersTableResponse() {
    }

    public OrdersTableResponse(List<OrdersTableColumn> ordersTableColumns, List<OrdersTableRow> ordersTableBody) {
        this.ordersTableColumns = ordersTableColumns;
        this.ordersTableBody = ordersTableBody;
    }

    public OrdersTableResponse(Integer status) {
        this.ordersTableBody=new ArrayList<OrdersTableRow>(){{
            add(new OrdersTableRow());
        }};
        this.ordersTableColumns=new ArrayList<OrdersTableColumn>(){{
            add(new OrdersTableColumn());
        }};
        this.status = status;
    }

    public List<OrdersTableColumn> getColumnTables() {
        return ordersTableColumns;
    }

    public void setColumnTables(List<OrdersTableColumn> ordersTableColumns) {
        this.ordersTableColumns = ordersTableColumns;
    }


    public List<OrdersTableColumn> getOrdersTableColumns() {
        return ordersTableColumns;
    }

    public void setOrdersTableColumns(List<OrdersTableColumn> ordersTableColumns) {
        this.ordersTableColumns = ordersTableColumns;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OrdersTableRow> getOrdersTableBody() {
        return ordersTableBody;
    }

    public void setOrdersTableBody(List<OrdersTableRow> ordersTableBody) {
        this.ordersTableBody = ordersTableBody;
    }

    @Override
    public String toString() {
        return "OrdersTableResponse{" +
                "ordersTableColumns=" + ordersTableColumns +
                ", ordersTableBody=" + ordersTableBody +
                ", status=" + status +
                '}';
    }
}
