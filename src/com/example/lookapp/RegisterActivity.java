package com.example.lookapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	EditText phone_number;
	
	DataManager dataManager;
	ServerCommunicator serverCommunicator;
	
	Typeface type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	    
	    dataManager = new DataManager(getApplicationContext());
	    serverCommunicator = new ServerCommunicator(new NetworkHandler(), new DataManager(getApplicationContext()));
	    
	    type = Typeface.createFromAsset(getAssets(), "Cookies.ttf");
	    
	    ((Button) findViewById(R.id.okay)).setTypeface(type);
	    ((TextView) findViewById(R.id.register)).setTypeface(type);
	    
	    phone_number = (EditText) findViewById(R.id.phone_number);
	    phone_number.setTypeface(type);
	}
	
	public void register(View view)
	{
		dataManager.setUsername(phone_number.getText().toString());
		String msg = "";
		try {
			serverCommunicator.register();
			msg = "Success!";
		} catch (Exception e) {
			dataManager.setUsername(null);
			msg = "Failed to register... Try again later";
		}
		
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
		
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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