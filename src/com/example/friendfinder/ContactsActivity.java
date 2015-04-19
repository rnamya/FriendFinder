package com.example.friendfinder;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends Activity {

	public TextView listView;
	public DataManager dataManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
        
		ListView listView = (ListView) findViewById(R.id.list);
		dataManager = new DataManager(getApplicationContext());
        ContactAdapter adapter = new ContactAdapter(this, new ArrayList<Contact>(dataManager.getAllContacts()), dataManager);
        listView.setAdapter(adapter);
        
        /*
        StringBuilder output = new StringBuilder();
        for (Contact item: contactsList) {
            String str = item.name + " " + item.phone + "\n";
            output.append(str);
        }
        TextView outputText = (TextView) findViewById(R.id.textView1);
        outputText.setText(output);
		*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
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