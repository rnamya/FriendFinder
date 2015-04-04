package com.example.friendfinder;

import java.util.List;

import android.app.Activity;
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
  private OnCheckedChangeListener hasAccessChangedListener = new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      	Contact element = (Contact) ((CheckBox) buttonView).getTag();
      	Log.d("BUTTONVIEW", buttonView.toString());
      	Log.d("CONTACT IN ONCHECKEDCHANGE", element.toString());
      	element.setHasAccessToLocation(isChecked);
      	Log.d("CONTACT CHANGE", element.toString());
      	Log.d("CONTACTS AFTER CHANGE: ", list.toString());
      	dataManager.updateContact(element);
      }
    };

  public ContactAdapter(Activity context, List<Contact> list, DataManager dataManager) {
	  super(context, R.layout.list_item_contact, list);
	  this.context = context;
	  this.list = list;
	  this.dataManager = dataManager;
	  
	  Log.d("ContactAdapter constrcutor: ", this.list.toString());
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View item = null;
    LayoutInflater inflator = context.getLayoutInflater();
    item = inflator.inflate(R.layout.list_item_contact, parent, false);
  
    Contact contact = list.get(position);
  
    TextView contactDetails = (TextView) item.findViewById(R.id.contactNamePhone);
    contactDetails.setText(contact.getName() + "\n" + contact.getPhone());
  
    CheckBox hasAccess = (CheckBox) item.findViewById(R.id.contactHasAccessToLocation);
    hasAccess.setTag(contact);
    hasAccess.setChecked(contact.hasAccessToLocation);
    
    hasAccess.setOnCheckedChangeListener(hasAccessChangedListener);
    
    return item;
  }
}