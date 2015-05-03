package com.example.friendfinder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.util.Log;

public class NetworkHandler {
	private static final int TIMEOUT_IN_MILLIS = 2000;
	private static final String REQUEST_METHOD = "GET";
	
	public JSONObject send(JSONObject jsonData, String address, boolean testing) throws Exception {
		if (testing) {
			return testSend(jsonData, address);
		}
		else {
			return send(jsonData, address);
		}
	}
	
	public JSONObject send(String address) throws Exception {
		Log.d("URL", address);
		HttpURLConnection connection = getConnection(address);
		Log.d("SEND", "Connection created!");
		
		connection.connect();
		
		Log.d("RESPONSE CODE AND MESSAGE", connection.getResponseCode()+": "+connection.getResponseMessage());
		
		InputStream stream = connection.getInputStream();
		JSONObject resultData = toJSON(stream);
		
		Log.d("SEND", "Read response stream!");
		
		connection.disconnect();
		
		Log.d("SEND", "Connection terminated!");
		
		return resultData;
	}
	
	public JSONObject send(JSONObject jsonData, String address) throws Exception {
		
		HttpURLConnection connection = getConnection(address);
		Log.d("SEND", "Connection created!");
		
		OutputStream outStream = connection.getOutputStream();
		PrintWriter writer = new PrintWriter(outStream);
		
		connection.connect();
		
		Log.d("SEND", "Connection established!");
		JSONObject phnos = new JSONObject();
		phnos.put("phone_number", "9876543210");
		phnos.put("phone_numbers", "9876543210,9876543211");
		
		JSONObject sampleJson = new JSONObject();
		sampleJson.put("distances", phnos);
		
		writer.write(sampleJson.toString());
		
		Log.d("REQUEST OBJECT", sampleJson.toString());
		
		Log.d("SEND", "Wrote data into stream!");
		
		Log.d("RESPONSE CODE AND MESSAGE", connection.getResponseCode()+": "+connection.getResponseMessage());
		
		InputStream stream = connection.getInputStream();
		JSONObject resultData = toJSON(stream);
		
		Log.d("SEND", "Read response stream!");
		
		connection.disconnect();
		
		Log.d("SEND", "Connection terminated!");
		
		return resultData;
	}
	
	public JSONObject testSend(JSONObject requestObject, String address) throws Exception {
		JSONObject json = new JSONObject();
		
		json.put("phone_number", "9449728724");
		json.put("latitude", "1.23456789");
		json.put("longitude", "1.23456789");	
		
		/*
		JSONObject contact1 = new JSONObject();
		contact1.put("phone", "(944) 972-8724");
		contact1.put("distance", "2");
		
		JSONObject contact2 = new JSONObject();
		contact2.put("phone", "(782) 910-6652");
		contact2.put("distance", "90");
		
		JSONObject contact3 = new JSONObject();
		contact3.put("phone", "(782) 910-6651");
		contact3.put("distance", "9");
		
		JSONArray contacts = new JSONArray();
		contacts.put(contact1);
		contacts.put(contact2);
		contacts.put(contact3);
		
		json.put("contacts", contacts);
		*/
		Log.d("TEST SEND FOR CHECKIN", json.toString());
		return json;
	}
	
	/**
	 * 
	 * @param address The address to which a connection is to be made
	 * @return Returns an HttpUrlConnection
	 * @throws Exception Throws an exception when connection could not be established
	 */
	private HttpURLConnection getConnection(String address) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(address).openConnection();
		
		connection.setConnectTimeout(TIMEOUT_IN_MILLIS);
		connection.setReadTimeout(TIMEOUT_IN_MILLIS);
		connection.setRequestMethod(REQUEST_METHOD);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		return connection;
	}
	
	private JSONObject toJSON(InputStream stream) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = "", string = "";
		
		while ( (line=reader.readLine()) != null) {
			string += line;
		}
		
		return new JSONObject(string);
	}
}
