package com.nglinx.pulse.models;

public class FenceModel {

    private String id;
    private String ownerId;
    private String name;
    private String latitude;
    private String longitude;
    private Integer radius;
    private Integer status;
    private String address;
    private String creationDate;
    private String modifiedDate;

    public FenceModel() {
    }

    public FenceModel(String id, String ownerId, String name, String latitude, String longitude, Integer radius,
                      Integer status, String address, String creationDate, String modifiedDate) {
        super();
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.status = status;
        this.address = address;
        this.creationDate = creationDate;
        this.modifiedDate = modifiedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return name;
    }

}
