package com.info.st.persistence;

import java.util.Arrays;
import java.util.HashSet;

import com.info.st.persistence.cartitems.CartItemsTable;
import com.info.st.persistence.masteritems.MasterItemsTable;
import com.info.st.persistence.store.StoresTable;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class SmartCartContentProvider extends ContentProvider {

	// database
	private SmartCartDBHelper database;

	// used for the UriMacher
	private static final int CART_ITEMS = 10;
	private static final int CART_ITEM_ID = 20;

	private static final int MASTER_ITEMS = 30;
	private static final int MASTER_ITEM_ID = 40;

	private static final int CART_STORES = 50;
	private static final int CART_STORE_ID = 60;
	
	
	private static final int CART_STORE_ITEMS = 70;

	private static final String AUTHORITY = "com.info.st.smartcart.contentprovider";

	private static final String BASE_PATH = "smartcart";
	private static final String CART_ITEMS_PATH = "cartitems";
	private static final String MASTER_ITEMS_PATH = "masteritems";
	private static final String STORES_PATH = "stores";
	private static final String STORE_ITEMS_PATH = "storeitemss";

	public static final Uri CART_ITEMS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + CART_ITEMS_PATH);

	public static final Uri MASTER_ITEMS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + MASTER_ITEMS_PATH);

	public static final Uri STORES_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + STORES_PATH);
	
	public static final Uri STORE_ITEMS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + STORE_ITEMS_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/cartitems";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/cartitem";

	public static final String STORE_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/stores";
	public static final String STORE_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/store";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, CART_ITEMS_PATH, CART_ITEMS);
		sURIMatcher.addURI(AUTHORITY, CART_ITEMS_PATH + "/#", CART_ITEM_ID);
		sURIMatcher.addURI(AUTHORITY, MASTER_ITEMS_PATH, MASTER_ITEMS);
		sURIMatcher.addURI(AUTHORITY, MASTER_ITEMS_PATH + "/#", MASTER_ITEM_ID);
		sURIMatcher.addURI(AUTHORITY, STORES_PATH, CART_STORES);
		sURIMatcher.addURI(AUTHORITY, STORES_PATH + "/#", CART_STORE_ID);
		sURIMatcher.addURI(AUTHORITY, STORE_ITEMS_PATH, CART_STORE_ITEMS);
	}

	@Override
	public boolean onCreate() {
		database = new SmartCartDBHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		String groupBy = null;
		String having = null;
		SQLiteDatabase db = database.getWritableDatabase();	

		//Query for Cart Items
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		StringBuilder cartItemQuryStr = new StringBuilder();
		cartItemQuryStr.append(CartItemsTable.TABLE_CART_ITEMS
				+ " as cartitems ");
		cartItemQuryStr.append(" Left Outer Join ");
		cartItemQuryStr.append(MasterItemsTable.TABLE_MASTER_ITEMS
				+ " as masteritems on ");
		cartItemQuryStr.append("cartitems." + CartItemsTable.MASTER_ITEM_ID);
		cartItemQuryStr.append("= masteritems." + MasterItemsTable.COLUMN_ID);
		
		//Query for Cart Items in a store
		StringBuilder cartStoreItemQuery = new StringBuilder();
		cartStoreItemQuery.append(cartItemQuryStr);
		cartStoreItemQuery.append(" Left Outer Join ");
		cartStoreItemQuery.append(StoresTable.TABLE_STORES + " as stores on ");
		cartStoreItemQuery.append(" masteritems." + MasterItemsTable.COLUMN_STORE_ID);
		cartStoreItemQuery.append("= stores." + StoresTable.COLUMN_ID);
		
		//Query for store list (with items count from catrtitems table)
		StringBuilder storeListQuery = new StringBuilder();
		storeListQuery.append(StoresTable.TABLE_STORES + " as stores ");
		storeListQuery.append(" Left Outer Join ");
		storeListQuery.append(MasterItemsTable.TABLE_MASTER_ITEMS
				+ " as masteritems on ");
		storeListQuery.append("stores." + StoresTable.COLUMN_ID);
		storeListQuery.append("= masteritems." + MasterItemsTable.COLUMN_STORE_ID);
		storeListQuery.append(" Left Outer Join ");
		storeListQuery.append(CartItemsTable.TABLE_CART_ITEMS
				+ " as cartitems on ");
		storeListQuery.append("cartitems." + CartItemsTable.MASTER_ITEM_ID);
		storeListQuery.append("= masteritems." + MasterItemsTable.COLUMN_ID);
		
		//Query for master items grid. (Exclude the cart items from mater items)
		StringBuilder masterItemsGridQuery = new StringBuilder();
		masterItemsGridQuery.append("select icon, name, note, _id from masteritems where masteritems._id not in (select master_item_id from cartitems)");
		
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case CART_ITEMS:
			queryBuilder.setTables(cartItemQuryStr.toString());
			break;
		case MASTER_ITEMS:
			queryBuilder.setTables(MasterItemsTable.TABLE_MASTER_ITEMS);
			Cursor cur = db.rawQuery(masterItemsGridQuery.toString(), null);
			cur.setNotificationUri(getContext().getContentResolver(), uri);
			return cur;
		case CART_ITEM_ID:
			// check if the caller has requested a column which does not exists
			checkFullItemColumns(projection);
			// Set the table
			queryBuilder.setTables(cartItemQuryStr.toString());
			// adding the ID to the original query
			queryBuilder.appendWhere("cartitems." + CartItemsTable.COLUMN_ID
					+ "=" + uri.getLastPathSegment());
			break;
		case MASTER_ITEM_ID:
			// check if the caller has requested a column which does not exists
			checkMasterItemColumns(projection);
			// adding the ID to the original query
			queryBuilder.setTables(MasterItemsTable.TABLE_MASTER_ITEMS);
			queryBuilder.appendWhere(MasterItemsTable.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		case CART_STORES:
			queryBuilder.setTables(storeListQuery.toString());
			groupBy = " stores._id ";
			break;
		case CART_STORE_ID:
			// check if the caller has requested a column which does not exists
			checkStoreColumns(projection);
			// adding the ID to the original query
			queryBuilder.setTables(StoresTable.TABLE_STORES);
			queryBuilder.appendWhere(StoresTable.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		case CART_STORE_ITEMS:
			// check if the caller has requested a column which does not exists
//			checkCartItemStoreColumns(projection);
			// adding the ID to the original query
			queryBuilder.setTables(cartStoreItemQuery.toString());
//			queryBuilder.appendWhere("stores."+StoresTable.COLUMN_ID + "="
//					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

			
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, having, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		long id = 0;
		switch (uriType) {
		case CART_ITEMS:
			id = sqlDB.insert(CartItemsTable.TABLE_CART_ITEMS, null, values);
			break;
		case MASTER_ITEMS:
			id = sqlDB
					.insert(MasterItemsTable.TABLE_MASTER_ITEMS, null, values);
			break;
		case CART_STORES:
			id = sqlDB.insert(StoresTable.TABLE_STORES, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case CART_ITEMS:
			rowsDeleted = sqlDB.delete(CartItemsTable.TABLE_CART_ITEMS,
					selection, selectionArgs);
			break;
		case CART_ITEM_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(CartItemsTable.TABLE_CART_ITEMS,
						CartItemsTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(CartItemsTable.TABLE_CART_ITEMS,
						CartItemsTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		case MASTER_ITEMS:
			rowsDeleted = sqlDB.delete(MasterItemsTable.TABLE_MASTER_ITEMS,
					selection, selectionArgs);
			break;
		case MASTER_ITEM_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(MasterItemsTable.TABLE_MASTER_ITEMS,
						MasterItemsTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(MasterItemsTable.TABLE_MASTER_ITEMS,
						MasterItemsTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		case CART_STORES:
			rowsDeleted = sqlDB.delete(StoresTable.TABLE_STORES, selection,
					selectionArgs);
			break;
		case CART_STORE_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(StoresTable.TABLE_STORES,
						StoresTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(StoresTable.TABLE_STORES,
						StoresTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case CART_ITEMS:
			rowsUpdated = sqlDB.update(CartItemsTable.TABLE_CART_ITEMS, values,
					selection, selectionArgs);
			break;

		case CART_ITEM_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(CartItemsTable.TABLE_CART_ITEMS,
						values, CartItemsTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(CartItemsTable.TABLE_CART_ITEMS,
						values, CartItemsTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;

		case MASTER_ITEMS:
			rowsUpdated = sqlDB.update(MasterItemsTable.TABLE_MASTER_ITEMS,
					values, selection, selectionArgs);
			break;
		case MASTER_ITEM_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(MasterItemsTable.TABLE_MASTER_ITEMS,
						values, MasterItemsTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(MasterItemsTable.TABLE_MASTER_ITEMS,
						values, MasterItemsTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		case CART_STORES:
			rowsUpdated = sqlDB.update(StoresTable.TABLE_STORES, values,
					selection, selectionArgs);
			break;

		case CART_STORE_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(StoresTable.TABLE_STORES, values,
						StoresTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(StoresTable.TABLE_STORES, values,
						StoresTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkCartItemColumns(String[] projection) {
		String[] available = { CartItemsTable.COLUMN_ID,
				CartItemsTable.COLUMN_DUE_DATE_TIME,
				CartItemsTable.COLUMN_PURCHASE_STATE,
				CartItemsTable.MASTER_ITEM_ID };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

	private void checkMasterItemColumns(String[] projection) {
		String[] available = { MasterItemsTable.COLUMN_ID,
				MasterItemsTable.COLUMN_ICON, MasterItemsTable.COLUMN_NAME,
				MasterItemsTable.COLUMN_NOTE, MasterItemsTable.COLUMN_QUANTITY,
				MasterItemsTable.COLUMN_QUANTITY_MEASURE };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

	private void checkFullItemColumns(String[] projection) {
		String[] available = { "cartitems." + CartItemsTable.COLUMN_ID,
				CartItemsTable.COLUMN_DUE_DATE_TIME,
				CartItemsTable.COLUMN_PURCHASE_STATE,
				CartItemsTable.MASTER_ITEM_ID,
				"masteritems." + MasterItemsTable.COLUMN_ID + " as item_id ",
				MasterItemsTable.COLUMN_ICON, MasterItemsTable.COLUMN_NAME,
				MasterItemsTable.COLUMN_NOTE, MasterItemsTable.COLUMN_QUANTITY,
				MasterItemsTable.COLUMN_QUANTITY_MEASURE, MasterItemsTable.COLUMN_STORE_ID };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

	private void checkStoreColumns(String[] projection) {
		String[] available = StoresTable.projection;
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

}
