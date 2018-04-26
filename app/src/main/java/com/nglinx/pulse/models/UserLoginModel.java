package com.nglinx.pulse.models;

public class UserLoginModel {

    private String username;
    private String password;
    private String email;
    private UserType type;
    private String appKey;
    private String token;
    private String latitude;
    private String longitude;

    public UserLoginModel() {
        super();
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public UserType getType() {
        return type;
    }
    public void setType(UserType type) {
        this.type = type;
    }
    public String getAppKey() {
        return appKey;
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
    @Override
    public String toString() {
        return "UserLoginModel [username=" + username + ", password=" + password + ", email=" + email + ", type="
                + type + ", appKey=" + appKey + ", token=" + token + ", latitude=" + latitude + ", longitude="
                + longitude + "]";
    }
}
