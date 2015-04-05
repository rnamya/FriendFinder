package com.example.friendfinder;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

	public final String KEY_DATA_MANAGER_EXTRA = "DataManager";
	private ServerCommunicator communicator;
	
	DataManager dataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dataManager = new DataManager(getApplicationContext());
        
        if(dataManager.getUsername() == null) {
        	Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        	startActivity(intent);
        }	
        
        setContentView(R.layout.activity_main);
        try {
        communicator = new ServerCommunicator();
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
    
    // Using intent to launch another activity
        
    Button buttonLocation = (Button) findViewById(R.id.button1);
    buttonLocation.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, UpdateLocationActivity.class);
            startActivity(intent);
        }
    });
        
    Button buttonPeople = (Button) findViewById(R.id.button2);
    buttonPeople.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(MainActivity.this, PeopleNearbyActivity.class);
            //startActivity(intent);
        	
        	new Thread(){
        		@Override
        		public void run() {
        			JSONObject jsonData = null;
                	try {
                		jsonData = new JSONObject("{'hi':'hello'}");
                		Toast.makeText(getApplicationContext(), communicator.send(jsonData).toString(), Toast.LENGTH_SHORT).show();
                	}
                	catch (Exception e) {
                		e.printStackTrace();
        			}
        		}
        	}.start();
        }
    });

    Button buttonContacts = (Button) findViewById(R.id.button3);
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
