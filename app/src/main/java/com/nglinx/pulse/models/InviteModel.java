package com.nglinx.pulse.models;

public class InviteModel {

    private String id;
    private String uuid;
    private String name;
    private String email;
    private String toId;
    private String toName;
    private String date;
    private Integer status;

    public InviteModel() {
    }

    public InviteModel(String id, String uuid, String name, String email, String toId,
                       String toName, String date, Integer status) {
        super();
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.toId = toId;
        this.toName = toName;
        this.date = date;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "InviteModel{" +
                "id='" + id + '\'' +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status +
                '}';
    }
}
