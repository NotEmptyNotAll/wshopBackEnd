package com.example.demo.DTO;

import java.util.ArrayList;
import java.util.List;

public class OrdersTableRow {
    private List<CellData> rowData;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OrdersTableRow() {
        this.rowData=new ArrayList<CellData>(){{
            add(new CellData());
        }};
    }

    public OrdersTableRow(List<CellData> rowData, String comment) {
        this.rowData = rowData;
        this.comment = comment;
    }

    public OrdersTableRow(List<CellData> rowData) {
        this.rowData = rowData;
    }

    public List<CellData> getRowData() {
        return rowData;
    }

    public void setRowData(List<CellData> rowData) {
        this.rowData = rowData;
    }
}
