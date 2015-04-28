package com.example.friendfinder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class NetworkHandler {
	private static final int TIMEOUT_IN_MILLIS = 2000;
	private static final String REQUEST_METHOD = "POST";
	
	public JSONObject send(JSONObject jsonData, String address, boolean testing) throws Exception {
		if (testing) {
			return testSend(jsonData, address);
		}
		else {
			return send(jsonData, address);
		}
	}
	
	public JSONObject send(JSONObject jsonData, String address) throws Exception {
		HttpURLConnection connection = getConnection(address);
		connection.connect();
		
		OutputStream outStream = connection.getOutputStream();
		PrintWriter writer = new PrintWriter(outStream);
		writer.write(jsonData.toString());
		
		InputStream stream = connection.getInputStream();
		JSONObject resultData = toJSON(stream);
		
		connection.disconnect();
		
		return resultData;
	}
	
	public JSONObject testSend(JSONObject requestObject, String address) throws Exception {
		JSONObject json = new JSONObject();
		
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
		
		return json;
	}
	
	public JSONObject testSendLocation(JSONObject j) throws Exception {
		
		JSONObject json = new JSONObject();
		json.put("phone", "9449729724");
		json.put("location", "myLocation");
		
		JSONObject response = new JSONObject();
		response.put("STATUS", "OK");
		
		return response;
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
