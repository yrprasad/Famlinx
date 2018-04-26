package com.nglinx.pulse.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {

    private String id;
    private String parentId;
    private String username;
    private String password;
    private String name;
    private String picture;
    private String email;
    private String phone;
    private Integer status;
    private String udid;
    private String address;
    private String city;
    private String role;
    private String token;
    private UserType type;
    private String features;
    private String appKey;
    private String createdDate;
    private String modifiedDate;
    private UserTrackingModel location;
    private List<GroupModel> groups;
    private Map<String, SettingsModel> settingsMap;

    public UserModel() {
        super();
        settingsMap = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public List<GroupModel> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupModel> groups) {
        this.groups = groups;
    }

    public UserTrackingModel getLocation() {
        return location;
    }

    public void setLocation(UserTrackingModel location) {
        this.location = location;
    }

    public Map<String, SettingsModel> getSettingsMap() {
        return settingsMap;
    }

    public void setSettingsMap(Map<String, SettingsModel> settingsMap) {
        this.settingsMap = settingsMap;
    }

    @Override
    public String toString() {
        return this.username;
    }
}
