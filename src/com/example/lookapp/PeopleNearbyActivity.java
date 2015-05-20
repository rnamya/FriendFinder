package com.example.lookapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

		serverCommunicator = new ServerCommunicator(new NetworkHandler(), dataManager);
		
		new ViewPeopleNearbyTask().execute(new ArrayList<Contact>(dataManager.getAllContacts()));
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
			
			Log.d("CONTACTS INFO FROM SERVER", contactsInfo.toString());
			return contactsInfo;
		}
		
		@Override
		protected void onPostExecute(List<Contact> contactsInfo)
		{
		    Contact dummy1 = new Contact("9999999999", "VOV", true, "1km");
		    Contact dummy2 = new Contact("1111111111", "VOOOOO", true, "4km");
		    
		    contactsInfo.add(dummy1);
		    contactsInfo.add(dummy2);
		    
			PeopleNearbyAdapter adapter = new PeopleNearbyAdapter(PeopleNearbyActivity.this, contactsInfo);
			progressDialog.dismiss();
			listView.setAdapter(adapter);
			Log.d("ADAPTER", listView.getAdapter().toString());
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