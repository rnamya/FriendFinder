package com.example.lookapp;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	private static final String DB_NAME = "lookapp.db";
	
	private static final String TABLE_CONTACT = "contact";
	
	private static final String TABLE_CONTACT_COLUMN_PHONE_NUMBER = "phone_number";
	private static final String TABLE_CONTACT_COLUMN_NAME = "name";
	private static final String TABLE_CONTACT_COLUMN_ALLOWED = "allowed";
	
	public Database(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createContact = "CREATE TABLE "+TABLE_CONTACT+"( "+TABLE_CONTACT_COLUMN_PHONE_NUMBER+" TEXT, " +
				TABLE_CONTACT_COLUMN_NAME+" TEXT, "+TABLE_CONTACT_COLUMN_ALLOWED+" INTEGER);";
		Log.d("INIT", "DATABASE");
		db.execSQL(createContact);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public void addContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO "+TABLE_CONTACT+" VALUES ('"+contact.getPhone()+"', '"+contact.getName()+"', '"+hasAccessToLocation(contact)+"');";
		Log.d("INSERT SQL", sql);
		db.execSQL(sql);
		
	}
	
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "DELETE FROM "+TABLE_CONTACT+" WHERE "+TABLE_CONTACT_COLUMN_PHONE_NUMBER+"='"+contact.getPhone()+"';";
		db.execSQL(sql);
		
	}
	
	public void deleteAll(Set<Contact> contacts) {
		for (Contact contact: contacts) {
			deleteContact(contact);
		}
	}
	
	public void addAll(Set<Contact> contacts) {
		for (Contact contact: contacts) {
			addContact(contact);
		}
	}
	
	public void updateName(String phone, String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE "
		+TABLE_CONTACT
		+" SET "
		+TABLE_CONTACT_COLUMN_NAME
		+"='"
		+name
		+"' WHERE "
		+TABLE_CONTACT_COLUMN_PHONE_NUMBER
		+"='"
		+phone
		+"';";
		db.execSQL(sql);
	}
	
	public void updateContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE "+TABLE_CONTACT+" SET "+TABLE_CONTACT_COLUMN_NAME+"='"+contact.getName()+"', "
					+TABLE_CONTACT_COLUMN_ALLOWED+"='"+hasAccessToLocation(contact)+"' WHERE "
					+TABLE_CONTACT_COLUMN_PHONE_NUMBER+"='"+contact.getPhone()+"';";
		Log.d("UPDATE SQL", sql);
		db.execSQL(sql);
		
		logAllContacts();
	}
	
	public void logAllContacts() {
		Log.d("ALL CONTACTS FROM DB", getAllContacts().toString());
	}
	
	public void updateAllContacts(Set<Contact> allContacts) {
		logAllContacts();
		Set<Contact> currentContacts = getAllContacts();
		Log.d("ALL CONTACTS IN DATABASE: ", currentContacts.toString());
		Log.d("ALL CONTACTS IN PHONE: ", allContacts.toString());
		
		Set<Contact> toBeAdded = new HashSet<>(allContacts);
		toBeAdded.removeAll(currentContacts);
		Log.d("TO BE ADDED: ", toBeAdded.toString());
		
		Set<Contact> toBeDeleted = new HashSet<>(currentContacts);
		toBeDeleted.addAll(allContacts);
		toBeDeleted.removeAll(allContacts);
		
		Log.d("TO BE REMOVED: ", toBeDeleted.toString());
			
		deleteAll(toBeDeleted);
		addAll(toBeAdded);
	}
	
	public Set<Contact> getAllContacts() {
		Set<Contact> contacts = new HashSet<>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		String sql = "SELECT *FROM "+TABLE_CONTACT+";";
		Cursor cursor = db.rawQuery(sql, null);
		
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact(cursor.getString(0), cursor.getString(1), hasAccessToLocation(cursor.getInt(2)), "Unknown");
				contacts.add(contact);
			} while(cursor.moveToNext());
		}
		
		Log.d("getAllContacts(): ", contacts.toString());
		return contacts;
	}
	
	public String getNameFromPhone(String phone)
	{
		Log.d("GETNAMEFROMPHONE", phone.toString());
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT "
				+TABLE_CONTACT_COLUMN_NAME
				+" FROM "
				+TABLE_CONTACT
				+" WHERE "
				+TABLE_CONTACT_COLUMN_PHONE_NUMBER
				+"='"
				+phone
				+"';";
		Cursor cursor = db.rawQuery(sql, null);
		Log.d("SQL", sql);
		if (cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		
		return "unknown";
	}	
	
	public boolean getHasAccessToLocationFromPhone(String phone)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT "
				+TABLE_CONTACT_COLUMN_ALLOWED 
				+" FROM "
				+TABLE_CONTACT
				+" WHERE "
				+TABLE_CONTACT_COLUMN_PHONE_NUMBER
				+"='"
				+phone
				+"';";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			return (cursor.getInt(0) == 1);
		}
		
		return false;
	}
	
	public int hasAccessToLocation(Contact contact) {
		return contact.hasAccessToLocation? 1 : 0; 
	}
	
	public boolean hasAccessToLocation(int value) {
		return value == 1;
	}
}
