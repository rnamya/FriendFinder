package com.example.lookapp;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ServerCommunicator serverCommunicator;	
	private DataManager dataManager;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dataManager = new DataManager(getApplicationContext());
        
        if(dataManager.getUsername() == null) {
        	Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        	startActivity(intent);
        }
        
    	setContentView(R.layout.activity_main);
        
        serverCommunicator = new ServerCommunicator(new NetworkHandler(), dataManager);
        
        Typeface type = Typeface.createFromAsset(getAssets(), "Cookies.ttf");
        
        Button buttonLocation = (Button) findViewById(R.id.checkIn);
        buttonLocation.setTypeface(type);
        buttonLocation.setOnClickListener(new View.OnClickListener() {    
	        @Override
	        public void onClick(View v) {
	        	String msg = null;
	        	try {
					msg = serverCommunicator.checkIn();
				} catch (JSONException e) {
					msg = "Failed to update location.";
				} catch (RuntimeException e) {
					msg = "Location unavailable! Try again later.";
				}
	        	
	        	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	        }
        });
        
	    Button buttonPeople = (Button) findViewById(R.id.peopleNearby);
	    buttonPeople.setTypeface(type);
	    buttonPeople.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            Intent intent = new Intent(MainActivity.this, PeopleNearbyActivity.class);
	            startActivity(intent);
	        }
	    	});

        Button buttonContacts = (Button) findViewById(R.id.contacts);
        buttonContacts.setTypeface(type);
	    buttonContacts.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
	            startActivity(intent);
        	}
	    	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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