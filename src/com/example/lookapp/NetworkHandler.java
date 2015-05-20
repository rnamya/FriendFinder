package com.example.lookapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class NetworkHandler {
	private static final int TIMEOUT_IN_MILLIS = 2000;
	private static final String REQUEST_METHOD = "POST";
	
	public JSONObject sendLocation(String url) throws Exception {
		HttpURLConnection connection = getConnection(url);
		
		connection.connect();
		
		Log.d("STATUS", connection.getResponseCode()+": "+connection.getResponseMessage());
		
		InputStream stream = connection.getInputStream();
		JSONObject resultData = toJSON(stream);
		
		Log.d("SEND", "Read response stream!");
		Log.d("RESULT", resultData.toString());
		
		connection.disconnect();
		
		Log.d("SEND", "Connection terminated!");
		
		return resultData;
	}
	
	public JSONObject sendContactsRequest(String address) throws Exception {
		Log.d("URL", address);
		HttpURLConnection connection = getConnection(address);
		Log.d("SEND", "Connection created!");
		
		connection.connect();
		
		Log.d("RESPONSE CODE AND MESSAGE", connection.getResponseCode()+": "+connection.getResponseMessage());
		
		InputStream stream = connection.getInputStream();
		JSONArray resultData = toJSONArray(stream);
		
		Log.d("SEND", "Read response stream!");
		connection.disconnect();
		Log.d("SEND", "Connection terminated!");
		
		JSONObject returnObject = new JSONObject();
		returnObject.put("data", resultData);
		
		return returnObject;
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
		
		while ( (line=reader.readLine()) != null) { // not reached the end of the stream
			string += line;
		}
		
		return new JSONObject(string);
	}
	
	private JSONArray toJSONArray(InputStream stream) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = "", string = "";
		
		while ( (line=reader.readLine()) != null) {
			string += line;
		}
		
		return new JSONArray(string);
	}
}
