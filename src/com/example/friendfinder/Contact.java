package com.example.friendfinder;

public class Contact {
	String name;
    String phone;
    boolean hasAccessToLocation;
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isHasAccessToLocation() {
		return hasAccessToLocation;
	}
	public void setHasAccessToLocation(boolean hasAccessToLocation) {
		this.hasAccessToLocation = hasAccessToLocation;
	}
}
