package com.example.friendfinder;

public class Contact {
	String name;
    String phone;
    boolean hasAccessToLocation;
	
    public Contact(String phone, String name, boolean hasAccessToLocation) {
		this.name = name;
		this.phone = phone;
		this.hasAccessToLocation = hasAccessToLocation;
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
		return this.name + " " + this.phone + " " + this.hasAccessToLocation;
	}
}
