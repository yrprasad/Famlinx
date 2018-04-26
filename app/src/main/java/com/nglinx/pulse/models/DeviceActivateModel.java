package com.nglinx.pulse.models;

public class DeviceActivateModel {
    private String activationCode;

    public DeviceActivateModel() {
        super();
    }
    public String getActivationCode() {
        return activationCode;
    }
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
    @Override
    public String toString() {
        return "DeviceActivateModel [activationCode=" + activationCode + "]";
    }
}
