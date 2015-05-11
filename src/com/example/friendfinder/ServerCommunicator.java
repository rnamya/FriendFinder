package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServerCommunicator {
	private final NetworkHandler networkHandler;
	private final DataManager dataManager;
	
	private final String KEY_REQUEST_USERS = "users";
	private final String KEY_REQUEST_PHONE_NUMBER = "phone_number";
	private final String KEY_REQUEST_PHONE_NUMBERS = "phone_numbers";
	private final String KEY_REQUEST_DISTANCES = "distances";
	private final String KEY_REQUEST_LATITUDE = "latitude";
	private final String KEY_REQUEST_LONGITUDE = "longitude";

	private final String KEY_RESPONSE_PHONE = "phone_number";
	private final String KEY_RESPONSE_DISTANCE = "distance";
	private final String KEY_RESPONSE_CONTACT_ARRAY = "data";
	
	private static final String SERVER_LOCATION = "http://lookation-testing.herokuapp.com";
	
	private static final String SERVER_REGISTER_ADDRESS = SERVER_LOCATION + "/users/create";
	private static final String SERVER_UPDATE_LOCATION_ADDRESS = SERVER_LOCATION + "/users/update";
	private static final String SERVER_GET_DISTANCES_ADDRESS = SERVER_LOCATION + "/distances/distance";
	
	public ServerCommunicator(NetworkHandler networkHandler, DataManager dataManager) {
		this.networkHandler = networkHandler;
		this.dataManager = dataManager;
	}
	
	public String register() throws Exception {
		return send(false);
	}
	
	public String send(final boolean isUpdate) {
		final com.example.friendfinder.Location location = dataManager.getLocation();
		if (location == null) {
			throw new RuntimeException("Location unavailable");
		}
		
		final String[] message = new String[1];
		message[0] = "unknown";
		
		new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				String url = isUpdate? SERVER_UPDATE_LOCATION_ADDRESS: SERVER_REGISTER_ADDRESS;
				url += "?phone_number=" + dataManager.getUsername() + 
						"&latitude="+location.getLatitude()+"&longitude="+location.getLongitude();
				try {
					JSONObject result = networkHandler.sendLocation(url);
					message[0] = result.getString("message");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		while(message[0] == "unknown");

		return message[0];
	}
	
	public String checkIn() throws JSONException, RuntimeException {
		return send(true);
	}
	
	public List<Contact> getContactsInfo(List<Contact> contacts) throws Exception {
		String phoneNumbers = "";
		for (int i=0; i<contacts.size(); i++) {
			phoneNumbers += contacts.get(i).getPhone().toString();
			if (i != contacts.size() - 1) {
				phoneNumbers += ",";
			}
		}
		
		String url = SERVER_GET_DISTANCES_ADDRESS + "?phone_number=" + dataManager.getUsername() + "&phone_numbers=" + phoneNumbers;
		
		JSONObject response = null;
		try {
			response = networkHandler.send(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.d("RESPONSE", response.toString());
		
		List<Contact> responseContacts = deserialize(response);
		for (Contact c: responseContacts) {
			c = dataManager.makeContact(c);
		}
		
		return responseContacts;
	}
	
	private List<Contact> deserialize(JSONObject json) throws JSONException {
		List<Contact> contacts = new ArrayList<>();
		JSONArray jsonContacts = json.getJSONArray(KEY_RESPONSE_CONTACT_ARRAY);
		
		JSONObject object;
		Contact contact;
		for (int i=0; i<jsonContacts.length(); i++) {
			object = jsonContacts.getJSONObject(i);
			contact = new Contact();
			contact.setPhone(object.getString(KEY_RESPONSE_PHONE));
			String distance = object.getString(KEY_RESPONSE_DISTANCE);
			contact.setDistance(distance.substring(0, distance.indexOf('.') + 2));
			contacts.add(contact);
		}
		
		Log.d("DESERIALIZED", contacts.toString());
		
		//TODO Populate contacts with names so this list can be directly sent to the ListView
		return contacts; // Contacts being returned contain only phone number and distance.
	}
}