package com.example.lookapp;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {

  private final List<Contact> list;
  private final Activity context;
  private final DataManager dataManager;
  private final Typeface type;
  
  private OnCheckedChangeListener hasAccessChangedListener = new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      	Contact element = (Contact) ((CheckBox) buttonView).getTag();
      	element.setHasAccessToLocation(isChecked);
      	dataManager.updateContact(element);
      }
    };

  public ContactAdapter(Activity context, List<Contact> list, DataManager dataManager) {
	  super(context, R.layout.list_item_contact, list);
	  this.context = context;
	  this.list = list;
	  this.dataManager = dataManager;
	  this.type = Typeface.createFromAsset(this.context.getAssets(), "Cookies.ttf");
	  
	  Log.d("ContactAdapter constrcutor: ", this.list.toString());
  }

  // Creating a view for each list item
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflator = context.getLayoutInflater();
    View item = inflator.inflate(R.layout.list_item_contact, parent, false);
  
    Contact contact = list.get(position);
  
    TextView contactName = (TextView) item.findViewById(R.id.contactName);
    contactName.setText(contact.getName());
    contactName.setTypeface(type);
    
    TextView contactPhone = (TextView) item.findViewById(R.id.contactPhone);
    contactPhone.setText(contact.getPhone());
    contactPhone.setTypeface(type);
  
    CheckBox hasAccess = (CheckBox) item.findViewById(R.id.contactHasAccessToLocation);
    hasAccess.setTag(contact);
    
    // to display whether ticked or not
    hasAccess.setChecked(contact.hasAccessToLocation);
    
    // to set listener to change tick later
    hasAccess.setOnCheckedChangeListener(hasAccessChangedListener);
    
    return item;
  }
}
