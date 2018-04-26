package com.nglinx.pulse.models;

public class LocationModel {
	
	private String latitude;
	private String longitude;
	
	public LocationModel() {
		super();
	}
	
	public LocationModel(String latitude, String langitude) {
		super();
		this.latitude = latitude;
		this.longitude = langitude;
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

	public void setLongitude(String langitude) {
		this.longitude = langitude;
	}
	
}
