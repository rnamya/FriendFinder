package com.example.friendfinder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class ServerCommunicator {
	private static final String SERVER_IP_ADDRESS = "http://www.mocky.io/v2/551f9166de02019314690e3f";
	private static final int TIMEOUT_IN_MILLIS = 2000;
	private static final String REQUEST_METHOD = "POST";
	
	URL url;
	
	public ServerCommunicator() throws Exception {
		this.url = new URL(SERVER_IP_ADDRESS);
	}
	
	public JSONObject send(JSONObject jsonData) throws Exception
	{
		HttpURLConnection connection = getConnection();
		
		connection.connect();
		
		OutputStream outStream = connection.getOutputStream();
		PrintWriter writer = new PrintWriter(outStream);
		writer.write(jsonData.toString());
		
		InputStream stream = connection.getInputStream();
		
		JSONObject resultData = toJSON(stream);
		
		connection.disconnect();
		
		return resultData;
	}
	
	private HttpURLConnection getConnection() throws Exception {
		HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
		
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
