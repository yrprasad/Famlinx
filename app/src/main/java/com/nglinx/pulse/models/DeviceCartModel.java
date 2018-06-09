package com.nglinx.pulse.models;

public class DeviceCartModel {

    private DeviceType type;
    private String name;
    private int cost;
    private DeviceOrderType orderType;
    private int count;
    private int rentalDays;

    public DeviceCartModel() {

    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public DeviceOrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(DeviceOrderType orderType) {
        this.orderType = orderType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public void setInfo(DeviceTypesModel deviceTypesModel) {
        this.type = deviceTypesModel.getType();
        this.name = deviceTypesModel.getName();
        this.cost = deviceTypesModel.getCost();
        this.orderType = deviceTypesModel.getOrderType();
    }

    @Override
    public String toString() {
        return "DeviceTypesModel [type=" + type + ", name=" + name + "]";
    }
}
