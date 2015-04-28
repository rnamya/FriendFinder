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
	
	private final String KEY_REQUEST_PHONE = "phone";
	private final String KEY_REQUEST_CONTACTS = "contacts";
	private final String KEY_REQUEST_LATITUDE = "latitude";
	private final String KEY_REQUEST_LONGITUDE = "longitude";

	private final String KEY_RESPONSE_PHONE = "phone";
	private final String KEY_RESPONSE_DISTANCE = "distance";
	private final String KEY_RESPONSE_CONTACT_ARRAY = "contacts";
	
	private static final String SERVER_REGISTER_ADDRESS = "http://lookation-testing.herokuapp.com/create";
	private static final String SERVER_UPDATE_LOCATION_ADDRESS = "http://lookation-testing.herokuapp.com/update";
	private static final String SERVER_GET_DISTANCES_ADDRESS = "http://lookation-testing.herokuapp.com/distances/distance";
	
	public ServerCommunicator(NetworkHandler networkHandler, DataManager dataManager) {
		this.networkHandler = networkHandler;
		this.dataManager = dataManager;
	}
	
	public String register() throws Exception {
		JSONObject locationJson = new JSONObject();
		com.example.friendfinder.Location location = dataManager.getLocation();
		
		locationJson.put(KEY_REQUEST_PHONE, dataManager.getUsername());
		locationJson.put(KEY_REQUEST_LATITUDE, location.latitude);
		locationJson.put(KEY_REQUEST_LONGITUDE, location.longitude);
		
		JSONObject response = null;
		
		try {
			response = networkHandler.send(locationJson, SERVER_UPDATE_LOCATION_ADDRESS, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response.toString();
	}
	
	public String checkIn() throws JSONException {
		JSONObject locationJson = new JSONObject();
		com.example.friendfinder.Location location = dataManager.getLocation();
		
		locationJson.put(KEY_REQUEST_PHONE, dataManager.getUsername());
		locationJson.put(KEY_REQUEST_LATITUDE, location.latitude);
		locationJson.put(KEY_REQUEST_LONGITUDE, location.longitude);
		
		JSONObject response = null;
		
		try {
			response = networkHandler.send(locationJson, SERVER_REGISTER_ADDRESS, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response.toString();
	}
	
	public List<Contact> getContactsInfo(List<Contact> contacts) throws Exception {
		JSONObject json = new JSONObject();
		// The request. Sending phone number, password, location, list of contacts.
		json.put(KEY_REQUEST_PHONE, dataManager.getUsername());
		json.put(KEY_REQUEST_CONTACTS, new JSONArray(contacts));
		
		JSONObject response = null;
		try {
			response = networkHandler.send(json, SERVER_GET_DISTANCES_ADDRESS, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.d("RESPONSE", response.toString());
		// The response is a JSON object.
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