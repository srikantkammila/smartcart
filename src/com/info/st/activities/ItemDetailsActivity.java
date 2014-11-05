package com.info.st.activities;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.info.st.models.Item;
import com.info.st.models.Store;
import com.info.st.persistence.SmartCartContentProvider;
import com.info.st.persistence.SmartCartDBHelper;
import com.info.st.persistence.cartitems.CartItemsTable;
import com.info.st.persistence.masteritems.MasterItemsTable;
import com.info.st.persistence.store.StoresTable;
import com.info.st.smartcart.R;

public class ItemDetailsActivity extends Activity {

	private View view;
	private Spinner quantitySpinner, storeSpinner;
	private Uri cartItemUri;
	private int masterItemId;
	private long itemStoreId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_details);
		
		Bundle extras = getIntent().getExtras();
		
		// check from the saved Instance
		cartItemUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
				.getParcelable(SmartCartContentProvider.CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			cartItemUri = extras
					.getParcelable(SmartCartContentProvider.CONTENT_ITEM_TYPE);
			
			itemStoreId = extras.getLong("SelectStoreId");

			fillData(cartItemUri);
		}
		
		addDateTimePicker();
		
		addItemsOnQuantitySpinner();
		
		addStoreSpinner();
		
		addUpdateButtonHandler();
	}
	
	private void fillData(Uri uri) {
		String[] projection = {   "cartitems." + CartItemsTable.COLUMN_ID,
				 CartItemsTable.COLUMN_DUE_DATE_TIME,
				CartItemsTable.COLUMN_PURCHASE_STATE, MasterItemsTable.COLUMN_STORE_ID,
				MasterItemsTable.COLUMN_ICON, MasterItemsTable.COLUMN_NAME,
				MasterItemsTable.COLUMN_NOTE, MasterItemsTable.COLUMN_QUANTITY, 
				MasterItemsTable.COLUMN_QUANTITY_MEASURE, "masteritems."+MasterItemsTable.COLUMN_ID + " as item_id "};		
		
		EditText nameView = (EditText)this.findViewById(R.id.itemname);
		ImageView iconView = (ImageView)this.findViewById(R.id.itemimage);
		EditText noteView = (EditText)this.findViewById(R.id.itemnote);
		EditText quantityView = (EditText)this.findViewById(R.id.itemquantity);
		Spinner measureSpinner = (Spinner)this.findViewById(R.id.quantitymeasure);
		Spinner storeSpinner = (Spinner)this.findViewById(R.id.storespinner);
		EditText dueDateTimeView = (EditText)this.findViewById(R.id.itemduedate);
		String dueDateTime = (String)dueDateTimeView.getTag();
		
		
		Cursor cursor = (uri != null) ? getContentResolver().query(uri, projection, null, null,
				null) : null;
		if (cursor != null) {
			cursor.moveToFirst();
			String quantityMeasure = cursor.getString(cursor
					.getColumnIndexOrThrow(MasterItemsTable.COLUMN_QUANTITY_MEASURE));

			for (int i = 0; i < measureSpinner.getCount(); i++) {

				Item.QuantityMeasure s = (Item.QuantityMeasure) measureSpinner.getItemAtPosition(i);
				if (s.equals(quantityMeasure)) {
					measureSpinner.setSelection(i);
				}
			}

			nameView.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(MasterItemsTable.COLUMN_NAME)));
			noteView.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(MasterItemsTable.COLUMN_NOTE)));
			quantityView.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(MasterItemsTable.COLUMN_QUANTITY)));
			long dateTime = cursor.getLong(cursor
					.getColumnIndexOrThrow(CartItemsTable.COLUMN_DUE_DATE_TIME));
			itemStoreId = cursor.getLong(cursor
					.getColumnIndexOrThrow(MasterItemsTable.COLUMN_STORE_ID));
			Date dt = new Date(dateTime);
			DateFormat dateFormat = DateFormat.getDateTimeInstance();
			dueDateTimeView.setText(dateFormat.format(dt));
			iconView.setImageResource(cursor.getInt(cursor
					.getColumnIndexOrThrow(MasterItemsTable.COLUMN_ICON)));
			
			//update title for the activity
			setTitle(cursor.getString(cursor
					.getColumnIndexOrThrow(MasterItemsTable.COLUMN_NAME)));
			
			//master item ID. Use alias as the peoject has alias name
			masterItemId = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));

			// always close the cursor
			cursor.close();
		}
	}
	
	public void addDateTimePicker() {
		final EditText dueDateTimeView = (EditText) findViewById(R.id.itemduedate);
			
		dueDateTimeView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar currentDate = Calendar.getInstance();
				final String dueDateTime = dueDateTimeView.getText().toString();	
				if (dueDateTime != null) {
					DateFormat df = DateFormat.getDateTimeInstance();
					Date dt;
					try {
						dt = df.parse(dueDateTime);
						currentDate.setTime(dt);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
				int mYear = currentDate.get(Calendar.YEAR);
				int mMonth = currentDate.get(Calendar.MONTH);
				int mDay = currentDate.get(Calendar.DAY_OF_MONTH);


				DatePickerDialog mDatePicker = new DatePickerDialog(
						ItemDetailsActivity.this, new OnDateSetListener() {
							boolean fired = false;

							public void onDateSet(DatePicker datepicker,
									final int selectedyear, final int selectedmonth,
									final int selectedday) {
								if (fired == true) {
									return;
								} else {
									// first time fired
									fired = true;
								}
								final String selectedDate = "" + selectedday
										+ "/" + (selectedmonth + 1) + "/"
										+ selectedyear + ";";
								

								Calendar currentDate = Calendar.getInstance();
								final String dueDateTime = dueDateTimeView.getText().toString();	
								if (dueDateTime != null) {
									DateFormat df = DateFormat.getDateTimeInstance();
									Date dt;
									try {
										dt = df.parse(dueDateTime);
										currentDate.setTime(dt);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
								int hour = currentDate.get(Calendar.HOUR);
								int mint = currentDate.get(Calendar.MINUTE);
								int scnd = currentDate.get(Calendar.SECOND);


								TimePickerDialog timePickerDialog = new TimePickerDialog(
										ItemDetailsActivity.this,
										new TimePickerDialog.OnTimeSetListener() {
											boolean fired = false;

											@Override
											public void onTimeSet(
													TimePicker view,
													int hourOfDay, int minute) {
												if (fired == true) {
													return;
												} else {
													// first time fired
													fired = true;
												}

												String selectedTime = ""
														+ hourOfDay + ":"
														+ minute;
												
												DateFormat df = DateFormat.getDateTimeInstance();
												
												Calendar cal = Calendar.getInstance();
												cal.set(Calendar.YEAR, selectedyear);
												cal.set(Calendar.MONTH, selectedmonth);
												cal.set(Calendar.DAY_OF_MONTH, selectedday);
												cal.set(Calendar.HOUR, hourOfDay);
												cal.set(Calendar.MINUTE, minute);
												
												dueDateTimeView.setText(df.format(cal.getTime()));
//												dueDateTimeView.setTag(cal.getTime().getTime());

											}
										}, hour, mint, false);

								timePickerDialog.setTitle("Time");
								timePickerDialog.show();

							}
						}, mYear, mMonth, mDay);
				mDatePicker.setTitle("Item need by date");
				mDatePicker.show();
			}

		});
	}

	// add items into spinner dynamically
	public void addItemsOnQuantitySpinner() {

		quantitySpinner = (Spinner) findViewById(R.id.quantitymeasure);
		ArrayAdapter<Item.QuantityMeasure> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Item.QuantityMeasure.values());
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		quantitySpinner.setAdapter(dataAdapter);
	}
	
	
	// add items into spinner dynamically
	public void addStoreSpinner() {

		storeSpinner = (Spinner) findViewById(R.id.storespinner);
		storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				//Set selected store for this item
				if (id > -1) {
					itemStoreId = id;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
	    String[] from = new String[] { StoresTable.COLUMN_NAME, StoresTable.COLUMN_ID };
		// Fields on the UI to which we map
		int[] to = new int[] { android.R.id.text1};
		
		SQLiteDatabase sdb = new SmartCartDBHelper(this).getWritableDatabase();
		Cursor dbCursor = sdb.query(StoresTable.TABLE_STORES, from, null, null, null, null, null);
		MatrixCursor defaultValueCursor = new MatrixCursor(new String[] { StoresTable.COLUMN_NAME, StoresTable.COLUMN_ID });
		defaultValueCursor.addRow(new String[] {"Select Store", "-1"});
		Cursor[] cursors = {defaultValueCursor, dbCursor};
		MergeCursor mergeCursor = new MergeCursor(cursors);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, mergeCursor, from,
				to, 0);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		storeSpinner.setAdapter(adapter);
		
		if (itemStoreId > -1) {
			mergeCursor.moveToFirst();
	        for(int i = 0; i < mergeCursor.getCount(); i++)
	        {
	        	mergeCursor.moveToPosition(i);
	            if (mergeCursor.getLong(mergeCursor.getColumnIndex(StoresTable.COLUMN_ID)) == itemStoreId )
	            {
	            	
	            	storeSpinner.setSelection(i); //(false is optional)
	                break;
	            }
	        } 
		}

		
		
		
	}
	
	public void addUpdateButtonHandler() {
		final Button addButton = (Button) findViewById(R.id.button_add_update);
		final ItemDetailsActivity activity = this;
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveState();
				setResult(RESULT_OK);
				finish();
			}
		});
		
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(SmartCartContentProvider.CONTENT_ITEM_TYPE, cartItemUri);
	}

