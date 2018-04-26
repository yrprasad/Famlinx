package com.nglinx.pulse.models;

import com.nglinx.pulse.models.DeviceStatus;
import com.nglinx.pulse.models.DeviceType;

public class DeviceModel {
    // internal unique id
    private String id;
    // device unique id
    private String udid;
    private String userId;
    private String attachedUserName;
    private DeviceStatus status;
    private boolean activated;
    private String activationCode;
    private String createdDate;
    private String modifiedDate;
    private DeviceType type;
    private String features;

    public DeviceModel() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAttachedUserName() {
        return attachedUserName;
    }

    public void setAttachedUserName(String userName) {
        this.attachedUserName = userName;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
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

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "DeviceModel [id=" + id + ", udid=" + udid + ", userId=" + userId + ", attachedUserName=" + attachedUserName
                + ", status=" + status + ", activated=" + activated + ", activationCode=" + activationCode
                + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", type=" + type + ", features="
                + features + "]";
    }
}
