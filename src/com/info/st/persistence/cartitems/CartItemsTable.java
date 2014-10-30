package com.info.st.persistence.cartitems;

import com.info.st.persistence.SmartCartDBHelper;
import com.info.st.persistence.masteritems.MasterItemsTable;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CartItemsTable {

	public static final String TABLE_CART_ITEMS = "cartitems";
	public static final String COLUMN_ID = "_id";
	public static final String MASTER_ITEM_ID = "master_item_id";
	public static final String COLUMN_DUE_DATE_TIME = "due_date_time";
	public static final String COLUMN_PURCHASE_STATE = "purchase_state";
	public static final String COLUMN_CREATION_DATE = "cart_item_created_on";
	

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_CART_ITEMS
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_DUE_DATE_TIME + " real, "
			+ COLUMN_PURCHASE_STATE + " text, "
			+ COLUMN_CREATION_DATE + " INTEGER NOT NULL DEFAULT (strftime('%s','now')), "
			+ MASTER_ITEM_ID + " integer, "
			+ "FOREIGN KEY(" + MASTER_ITEM_ID + ") REFERENCES " + MasterItemsTable.TABLE_MASTER_ITEMS + "(" + MasterItemsTable.COLUMN_ID + ") );";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}


	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SmartCartDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEMS);
		onCreate(db);
	}

}
