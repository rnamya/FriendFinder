package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServerCommunicator {
	NetworkHandler networkHandler;
	DataManager dataManager;
	
	private final String KEY_REQUEST_USERS = "users";
	private final String KEY_REQUEST_PHONE_NUMBER = "phone_number";
	private final String KEY_REQUEST_PHONE_NUMBERS = "phone_numbers";
	private final String KEY_REQUEST_DISTANCES = "distances";
	private final String KEY_REQUEST_LATITUDE = "latitude";
	private final String KEY_REQUEST_LONGITUDE = "longitude";

	private final String KEY_RESPONSE_PHONE = "phone";
	private final String KEY_RESPONSE_DISTANCE = "distance";
	private final String KEY_RESPONSE_CONTACT_ARRAY = "contacts";
	
	//private static final String SERVER_LOCATION = "http://192.168.0.100";
	private static final String SERVER_LOCATION = "http://10.0.2.2:3000";
	
	private static final String SERVER_REGISTER_ADDRESS = SERVER_LOCATION + "/users/create";
	private static final String SERVER_UPDATE_LOCATION_ADDRESS = SERVER_LOCATION + "/users/update";
	private static final String SERVER_GET_DISTANCES_ADDRESS = SERVER_LOCATION + "/distances/distance";
	
	public ServerCommunicator(NetworkHandler networkHandler, DataManager dataManager) {
		this.networkHandler = networkHandler;
		this.dataManager = dataManager;
	}
	
	public String register() throws Exception {
		JSONObject usersObject = new JSONObject();
		com.example.friendfinder.Location location = dataManager.getLocation();
		usersObject.put(KEY_REQUEST_PHONE_NUMBER, dataManager.getUsername());
		usersObject.put(KEY_REQUEST_LATITUDE, location.getLatitude());
		usersObject.put(KEY_REQUEST_LONGITUDE, location.getLongitude());

		JSONObject registerJson = new JSONObject();
		registerJson.put(KEY_REQUEST_USERS, usersObject);
		
		JSONObject response = null;
		
		try {
			response = networkHandler.send(registerJson, SERVER_UPDATE_LOCATION_ADDRESS, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (response == null) ? null : response.toString();
	}
	
	public String checkIn() throws JSONException, RuntimeException {
		final com.example.friendfinder.Location location = dataManager.getLocation();
		if (location == null) {
			throw new RuntimeException("Location unavailable");
		}
		
		final JSONObject[] responses = new JSONObject[1];
		new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				JSONObject usersObject = new JSONObject();
				JSONObject updateJson = new JSONObject();
				try {
					usersObject.put(KEY_REQUEST_PHONE_NUMBER, dataManager.getUsername());
					usersObject.put(KEY_REQUEST_LATITUDE, location.getLatitude());
					usersObject.put(KEY_REQUEST_LONGITUDE, location.getLongitude());
					
					updateJson.put(KEY_REQUEST_USERS, usersObject);
					
				} catch(JSONException e) {
					e.printStackTrace();
				}
				
				try {
					responses[0] = networkHandler.send(updateJson, SERVER_UPDATE_LOCATION_ADDRESS, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		//For this dirty synchronization to work, NetworkHandler's send function
		//must always return a non-null value
		while (responses[0] == null);
		
		Log.d("CHECKIN RESPONSE", responses[0].toString());
		return responses[0].toString();
	}
	
	public List<Contact> getContactsInfo(List<Contact> contacts) throws Exception {
		String phoneNumbers = "";
		for (int i=0; i<contacts.size(); i++) {
			StringBuilder strbldr = new StringBuilder(contacts.get(i).getPhone());
			//123123-1234
			strbldr.deleteCharAt(0);
			strbldr.deleteCharAt(3);
			strbldr.deleteCharAt(3);
			strbldr.deleteCharAt(6);
			phoneNumbers += strbldr.toString();
			if (i != contacts.size() - 1) {
				phoneNumbers += ",";
			}
		}
		
		String url = SERVER_GET_DISTANCES_ADDRESS + "?phone_number=" + dataManager.getUsername() + "&phone_numbers=" + phoneNumbers;
		/*JSONObject numbersObject = new JSONObject();
		numbersObject.put(KEY_REQUEST_PHONE_NUMBER, dataManager.getUsername());
		numbersObject.put(KEY_REQUEST_PHONE_NUMBERS, phoneNumbers);

		JSONObject json = new JSONObject();
		json.put(KEY_REQUEST_DISTANCES, numbersObject);
		*/
		
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
			contact.setDistance(object.getString(KEY_RESPONSE_DISTANCE));
			contacts.add(contact);
		}
		
		Log.d("DESERIALIZED", contacts.toString());
		
		//TODO Populate contacts with names so this list can be directly sent to the ListView
		return contacts; // Contacts being returned contain only phone number and distance.
	}
}