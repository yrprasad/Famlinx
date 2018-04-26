package com.nglinx.pulse.models;

public class RegisterModel {

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private UserType type;
    private String parentId;

    public RegisterModel() {
	super();
    }

    public RegisterModel(String username, String password, String name, String email, String phone) {
	super();
	this.username = username;
	this.password = password;
	this.name = name;
	this.email = email;
	this.phone = phone;
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

    public UserType getType() {
	return type;
    }

    public void setType(UserType type) {
	this.type = type;
    }

    public String getParentId() {
	return parentId;
    }

    public void setParentId(String parentId) {
	this.parentId = parentId;
    }

    @Override
    public String toString() {
	return "RegisterModel [username=" + username + ", password=" + password + ", name=" + name + ", email=" + email
		+ ", phone=" + phone + "]";
    }

}
