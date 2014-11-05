package com.info.st.persistence.store;

import com.info.st.persistence.SmartCartDBHelper;
import com.info.st.persistence.masteritems.MasterItemsTable;
import com.info.st.smartcart.R;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StoresTable {

	public static final String TABLE_STORES = "stores";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NOTE = "note";
	public static final String COLUMN_ICON = "icon";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_CREATION_DATE = "store_created_on";
	public static final String STORE_ITEM_COUNT = "item_count";
	public static final String QUERY_ITEM_COUNT = "COUNT(DISTINCT cartitems._id) as " + STORE_ITEM_COUNT;
	public static String[] projection = { TABLE_STORES + "." + COLUMN_ID, TABLE_STORES + "." + COLUMN_ADDRESS,
		TABLE_STORES + "." + COLUMN_ICON, TABLE_STORES + "." + COLUMN_NAME, TABLE_STORES + "." + COLUMN_CREATION_DATE, TABLE_STORES + "." + COLUMN_NOTE, QUERY_ITEM_COUNT };

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_STORES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null, " + COLUMN_ICON + " integer, " + COLUMN_ADDRESS
			+ " text, " + COLUMN_NOTE
			+ " text, " + COLUMN_CREATION_DATE
			+ " INTEGER NOT NULL DEFAULT (strftime('%s','now')) " + " );";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(SmartCartDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORES);
		onCreate(db);
	}

}
