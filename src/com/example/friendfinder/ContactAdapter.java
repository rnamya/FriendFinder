package com.example.friendfinder;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {

  private final List<Contact> list;
  private final Activity context;
  private final DataManager dataManager;

  public ContactAdapter(Activity context, List<Contact> list, DataManager dataManager) {
	  super(context, R.layout.list_item_contact, list);
	  this.context = context;
	  this.list = list;
	  this.dataManager = dataManager;
  }

  static class ViewHolder {
    protected TextView text;
    protected CheckBox checkbox;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = null;
    if (convertView == null) {
      LayoutInflater inflator = context.getLayoutInflater();
      view = inflator.inflate(R.layout.list_item_contact, null);
      final ViewHolder viewHolder = new ViewHolder();
      viewHolder.text = (TextView) view.findViewById(R.id.textView1);
      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkBox1);
      viewHolder.checkbox
          .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	Contact element = (Contact) viewHolder.checkbox.getTag();
            	element.setHasAccessToLocation(isChecked);
            	Log.d("CONTACT CHANGE", element.toString());
            	dataManager.updateContact(element);
            }
          });
      viewHolder.checkbox.setChecked(this.list.get(position).hasAccessToLocation);
      Log.d("CONTACT", this.list.get(position).toString());
      view.setTag(viewHolder);
      viewHolder.checkbox.setTag(list.get(position));
    } else {
      view = convertView;
      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
    }
    ViewHolder holder = (ViewHolder) view.getTag();
    holder.text.setText(list.get(position).getName());
    holder.checkbox.setChecked(list.get(position).isHasAccessToLocation());
    return view;
  }
}