//	@Override
//	protected void onPause() {
//		super.onPause();
//		saveState();
//	}

	private void saveState() {
		ItemDetailsActivity activity = this;
		
		EditText nameView = (EditText)activity.findViewById(R.id.itemname);
		String name = nameView.getText().toString();
		EditText noteView = (EditText)activity.findViewById(R.id.itemnote);
		String note = noteView.getText().toString();
		EditText quantityView = (EditText)activity.findViewById(R.id.itemquantity);
		String quantity = quantityView.getText().toString();
		Spinner measureSpinner = (Spinner)activity.findViewById(R.id.quantitymeasure);
		String measure = measureSpinner.getSelectedItem().toString();
//		Spinner storeSpinner = (Spinner)activity.findViewById(R.id.storespinner);
//		Store store = (Store)storeSpinner.getSelectedItem();		
		EditText dueDateTimeView = (EditText)activity.findViewById(R.id.itemduedate);
		final String dueDateTimeStr = dueDateTimeView.getText().toString();	
		DateFormat df = DateFormat.getDateTimeInstance();
		long dueDateTime = 0;
		if (dueDateTimeStr != null) {			
			Date dt;
			try {
				dt = df.parse(dueDateTimeStr);
				dueDateTime = dt.getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	

		//cart item table values
		ContentValues cartValues = new ContentValues();		
		cartValues.put(CartItemsTable.COLUMN_PURCHASE_STATE, "false");
		cartValues.put(CartItemsTable.COLUMN_DUE_DATE_TIME, dueDateTime);
		
		//master item table values
		ContentValues masterValues = new ContentValues();
		masterValues.put(MasterItemsTable.COLUMN_QUANTITY, quantity);
		masterValues.put(MasterItemsTable.COLUMN_QUANTITY_MEASURE, measure);
		masterValues.put(MasterItemsTable.COLUMN_NOTE, note);
		masterValues.put(MasterItemsTable.COLUMN_ICON, R.drawable.ic_launcher);
		masterValues.put(MasterItemsTable.COLUMN_NAME, name);
		if (itemStoreId > -1) {
			masterValues.put(MasterItemsTable.COLUMN_STORE_ID, itemStoreId);
		} else {
			masterValues.putNull(MasterItemsTable.COLUMN_STORE_ID);
		}
		
		
		
		if (cartItemUri == null) {
			// New item
			//Create master item table entry
			cartItemUri = getContentResolver().insert(
					SmartCartContentProvider.MASTER_ITEMS_CONTENT_URI, masterValues);
			String masterId = cartItemUri.getLastPathSegment();
			cartValues.put(CartItemsTable.MASTER_ITEM_ID, masterId);
			cartItemUri = getContentResolver().insert(
					SmartCartContentProvider.CART_ITEMS_CONTENT_URI, cartValues);
		} else {
			// Update item
			getContentResolver().update(cartItemUri, cartValues, null, null);
			Uri masterItemUri = Uri.parse(SmartCartContentProvider.MASTER_ITEMS_CONTENT_URI + "/" + masterItemId);
			getContentResolver().update(masterItemUri, masterValues, null, null);
		}
	}

}
