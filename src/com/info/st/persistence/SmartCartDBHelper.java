package com.info.st.persistence;

import com.info.st.persistence.cartitems.CartItemsTable;
import com.info.st.persistence.masteritems.MasterItemsTable;
import com.info.st.persistence.store.StoresTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SmartCartDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "smartcart.db";
	private static final int DATABASE_VERSION = 26;

	public SmartCartDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		MasterItemsTable.onCreate(database);
		StoresTable.onCreate(database);
		CartItemsTable.onCreate(database);		

	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		MasterItemsTable.onUpgrade(database, oldVersion, newVersion);
		StoresTable.onUpgrade(database, oldVersion, newVersion);
		CartItemsTable.onUpgrade(database, oldVersion, newVersion);

	}
}
