package com.nglinx.pulse.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationModel implements Comparable<NotificationModel>{

    public enum Type {ALERT, INFO} ;

    private String id;
    private String userId;
    private Type type;
    private String message;
    private String createdDate;
    private Integer ack;
    private String ackDate;

    public NotificationModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
        this.ack = ack;
    }

    public String getAckDate() {
        return ackDate;
    }

    public void setAckDate(String ackDate) {
        this.ackDate = ackDate;
    }

    @Override
    public String toString() {
        return "NotificationModel [id=" + id + ", userId=" + userId + ", type=" + type + ", message=" + message
                + ", createdDate=" + createdDate + ", ack=" + ack + ", ackDate=" + ackDate + "]";
    }

    @Override
    public int compareTo(NotificationModel notificationModel) {

        if((notificationModel.getCreatedDate() == null) || (this.getCreatedDate() == null))
            return 0;

        try {
            Date date2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(notificationModel.getCreatedDate());
            Date date1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(this.getCreatedDate());
            if(date1.compareTo(date2) > 0)
                return 1;
            else
                return -1;
        } catch (ParseException e) {
        }
        return 0;
    }

}
