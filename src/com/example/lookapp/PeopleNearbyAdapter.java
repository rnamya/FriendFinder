package com.example.lookapp;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PeopleNearbyAdapter extends ArrayAdapter<Contact> {

    private final List<Contact> list;
    private final Activity context;
    private final Typeface type;

    public PeopleNearbyAdapter(Activity context, List<Contact> list) {
	    super(context, R.layout.list_item_contact_details, list);
	    this.context = context;
	    this.list = list;
	    type = Typeface.createFromAsset(this.context.getAssets(), "Cookies.ttf");
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = this.context.getLayoutInflater();
        View item = inflator.inflate(R.layout.list_item_contact_details, parent, false);
        
        Contact contact = list.get(position);
        
        TextView contactName = (TextView) item.findViewById(R.id.contactName);
        SpannableString name = new SpannableString(contact.getName());
        name.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), 0);
        contactName.setText(name);
        contactName.setTypeface(type);
        
        TextView contactPhone = (TextView) item.findViewById(R.id.contactPhone);
        contactPhone.setText(contact.getPhone());
        contactPhone.setTypeface(type);
  
        TextView contactDistance = (TextView) item.findViewById(R.id.contactDistance);
        contactDistance.setText(contact.getDistance());
        contactDistance.setTypeface(type);
    
        return item;
    }
}