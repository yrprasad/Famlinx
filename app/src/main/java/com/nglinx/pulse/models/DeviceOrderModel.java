package com.nglinx.pulse.models;

import com.nglinx.pulse.utils.ApplicationUtils;

public class DeviceOrderModel implements Comparable<DeviceOrderModel> {
    private String id;
    private String udid;
    private String userId;
    private String username;
    private DeviceType deviceType;
    private OrderStatus status;
    private String createdDate;
    private String modifiedDate;
    private Integer rentalDays;
    private DeviceOrderType deviceOrderType;

    public DeviceOrderModel() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "DeviceOrderModel [id=" + id + ", udid=" + udid + ", userId=" + userId + ", username=" + username
                + ", deviceType=" + deviceType + ", status=" + status + ", createdDate=" + createdDate
                + ", modifiedDate=" + modifiedDate + "]";
    }

    public Integer getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(Integer rentalDays) {
        this.rentalDays = rentalDays;
    }

    public DeviceOrderType getDeviceOrderType() {
        return deviceOrderType;
    }

    public void setDeviceOrderType(DeviceOrderType deviceOrderType) {
        this.deviceOrderType = deviceOrderType;
    }

    @Override
    public int compareTo(DeviceOrderModel model) {
        return ApplicationUtils.compareDatesInFormat(this.createdDate, model.getCreatedDate());
    }
}
