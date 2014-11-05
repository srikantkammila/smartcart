package com.info.st.persistence.masteritems;

import java.util.Date;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.info.st.persistence.SmartCartDBHelper;
import com.info.st.persistence.store.StoresTable;
import com.info.st.smartcart.R;

public class MasterItemsTable {

	public static final String TABLE_MASTER_ITEMS = "masteritems";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NOTE = "note";
	public static final String COLUMN_ICON = "icon";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_CREATION_DATE = "created_on";
	public static final String COLUMN_MODIFIED_DATE = "modified_on";
	public static final String COLUMN_ITEM_PRIORITY = "priority";
	public static final String COLUMN_STORE_ID = "store_id";
	public static final String COLUMN_QUANTITY_MEASURE = "quantity_measure";
	public static String[] projection = { COLUMN_ICON, COLUMN_NAME,
		COLUMN_NOTE,  COLUMN_QUANTITY, COLUMN_QUANTITY_MEASURE, COLUMN_ID, COLUMN_STORE_ID};
	

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_MASTER_ITEMS
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null, " + COLUMN_NOTE + " text, "
			+ COLUMN_ICON + " integer, " + COLUMN_QUANTITY + " text, "
			+ COLUMN_CREATION_DATE + " INTEGER NOT NULL DEFAULT (strftime('%s','now')), "
			+ COLUMN_MODIFIED_DATE + " datetime default (datetime('now')), "
			+ COLUMN_STORE_ID + " integer, "
			+ COLUMN_ITEM_PRIORITY + " integer, "
			+ COLUMN_QUANTITY_MEASURE + " text, "
			+ "FOREIGN KEY(" + COLUMN_STORE_ID + ") REFERENCES " + StoresTable.TABLE_STORES + "(" + StoresTable.COLUMN_ID + ") );";
	
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, "New");
		values.put(COLUMN_NOTE, "${#New#}$");
		values.put(COLUMN_ICON, R.drawable.add);
		values.put(COLUMN_ITEM_PRIORITY, 1);
		database.insert(TABLE_MASTER_ITEMS, null, values);
	}


	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SmartCartDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_ITEMS);
		onCreate(db);
	}

}
