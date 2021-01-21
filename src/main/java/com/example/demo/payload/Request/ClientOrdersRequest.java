package com.example.demo.payload.Request;

import com.example.demo.DTO.CellData;
import com.example.demo.DTO.User;

import java.util.List;
import java.util.Objects;

public class ClientOrdersRequest {

    private String dateFrom;
    private String dateTo;
    private String workDateFrom;
    private String workDateTo;
    private Boolean payed;
    private String searchString;
    private String state;
    private Boolean closeDate;
    private Integer customerId;
    private Integer employeeId;
    private Integer workStatus;
    private Integer workId;
    private Integer detailId;
    private Boolean status;
    private User user;
    private String lang;
    private boolean autoDetectionExecutor;
    private boolean onlyUser;
    private List<CellData> cellData;

    public ClientOrdersRequest() {
    }

    public ClientOrdersRequest(User user) {
        this.user = user;
        this.lang = "ru";
        this.autoDetectionExecutor = false;
        this.workStatus = 2;
    }

    public ClientOrdersRequest(User user, boolean onlyUserMod) {
        this.user = user;
        this.lang = "ru";
        this.autoDetectionExecutor = false;
        this.workStatus = 2;
        this.onlyUser = onlyUserMod;
    }

    public ClientOrdersRequest(String dateFrom,
                               String dateTo,
                               String workDateFrom,
                               String workDateTo,
                               Boolean payed,
                               String searchString,
                               String state,
                               Boolean closeDate,
                               Integer customerId,
                               Integer employeeId,
                               Integer workStatus,
                               Integer workId,
                               Integer detailId,
                               Boolean status,
                               User user,
                               String lang,
                               List<CellData> cellData) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.workDateFrom = workDateFrom;
        this.workDateTo = workDateTo;
        this.payed = payed;
        this.searchString = searchString;
        this.state = state;
        this.closeDate = closeDate;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.workStatus = workStatus;
        this.workId = workId;
        this.detailId = detailId;
        this.status = status;
        this.user = user;
        this.lang = lang;
        this.cellData = cellData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientOrdersRequest that = (ClientOrdersRequest) o;
        return Objects.equals(dateFrom, that.dateFrom) && Objects.equals(dateTo, that.dateTo) && Objects.equals(workDateFrom, that.workDateFrom) && Objects.equals(workDateTo, that.workDateTo) && Objects.equals(payed, that.payed) && Objects.equals(searchString, that.searchString) && Objects.equals(state, that.state) && Objects.equals(closeDate, that.closeDate) && Objects.equals(customerId, that.customerId) && Objects.equals(employeeId, that.employeeId) && Objects.equals(workStatus, that.workStatus) && Objects.equals(workId, that.workId) && Objects.equals(detailId, that.detailId) && Objects.equals(status, that.status) && Objects.equals(user, that.user) && Objects.equals(lang, that.lang) && Objects.equals(cellData, that.cellData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateFrom, dateTo, workDateFrom, workDateTo, payed, searchString, state, closeDate, customerId, employeeId, workStatus, workId, detailId, status, user, lang, cellData);
    }

    public boolean isAutoDetectionExecutor() {
        return autoDetectionExecutor;
    }

    public void setAutoDetectionExecutor(boolean autoDetectionExecutor) {
        this.autoDetectionExecutor = autoDetectionExecutor;
    }

    public boolean isOnlyUser() {
        return onlyUser;
    }

    public void setOnlyUser(boolean onlyUser) {
        this.onlyUser = onlyUser;
    }

    public Boolean getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Boolean closeDate) {
        this.closeDate = closeDate;
    }


    public String getWorkDateFrom() {
        return workDateFrom;
    }

    public void setWorkDateFrom(String workDateFrom) {
        this.workDateFrom = workDateFrom;
    }

    public String getWorkDateTo() {
        return workDateTo;
    }

    public void setWorkDateTo(String workDateTo) {
        this.workDateTo = workDateTo;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CellData> getCellData() {
        return cellData;
    }

    public void setCellData(List<CellData> cellData) {
        this.cellData = cellData;
    }
}
