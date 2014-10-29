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
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.info.st.adapters.StoreSpinnerListAdapter;
import com.info.st.data.aggregators.StoreAggregator;
import com.info.st.models.Item;
import com.info.st.models.Store;
import com.info.st.persistence.CartItemsContentProvider;
import com.info.st.persistence.CartItemsTable;
import com.info.st.smartcart.R;

public class ItemDetailsActivity extends Activity {

	private View view;
	private Spinner quantitySpinner, storeSpinner;
	private Uri todoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_details);
		
		Bundle extras = getIntent().getExtras();		
		
		addDateTimePicker();
		
		addItemsOnQuantitySpinner();
		
		addItemsOnStoreSpinner();
		
		addUpdateButtonHandler();
		
		// check from the saved Instance
		todoUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
				.getParcelable(CartItemsContentProvider.CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			todoUri = extras
					.getParcelable(CartItemsContentProvider.CONTENT_ITEM_TYPE);

			fillData(todoUri);
		}
	}
	
	private void fillData(Uri uri) {
		String[] projection = {  CartItemsTable.COLUMN_ID,
				CartItemsTable.COLUMN_ICON, CartItemsTable.COLUMN_NAME,
				CartItemsTable.COLUMN_NOTE, CartItemsTable.COLUMN_DUE_DATE_TIME,
				CartItemsTable.COLUMN_PURCHASE_STATE, CartItemsTable.COLUMN_QUANTITY, CartItemsTable.COLUMN_QUANTITY_MEASURE};
		
		
		EditText nameView = (EditText)this.findViewById(R.id.itemname);
		ImageView iconView = (ImageView)this.findViewById(R.id.itemimage);
		EditText noteView = (EditText)this.findViewById(R.id.itemnote);
		EditText quantityView = (EditText)this.findViewById(R.id.itemquantity);
		Spinner measureSpinner = (Spinner)this.findViewById(R.id.quantitymeasure);
		Spinner storeSpinner = (Spinner)this.findViewById(R.id.storespinner);
		EditText dueDateTimeView = (EditText)this.findViewById(R.id.itemduedate);
		String dueDateTime = (String)dueDateTimeView.getTag();
		
		
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			String quantityMeasure = cursor.getString(cursor
					.getColumnIndexOrThrow(CartItemsTable.COLUMN_QUANTITY_MEASURE));

			for (int i = 0; i < measureSpinner.getCount(); i++) {

				Item.QuantityMeasure s = (Item.QuantityMeasure) measureSpinner.getItemAtPosition(i);
				if (s.equals(quantityMeasure)) {
					measureSpinner.setSelection(i);
				}
			}

			nameView.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(CartItemsTable.COLUMN_NAME)));
			noteView.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(CartItemsTable.COLUMN_NOTE)));
			quantityView.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(CartItemsTable.COLUMN_QUANTITY)));
			long dateTime = cursor.getLong(cursor
					.getColumnIndexOrThrow(CartItemsTable.COLUMN_DUE_DATE_TIME));
			Date dt = new Date(dateTime);
			DateFormat dateFormat = DateFormat.getDateTimeInstance();
			dueDateTimeView.setText(dateFormat.format(dt));
			iconView.setImageResource(cursor.getInt(cursor
					.getColumnIndexOrThrow(CartItemsTable.COLUMN_ICON)));
			
			//update title for the activity
			setTitle(cursor.getString(cursor
					.getColumnIndexOrThrow(CartItemsTable.COLUMN_NAME)));

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
	public void addItemsOnStoreSpinner() {

		storeSpinner = (Spinner) findViewById(R.id.storespinner);
		StoreSpinnerListAdapter dataAdapter = new StoreSpinnerListAdapter(this, android.R.layout.simple_spinner_item, new StoreAggregator().getInitStores());
//		ArrayAdapter<Store> dataAdapter = new ArrayAdapter<Store>(this, android.R.layout.simple_spinner_item, new StoreAggregator().getInitStores());
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		storeSpinner.setAdapter(dataAdapter);
		storeSpinner.setSelection(dataAdapter.getCount());
		
//		storeSpinner.setOnItemSelectedListener(listener);
		
	}
	
	public void addUpdateButtonHandler() {
		final Button addButton = (Button) findViewById(R.id.button_add_update);
		final ItemDetailsActivity activity = this;
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setResult(RESULT_OK);
				finish();
//				EditText nameView = (EditText)activity.findViewById(R.id.itemname);
//				String name = nameView.getText().toString();
//				EditText noteView = (EditText)activity.findViewById(R.id.itemnote);
//				String note = noteView.getText().toString();
//				EditText quantityView = (EditText)activity.findViewById(R.id.itemquantity);
//				String quantity = quantityView.getText().toString();
//				Spinner measureSpinner = (Spinner)activity.findViewById(R.id.quantitymeasure);
//				String measure = measureSpinner.getSelectedItem().toString();
//				Spinner storeSpinner = (Spinner)activity.findViewById(R.id.storespinner);
//				Store store = (Store)storeSpinner.getSelectedItem();
//				
//				EditText dueDateTimeView = (EditText)activity.findViewById(R.id.itemduedate);
//				String dueDateTime = dueDateTimeView.getText().toString();
//				
//				Item newItem = new Item(name, R.drawable.ic_launcher, quantity, store, 0, note, false, new Date(), Item.QuantityMeasure.GALON);
//				
//				System.out.println("name: " + name +  " note: " + note + " quantity: "+ quantity + " " + measure + " due:" + dueDateTime);
//				ItemHistoryAggregator.getInstance().addItem(newItem);
//				activity.finish();
			}
		});
		
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(CartItemsContentProvider.CONTENT_ITEM_TYPE, todoUri);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

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
		Spinner storeSpinner = (Spinner)activity.findViewById(R.id.storespinner);
		Store store = (Store)storeSpinner.getSelectedItem();		
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

		

		ContentValues values = new ContentValues();
		values.put(CartItemsTable.COLUMN_NAME, name);
		values.put(CartItemsTable.COLUMN_PURCHASE_STATE, "false");
		values.put(CartItemsTable.COLUMN_QUANTITY, quantity);
		values.put(CartItemsTable.COLUMN_QUANTITY_MEASURE, measure);
		values.put(CartItemsTable.COLUMN_NOTE, note);
		values.put(CartItemsTable.COLUMN_ICON, R.drawable.ic_launcher);
		values.put(CartItemsTable.COLUMN_DUE_DATE_TIME, dueDateTime);

		if (todoUri == null) {
			// New item
			todoUri = getContentResolver().insert(
					CartItemsContentProvider.CONTENT_URI, values);
		} else {
			// Update item
			getContentResolver().update(todoUri, values, null, null);
		}
	}

}
