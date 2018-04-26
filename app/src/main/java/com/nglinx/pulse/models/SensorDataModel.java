package com.nglinx.pulse.models;

public class SensorDataModel {
    private String id;
    private String udid;
    private String name;
    private String drum_level;
    private String steam_flow;
    private String ph;
    private String param1;
    private String param2;
    private String createdDate;

    public SensorDataModel() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDrum_level() {
        return drum_level;
    }

    public void setDrum_level(String drum_level) {
        this.drum_level = drum_level;
    }

    public String getLevel() {
        return drum_level;
    }

    public void setLevel(String level) {
        this.drum_level = level;
    }

    public String getSteam_flow() {
        return this.steam_flow;
    }

    public void setFlow(String flow) {
        this.steam_flow = flow;
    }

    public String getFlow() {
        return steam_flow;
    }

    public void setSteam_flow(String steam_flow) {
        this.steam_flow = steam_flow;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SensorDataModel [");
        if (drum_level != null && !drum_level.isEmpty())
            sb.append(" drum_level=" + drum_level);
        if (steam_flow != null && !steam_flow.isEmpty())
            sb.append(" steam_flow=" + steam_flow);
        if (ph != null && !ph.isEmpty())
            sb.append(" ph=" + ph);
        sb.append(" ]");
        return sb.toString();
    }
}
