package com.info.st.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.info.st.activities.ItemDetailsActivity;
import com.info.st.activities.StoreItemListActivity;
import com.info.st.common.DialogHandler;
import com.info.st.persistence.SmartCartContentProvider;
import com.info.st.persistence.cartitems.CartItemsTable;
import com.info.st.persistence.masteritems.MasterItemsTable;
import com.info.st.persistence.store.StoresTable;
import com.info.st.smartcart.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 *
 */
public class ItemsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemLongClickListener, OnItemClickListener{

	
	SimpleCursorAdapter adapter;
	
	boolean showDeleteButton = false;

	
	public ItemsFragment() {
		super();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    System.out.println("****************activity created"); 
	    fillData();
	    getListView().setOnItemLongClickListener(this);
	    getListView().setOnItemClickListener(this);
	}
	
	private void fillData() {

	    String[] from = new String[] { MasterItemsTable.COLUMN_NAME, MasterItemsTable.COLUMN_ICON, CartItemsTable.COLUMN_PURCHASE_STATE };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.label, R.id.icon, R.id.itempurchasesate };

		getLoaderManager().initLoader(0, null, this);
		adapter = new CustomCursorAdapter(getActivity(), R.layout.item_fragment, null, from,
				to, 0);
		SimpleCursorAdapter.ViewBinder viewBinder = new CustomCursorViewBinder();
		adapter.setViewBinder(viewBinder);

		setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("****************create view"); 
	    View view =inflater.inflate(R.layout.item_list, container, false);	    
	    
	    return view;
	}
	
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		System.out.println("****************get view"); 
		return super.getView();		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> viewGroup, View view, int position, long id) {
		//Show item details view
		Intent i = new Intent(getActivity(), ItemDetailsActivity.class);
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
		
		getActivity().getContentResolver().update(itemUri, values, null, null);
		
		getLoaderManager().restartLoader(0, null, this);
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { "cartitems."+CartItemsTable.COLUMN_ID + " as _id", CartItemsTable.COLUMN_DUE_DATE_TIME,
				CartItemsTable.COLUMN_PURCHASE_STATE,
				MasterItemsTable.COLUMN_ICON, MasterItemsTable.COLUMN_NAME,
				MasterItemsTable.COLUMN_NOTE,  MasterItemsTable.COLUMN_QUANTITY,
				MasterItemsTable.COLUMN_QUANTITY_MEASURE , "masteritems."+MasterItemsTable.COLUMN_ID + " as item_id"};
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				SmartCartContentProvider.CART_ITEMS_CONTENT_URI, projection, null, null, CartItemsTable.COLUMN_PURCHASE_STATE + " ASC, " + CartItemsTable.COLUMN_CREATION_DATE + " DESC");
		
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
	
	
	private class CustomCursorViewBinder implements SimpleCursorAdapter.ViewBinder {
		
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
	
	public void toggleBeleteButton (boolean hide) {
		showDeleteButton = hide;		
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
			        boolean dlg = appdialog.Confirm(getActivity(), "Delete: " + name + " ?", "Do you want to delete : " + name + " from the list?",
			                "Cancel", "OK", okRun(), cancelRun());
			       
			    }

			    public Runnable okRun(){
			    	return new Runnable() {
			            public void run() {
			            	int id = cursor.getInt(cursor.getColumnIndex(CartItemsTable.COLUMN_ID));
							Uri itemUri = Uri.parse(SmartCartContentProvider.CART_ITEMS_CONTENT_URI + "/" + id);
							getActivity().getContentResolver().delete(itemUri, null, null);
							CustomCursorAdapter.this.notifyDataSetChanged();
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
