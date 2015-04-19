package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class PeopleNearbyActivity extends Activity {
	
	private ServerCommunicator serverCommunicator;
	private DataManager dataManager;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_nearby);

		listView = (ListView) findViewById(R.id.list);
		dataManager = new DataManager(getApplicationContext());
		
		try {
			serverCommunicator = new ServerCommunicator(new NetworkHandler(), dataManager);
			serverCommunicator.getContactsInfo(null).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Contact> contactsList = new ArrayList<Contact>(dataManager.getAllContacts());
		new ViewPeopleNearbyTask().execute(contactsList);

	}
	
	public class ViewPeopleNearbyTask extends AsyncTask<List<Contact>, Void, List<Contact>>
	{
		ProgressDialog progressDialog = new ProgressDialog(PeopleNearbyActivity.this);
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog.setMessage("\tLoading...");
			progressDialog.show();
		}

		@Override
		protected List<Contact> doInBackground(List<Contact>... params) {
			List<Contact> contactsInfo = new ArrayList<Contact>();
			List<Contact> contacts = params[0];
			
			try {
				contactsInfo = serverCommunicator.getContactsInfo(contacts);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return contactsInfo;
		}
		
		@Override
		protected void onPostExecute(List<Contact> contactsInfo)
		{
			 progressDialog.dismiss();
			 PeopleNearbyAdapter adapter = new PeopleNearbyAdapter(PeopleNearbyActivity.this, contactsInfo);
			 listView.setAdapter(adapter);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.people_nearby, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}