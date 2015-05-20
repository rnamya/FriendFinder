package com.example.lookapp;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

public class DataManager {
	
	private final String USER_PREF = "UserPreferences";
	
	private final String KEY_USERNAME = "username";
	
	Context context;
	ContentResolver contentResolver;
	SharedPreferences sharedPreferences;
	Database database;
	
	LocationManager locationManager;
	
	DataManager(Context context) {
		this.context = context;
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
			
			@Override
			public void onProviderEnabled(String provider) {
			}
			
			@Override
			public void onProviderDisabled(String provider) {
			}
			
			@Override
			public void onLocationChanged(Location location) {
			}
		});
		
		this.sharedPreferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
		this.contentResolver = context.getContentResolver();
		this.database = new Database(context, null, null, 1);
		
		updateContacts();
	}
	
	public String getUsername()
	{
		return sharedPreferences.getString(KEY_USERNAME, null);
	}
	
	public void setUsername(String username)
	{
		SharedPreferences.Editor edit = sharedPreferences.edit();
		edit.putString(KEY_USERNAME, username);
		edit.commit();
	}
	
	public void addContact(Contact contact) {
		this.database.addContact(contact);
	}
	
	public void updateContact(Contact contact) {
		this.database.updateContact(contact);
	}
	
	public void updateContacts() {
		Set<Contact> allContacts = fetchContacts();
		this.database.updateAllContacts(allContacts);
	}
	
	public Set<Contact> getAllContacts() {
		return this.database.getAllContacts();
	}
	
	public String formatPhone(String phone) {
        return new StringBuffer(phone.replaceAll("\\D+", "")).toString();
    }
	
	public Set<Contact> fetchContacts() {
	    Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
	    String _ID = ContactsContract.Contacts._ID;
	    String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
	    String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

	    Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	    String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	    String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

	    Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

	    Set<Contact> contactsSet = new HashSet<>();
	      // Loop for every contact in the phone
	    if (cursor.getCount() > 0) {
	        while (cursor.moveToNext()) {
	            String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
	            String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

	            int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

	            if (hasPhoneNumber > 0) {
	                // Query and loop for every phone number of the contact
	                Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

	                while (phoneCursor.moveToNext()) {
	                    String phoneNumber = formatPhone(phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)));
	                	if (phoneNumber.length() < 10) continue;
	                	phoneNumber = new StringBuffer(new StringBuffer(phoneNumber).reverse().substring(0, 10)).reverse().toString();
	                	  
	                	Contact contact = new Contact(phoneNumber.toString(), name, false, "Unknown");
	                    contactsSet.add(contact);
	                }
	                phoneCursor.close();
	            }
	        }
	    }

	    return contactsSet;
	}
	
	// Contact from server contains only phone number and distance.
	// This function will return a contact containing name, phone number, distance and whether the contact has access to the user's location.
	
	public Contact makeContact(Contact contactFromServer)
	{
		contactFromServer.setName(database.getNameFromPhone(contactFromServer.getPhone()));
		contactFromServer.setHasAccessToLocation(database.getHasAccessToLocationFromPhone(contactFromServer.getPhone()));
		
		return contactFromServer;
	}
	
	com.example.lookapp.Location getLocation() {
		android.location.Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		com.example.lookapp.Location myLocation = null;
		if (location != null) {
			myLocation = new com.example.lookapp.Location(location.getLatitude(), location.getLongitude());
		}
		else {
			myLocation = new com.example.lookapp.Location(1.1f, 2.2f);
		}
		
		return myLocation;
	}
}