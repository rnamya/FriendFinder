// ServerCommunicator receives the list of contacts and constructs JSON objects from it.

package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerCommunicator {
	NetworkHandler networkHandler;
	DataManager dataManager;
	
	private final String KEY_RESPONSE_CONTACT_ARRAY = "contacts";
	private final String KEY_RESPONSE_PHONE = "phone";
	private final String KEY_RESPONSE_DISTANCE = "distance";
	
	private final String KEY_REQUEST_CONTACTS = "contacts";
	private final String KEY_REQUEST_PHONE = "phone";
	private final String KEY_REQUEST_PASSWORD = "password";
	private final String KEY_REQUEST_LOCATION = "location";
	
	public ServerCommunicator(NetworkHandler networkHandler, DataManager dataManager) {
		this.networkHandler = networkHandler;
		this.dataManager = dataManager;
	}
	
	public List<Contact> getContactsInfo(List<Contact> contacts) throws Exception {
		JSONObject json = new JSONObject();
		// The request. Sending list of contacts, phone number, password and location.
		json.put(KEY_REQUEST_CONTACTS, new JSONArray(contacts));
		json.put(KEY_REQUEST_PHONE, dataManager.getUsername());
		json.put(KEY_REQUEST_PASSWORD, dataManager.getPassword());
		json.put(KEY_REQUEST_LOCATION, dataManager.getLocation());
		
		JSONObject response = null;
		try {
			response = networkHandler.testSend(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// The response is a JSON object.
		
		List<Contact> responseContacts = deserialize(response);
		
		for(Contact c: responseContacts)
		{
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
			contact.setDistance(object.getInt(KEY_RESPONSE_DISTANCE));
			contacts.add(contact);
		}
		
		//TODO Populate contacts with names so this list can be directly sent to the ListView
		return contacts; // Contacts being returned contain only phone number and distance.
	}
}
