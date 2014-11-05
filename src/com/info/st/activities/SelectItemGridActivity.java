package com.info.st.activities;

import java.util.Date;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.info.st.persistence.SmartCartContentProvider;
import com.info.st.persistence.cartitems.CartItemsTable;
import com.info.st.persistence.masteritems.MasterItemsTable;
import com.info.st.smartcart.R;

public class SelectItemGridActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener{
	
	GridView gridView;
	SimpleCursorAdapter adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_item_grid);
 
		fillData();

	}
	
	private void fillData () {
		gridView = (GridView) findViewById(R.id.itemsgridview);
		 
//		gridView.setAdapter(new ItemGridAdapter(this, ItemHistoryAggregator.getInstance().getItems()));
		
		String[] from = new String[] { MasterItemsTable.COLUMN_NAME, MasterItemsTable.COLUMN_ICON };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.grid_item_label, R.id.grid_item_image };

		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.select_item_grid_fragment, null, from,
				to, 0);
		SimpleCursorAdapter.ViewBinder viewBinder = new CustomCursorViewBinder();
		adapter.setViewBinder(viewBinder);
		
		gridView.setAdapter(adapter);
 
		gridView.setOnItemClickListener(this);
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (view.findViewById(R.id.grid_item_label) != null && view.findViewById(R.id.grid_item_label).getTag() != null && view.findViewById(R.id.grid_item_label).getTag().equals("${#New#}$")) {
			//new Item activity
			Intent itemGridIntent = new Intent(this, ItemDetailsActivity.class);
			startActivity(itemGridIntent);
			
		} else {
			Cursor cursor = adapter.getCursor();
			boolean cursorPos = cursor.moveToPosition(position);
			String itemName = null;
			int masterItemId  = 0;
			if (cursorPos) {
				itemName = cursor.getString(cursor.getColumnIndexOrThrow(MasterItemsTable.COLUMN_NAME));
				masterItemId = cursor.getInt(cursor.getColumnIndexOrThrow(MasterItemsTable.COLUMN_ID));				
			}
			
			if (itemName != null && masterItemId > 0) {
				
				addItemInCart(masterItemId);
				
				getLoaderManager().restartLoader(0, null, this);
				
				String  displayMsg = "Item : " + ((TextView) view.findViewById(R.id.grid_item_label))
						   .getText() + " added to cart";
				Toast.makeText(
				   getApplicationContext(), displayMsg, Toast.LENGTH_SHORT).show();
				
				
			}

		}
	}
	
	public void addItemInCart(int masterItemId ) {
		//cart item table values
		ContentValues cartValues = new ContentValues();		
		cartValues.put(CartItemsTable.COLUMN_PURCHASE_STATE, "false");
		cartValues.put(CartItemsTable.COLUMN_DUE_DATE_TIME, new Date().getTime());
		cartValues.put(CartItemsTable.MASTER_ITEM_ID, masterItemId);
		
		getContentResolver().insert(
				SmartCartContentProvider.CART_ITEMS_CONTENT_URI, cartValues);
		
	}
	
//	@Override
//	protected void onResume() {		
//		super.onResume();
//		this.onCreate(null);
//	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = MasterItemsTable.projection;
		CursorLoader cursorLoader = new CursorLoader(this,
				SmartCartContentProvider.MASTER_ITEMS_CONTENT_URI, projection, null, null, null);
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// data is not available anymore, delete reference
		adapter.swapCursor(null);
	}
	
	
private class CustomCursorViewBinder implements SimpleCursorAdapter.ViewBinder{
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			boolean newItemNote = cursor.getString(cursor.getColumnIndex(MasterItemsTable.COLUMN_NOTE)).equals("${#New#}$");
			
			if(view instanceof TextView && view.getId() == R.id.grid_item_label && newItemNote) {
	    		((TextView)view).setTag("${#New#}$");
	    	}
	    	return false;
		}
		
	}
}
