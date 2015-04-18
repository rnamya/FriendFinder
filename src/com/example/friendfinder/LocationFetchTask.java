package com.example.friendfinder;

import java.util.List;

import android.os.AsyncTask;

public class LocationFetchTask extends AsyncTask<List<Contact>, Void, List<Contact>> {

	private final NetworkHandler serverCommunicator;
	
	public LocationFetchTask(NetworkHandler serverCommunicator) {
		this.serverCommunicator = serverCommunicator;
	}
	
	@Override
	protected List<Contact> doInBackground(List<Contact>... params) {
		
		return null;
	}

}
