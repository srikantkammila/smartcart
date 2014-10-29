package com.info.st.persistence;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CartItemsTable {

	public static final String TABLE_CART_ITEMS = "cartitems";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NOTE = "note";
	public static final String COLUMN_ICON = "icon";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_QUANTITY_MEASURE = "quantity_measure";
	public static final String COLUMN_DUE_DATE_TIME = "due_date_time";
	public static final String COLUMN_PURCHASE_STATE = "purchase_state";
	

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_CART_ITEMS
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null, " + COLUMN_NOTE + " text, "
			+ COLUMN_ICON + " integer, " + COLUMN_QUANTITY + " text, "
			+ COLUMN_QUANTITY_MEASURE + " text, " + COLUMN_DUE_DATE_TIME + " real, "
			+ COLUMN_PURCHASE_STATE + " text );";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}


	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CartItemsDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEMS);
		onCreate(db);
	}

}
