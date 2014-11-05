package com.info.st.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.info.st.persistence.SmartCartContentProvider;
import com.info.st.persistence.store.StoresTable;
import com.info.st.smartcart.R;

public class StoreDetailsActivity extends Activity {

	private Uri storeUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_details);
		
		Bundle extras = getIntent().getExtras();
		
		addUpdateButtonHandler();
		
		// check from the saved Instance
		storeUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
				.getParcelable(SmartCartContentProvider.STORE_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			storeUri = extras
					.getParcelable(SmartCartContentProvider.STORE_ITEM_TYPE);

			fillData(storeUri);
		}
	}
	
	private void fillData(Uri uri) {
		String[] projection = StoresTable.projection;		
		
		EditText nameView = (EditText)this.findViewById(R.id.storename);
		ImageView iconView = (ImageView)this.findViewById(R.id.storeimage);
		EditText noteView = (EditText)this.findViewById(R.id.storenote);
		EditText addressView = (EditText)this.findViewById(R.id.storeaddress);
		
		
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			nameView.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(StoresTable.COLUMN_NAME)));
			noteView.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(StoresTable.COLUMN_NOTE)));
			addressView.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(StoresTable.COLUMN_ADDRESS)));
			iconView.setImageResource(cursor.getInt(cursor
					.getColumnIndexOrThrow(StoresTable.COLUMN_ICON)));
			
			
			//update title for the activity
			setTitle(cursor.getString(cursor
					.getColumnIndexOrThrow(StoresTable.COLUMN_NAME)));

			// always close the cursor
			cursor.close();
		}
	}
	
	
	public void addUpdateButtonHandler() {
		final Button addButton = (Button) findViewById(R.id.button_add_update);
		final StoreDetailsActivity activity = this;
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
		outState.putParcelable(SmartCartContentProvider.STORE_ITEM_TYPE, storeUri);
	}

//	@Override
//	protected void onPause() {
//		super.onPause();
//		saveState();
//	}

	private void saveState() {
		StoreDetailsActivity activity = this;
		
		EditText nameView = (EditText)activity.findViewById(R.id.storename);
		String name = nameView.getText().toString();
		EditText noteView = (EditText)activity.findViewById(R.id.storenote);
		String note = noteView.getText().toString();
		EditText addressView = (EditText)activity.findViewById(R.id.storeaddress);
		String address = addressView.getText().toString();


		//'store' table values
		ContentValues storeValues = new ContentValues();		
		storeValues.put(StoresTable.COLUMN_NAME, name);
		storeValues.put(StoresTable.COLUMN_ADDRESS, address);
		storeValues.put(StoresTable.COLUMN_NOTE, note);
		storeValues.put(StoresTable.COLUMN_ICON, R.drawable.ic_launcher);
				
		
		if (storeUri == null) {
			//New Store
			storeUri = getContentResolver().insert(
					SmartCartContentProvider.STORES_CONTENT_URI, storeValues);
		} else {
			//Update store
			getContentResolver().update(storeUri, storeValues, null, null);
		}
	}

}
