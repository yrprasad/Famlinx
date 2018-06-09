package com.nglinx.pulse.models;

import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.utils.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;

public class DeviceTypesModel {

    private DeviceType type;
    private String name;
    private int cost;
    private DeviceOrderType orderType;

    private List<Integer> sizes;
    private List<String> description;
    private List<String> care;
    public DeviceTypesModel()
    {
        sizes = new ArrayList<>(3);
        sizes.add(1);
        sizes.add(2);
        sizes.add(3);
        description = new ArrayList<>(3);
        description.add(name + "Description1");
        description.add(name + "Description2");
        description.add(name + "Description3");
        care = new ArrayList<>(3);
        care.add(name + "Care1");
        care.add(name + "Care2");
        care.add(name + "Care3");
    }

    public DeviceTypesModel(DeviceType type, String name) {
        super();
        this.type = type;
        this.name = name;
        sizes = new ArrayList<>(3);
        sizes.add(1);
        sizes.add(2);
        sizes.add(3);
        description = new ArrayList<>(3);
        description.add(name + "Description1");
        description.add(name + "Description2");
        description.add(name + "Description3");
        care = new ArrayList<>(3);
        care.add(name + "Care1");
        care.add(name + "Care2");
        care.add(name + "Care3");

        cost = ApplicationUtils.getDeviceCost(type);
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

    public List<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(List<Integer> sizes) {
        this.sizes = sizes;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getCare() {
        return care;
    }

    public void setCare(List<String> care) {
        this.care = care;
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

    @Override
    public String toString() {
        return "DeviceTypesModel [type=" + type + ", name=" + name + "]";
    }
}
