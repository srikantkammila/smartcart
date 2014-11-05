package com.info.st.activities;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.info.st.common.DialogHandler;
import com.info.st.persistence.SmartCartContentProvider;
import com.info.st.persistence.cartitems.CartItemsTable;
import com.info.st.persistence.masteritems.MasterItemsTable;
import com.info.st.persistence.store.StoresTable;
import com.info.st.smartcart.R;

public class StoreItemListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnItemLongClickListener, OnItemClickListener {

	SimpleCursorAdapter adapter;
	Uri storeUri;
//	boolean deleteButtonActive = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_item_list);
		
		Bundle extras = getIntent().getExtras();
		
		// check from the saved Instance
		storeUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
				.getParcelable(SmartCartContentProvider.STORE_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			storeUri = extras
					.getParcelable(SmartCartContentProvider.STORE_ITEM_TYPE);			
		}
		
		fillData();
		
	    getListView().setOnItemLongClickListener(this);
	    getListView().setOnItemClickListener(this);
	}
	

	@Override
	public void onResume() {
		super.onResume();
		String storeId = null;
		if (storeUri != null) {
			storeId = storeUri.getLastPathSegment();
		} 
		Bundle bundle = new Bundle();
		bundle.putSerializable("StoreId", storeId);
		getLoaderManager().restartLoader(0, bundle, this);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// TODO Auto-generated method stub
		System.out.println("Store Item Click");
		super.onListItemClick(l, v, position, id);
	}
	
	private void fillData() {
		String storeId = null;
		if (storeUri != null) {
			storeId = storeUri.getLastPathSegment();
		} else {
			finish();
		}
	    String[] from = new String[] { MasterItemsTable.COLUMN_NAME, MasterItemsTable.COLUMN_ICON, CartItemsTable.COLUMN_PURCHASE_STATE };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.label, R.id.icon, R.id.itempurchasesate };
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("StoreId", storeId);

		getLoaderManager().initLoader(0, bundle, this);
		adapter = new CustomCursorAdapter(this, R.layout.item_fragment, null, from,
				to, 0);
		SimpleCursorAdapter.ViewBinder viewBinder = new CustomCursorViewBinder();
		adapter.setViewBinder(viewBinder);

		setListAdapter(adapter);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> viewGroup, View view, int position, long id) {
		//Show item details view
		Intent i = new Intent(this, ItemDetailsActivity.class);
		Uri cartItemUri = Uri.parse(SmartCartContentProvider.CART_ITEMS_CONTENT_URI + "/" + id);
		i.putExtra(SmartCartContentProvider.CONTENT_ITEM_TYPE, cartItemUri);

		startActivity(i);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> viewGroup, View view, int position, long id) {
		//Mark item as purchased and update DB
		CheckBox cb = (CheckBox)view.findViewById(R.id.itempurchasesate);
		boolean itemPurchaseState = cb.isChecked();
		
		Uri itemUri = Uri.parse(SmartCartContentProvider.CART_ITEMS_CONTENT_URI + "/" + id);		
		ContentValues values = new ContentValues();
		if (itemPurchaseState) {
			//current state true. Change to false, persist data,
			values.put(CartItemsTable.COLUMN_PURCHASE_STATE, "false");
		} else {
			//current state true. Change to false, persist data,
			values.put(CartItemsTable.COLUMN_PURCHASE_STATE, "true");
		}		
		
		this.getContentResolver().update(itemUri, values, null, null);
		
		String storeId = null;
		if (storeUri != null) {
			storeId = storeUri.getLastPathSegment();
		} else {
			finish();
		}
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("StoreId", storeId);
		
		getLoaderManager().restartLoader(0, bundle, this);
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		String[] projection = { "cartitems." + CartItemsTable.COLUMN_ID + " as _id", CartItemsTable.COLUMN_DUE_DATE_TIME,
				CartItemsTable.COLUMN_PURCHASE_STATE,
				"masteritems." + MasterItemsTable.COLUMN_ICON + " as icon", "masteritems." + MasterItemsTable.COLUMN_NAME + " as name",
				"masteritems." + MasterItemsTable.COLUMN_NOTE + " as note",  MasterItemsTable.COLUMN_QUANTITY,
				MasterItemsTable.COLUMN_QUANTITY_MEASURE , "masteritems."+MasterItemsTable.COLUMN_ID + " as item_id",
				" stores." + StoresTable.COLUMN_NAME + " as store_name"};
		String soreId = (String)bundle.getSerializable("StoreId");
		String[] args = new String[]{soreId};
		CursorLoader cursorLoader = new CursorLoader(this,
				SmartCartContentProvider.STORE_ITEMS_CONTENT_URI, projection, " stores._id=? ", args, CartItemsTable.COLUMN_PURCHASE_STATE + " ASC, " + CartItemsTable.COLUMN_CREATION_DATE + " DESC");
		
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
			boolean purchaseState = cursor.getString(cursor.getColumnIndex(CartItemsTable.COLUMN_PURCHASE_STATE)).equals("true");
			if(view instanceof CheckBox) {
	    		CheckBox cb = (CheckBox) view;	    		
	    		cb.setChecked(purchaseState);
	    		return true;
	    	} 
			else if (view instanceof TextView) {
	    		if (purchaseState) {
	    			((TextView)view).setPaintFlags(((TextView)view).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	    		} else {
	    			((TextView)view).setPaintFlags(((TextView)view).getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
	    		}
	    		
	    		((TextView)view).setText(cursor.getString(cursor.getColumnIndex(MasterItemsTable.COLUMN_NAME)));
	    		return true;
	    	}
	    	return false;
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.store_items_list_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		System.out.println("*******************Menu Item Id" + (item.getItemId() == R.id.action_add_item ));
//		return super.onOptionsItemSelected(item);
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_add_new:
	        	performAddItem();
	            return true;
	        case R.id.action_delete:
	        	performDeleteItem();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void performAddItem () {
		Intent itemGridIntent = new Intent(this, ItemDetailsActivity.class);
		Bundle bundle = new Bundle();
		long storeId = 0;
		if (storeUri != null) {
			storeId = Long.parseLong(storeUri.getLastPathSegment());
			
		}
		bundle.putLong("SelectStoreId", storeId);
		itemGridIntent.putExtras(bundle);
		startActivity(itemGridIntent);		
	}
	
	private void performDeleteItem () {
//		if (this.deleteButtonActive) {
//			this.deleteButtonActive = false;
//		} else {
//			this.deleteButtonActive = true;
//		}
		
		((CustomCursorAdapter)adapter).toggleHideDeleteButton();
		((CustomCursorAdapter)adapter).notifyDataSetChanged();
		

	}
	
	
	
	public class CustomCursorAdapter extends SimpleCursorAdapter {
		
		private boolean hideDeleteButton = true; //default, Hide button 

		public CustomCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);			
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void bindView(View view, Context context, final Cursor cursor) {
			// TODO Auto-generated method stub
			System.out.println("*** Cursor adapter bind view");
			super.bindView(view, context, cursor);
			
			ImageButton imgb = (ImageButton)view.findViewById(R.id.deletebutton);
			ImageView icon = (ImageView)view.findViewById(R.id.icon);
			if (hideDeleteButton) {
				//true.
				imgb.setVisibility(View.INVISIBLE);
				icon.setVisibility(View.VISIBLE);
			} else {
				//false.
				imgb.setVisibility(View.VISIBLE);
				icon.setVisibility(View.INVISIBLE);
			}
			imgb.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					deleteConfirm();
				}
				
			    public void deleteConfirm() {
			    	cursor.moveToFirst();
	            	String name = cursor.getString(cursor.getColumnIndex(MasterItemsTable.COLUMN_NAME));
			        DialogHandler appdialog = new DialogHandler();
			        boolean dlg = appdialog.Confirm(StoreItemListActivity.this, "Delete: " + name + " ?", "Do you want to delete : " + name + " from the list?",
			                "Cancel", "OK", okRun(), cancelRun());
			    }

			    public Runnable okRun(){
			    	return new Runnable() {
			            public void run() {
			            	int id = cursor.getInt(cursor.getColumnIndex(CartItemsTable.COLUMN_ID));
//							Uri itemUri = Uri.parse(SmartCartContentProvider.CART_ITEMS_CONTENT_URI + "/" + id);
							Uri itemUri = Uri.parse(SmartCartContentProvider.CART_ITEMS_CONTENT_URI + "/" + id);
							StoreItemListActivity.this.getContentResolver().delete(itemUri, null, null);
							CustomCursorAdapter.this.notifyDataSetChanged();
							String storeId = null;
							if (storeUri != null) {
								storeId = storeUri.getLastPathSegment();
							} 
							Bundle bundle = new Bundle();
							bundle.putSerializable("StoreId", storeId);
							StoreItemListActivity.this.getLoaderManager().restartLoader(0, bundle, StoreItemListActivity.this);
			            }
			          };
			    }

			    public Runnable cancelRun(){
			    	return new Runnable() {
			            public void run() {
			                System.out.println("Test This from cancel");
			            }
			          };
			    }
			});
		}
		
		public boolean isHideDeleteButton() {
			return hideDeleteButton;
		}

		public void toggleHideDeleteButton() {
			if (this.hideDeleteButton) {
				this.hideDeleteButton = false;
			} else {
				this.hideDeleteButton = true;
			}			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = super.getView(position, convertView, parent);
			return view;
		}
		
	}
	
	
}
