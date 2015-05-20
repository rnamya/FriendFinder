package com.example.lookapp;

public class Contact {
	String name;
	String phone;
    String distance;
    boolean hasAccessToLocation;
    
    public Contact() {
    	
    }
    public Contact(String phone, String name, boolean hasAccessToLocation, String distance) {
		this.name = name;
		this.phone = phone;
		this.hasAccessToLocation = hasAccessToLocation;
		this.distance = distance;
	}
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
    public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Contact) {
			Contact that = (Contact) object;
			return this.phone.equals(that.getPhone());
		}
		else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.phone.hashCode();
	}
	
	@Override
	public String toString() {
		return this.name + " " + this.phone + " " + this.hasAccessToLocation + " " + this.distance;
	}
}
