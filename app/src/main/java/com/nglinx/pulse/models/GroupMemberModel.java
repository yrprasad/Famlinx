package com.nglinx.pulse.models;

public class GroupMemberModel {

    private String id;

    private String username;

    private String name;

    private String status;

    private String color;

    private String image;

    private UserTrackingModel trackingModel;

    public GroupMemberModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserTrackingModel getTrackingModel() {
        return trackingModel;
    }

    public void setTrackingModel(UserTrackingModel trackingModel) {
        this.trackingModel = trackingModel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "GroupMemberModel{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", color='" + color + '\'' +
                ", image='" + image + '\'' +
                ", trackingModel=" + trackingModel +
                '}';
    }
}
