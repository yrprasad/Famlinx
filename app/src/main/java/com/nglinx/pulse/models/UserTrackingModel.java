package com.nglinx.pulse.models;

import java.io.Serializable;

public class UserTrackingModel implements Serializable {

    private static final long serialVersionUID = -6525544764197806797L;
    private String id;
    private String uuid;
    private String latitude;
    private String longitude;
    private String accuracy;
    private String altitude;
    private Integer alert_status;
    private Integer battery;
    private Integer speed;
    private Integer fence;
    private Boolean loop;
    private Presence presence;
    private Integer tamper;
    private Integer cellId;
    private String modifiedTime;
    private Integer tamperStatus;
    private Boolean hardwareFault;
    private DeviceType type;
    // Used the below properties in sensor
    private String drum_level;
    private String steam_flow;
    private String version;

    public enum Presence {
        ACTIVE, WEB, OFFLINE, GPS, WIFI, GSM_TOWER;
    }

    public UserTrackingModel() {
        super();
    }

    public UserTrackingModel(String uuid, String latitude, String longitude,
                             Integer alert_status, Integer battery) {
        super();
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.alert_status = alert_status;
        this.battery = battery;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getAlert_status() {
        return alert_status;
    }

    public void setAlert_status(Integer alert_status) {
        this.alert_status = alert_status;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getFence() {
        return fence;
    }

    public void setFence(Integer fence) {
        this.fence = fence;
    }

    public Integer getTamper() {
        return tamper;
    }

    public void setTamper(Integer tamper) {
        this.tamper = tamper;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Integer getTamperStatus() {
        return tamperStatus;
    }

    public void setTamperStatus(Integer tamperStatus) {
        this.tamperStatus = tamperStatus;
    }

    public Boolean getHardwareFault() {
        return hardwareFault;
    }

    public void setHardwareFault(Boolean hardwareFault) {
        this.hardwareFault = hardwareFault;
    }

    public Boolean getLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

    public Presence getPresence() {
        return presence;
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getDrum_level() {
        return drum_level;
    }

    public void setDrum_level(String drum_level) {
        this.drum_level = drum_level;
    }

    public String getSteam_flow() {
        return steam_flow;
    }

    public void setSteam_flow(String steam_flow) {
        this.steam_flow = steam_flow;
    }

    @Override
    public String toString() {
        return "UserTrackingModel [id=" + id + ", uuid=" + uuid + ", latitude=" + latitude + ", longitude=" + longitude
                + ", accuracy=" + accuracy + ", altitude=" + altitude + ", alert_status=" + alert_status + ", battery="
                + battery + ", speed=" + speed + ", fence=" + fence + ", loop=" + loop
                + ", presence=" + presence + ", tamper=" + tamper + ", cellId=" + cellId + ", modifiedTime="
                + modifiedTime + ", tamperStatus=" + tamperStatus + ", hardwareFault=" + hardwareFault + ", type="
                + type + ", version=" + version + "]";
    }


}
