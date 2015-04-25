package com.example.friendfinder;

import java.util.List;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PeopleNearbyAdapter extends ArrayAdapter<Contact> {

  private final List<Contact> list;
  private final Activity context;

  public PeopleNearbyAdapter(Activity context, List<Contact> list) {
	  
	  super(context, R.layout.list_item_contact_details, list);
	  this.context = context;
	  this.list = list;
	  
	  Log.d("PeopleNearbyAdapter constrcutor: ", this.list.toString());
  }

  // Creating a view for each list item
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View item = null;
    LayoutInflater inflator = context.getLayoutInflater();
    item = inflator.inflate(R.layout.list_item_contact_details, parent, false);
  
    Contact contact = list.get(position);
  
    TextView contactDetails = (TextView) item.findViewById(R.id.contactNamePhone);
    contactDetails.setText(contact.getName() + "\n" + contact.getPhone());
  
    TextView contactDistance = (TextView) item.findViewById(R.id.contactDistance);
    contactDistance.setText(contact.getDistance());
    
    
    return item;
  }
}