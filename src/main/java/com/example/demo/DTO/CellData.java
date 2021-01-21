package com.example.demo.DTO;

public class CellData {
    private String cellName;

    private String cellData;

    public CellData() {
    }

    public CellData(String cellData) {
        this.cellData = cellData;
    }

    public CellData(String cellName, String cellData) {
        this.cellName = cellName;
        this.cellData = cellData;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getCellData() {
        return cellData;
    }

    public void setCellData(String cellData) {
        this.cellData = cellData;
    }
}
