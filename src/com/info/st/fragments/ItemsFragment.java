package com.info.st.fragments;

import java.util.List;
import java.util.Random;

import android.content.ContentValues;
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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.info.st.activities.ItemDetailsActivity;
import com.info.st.models.Item;
import com.info.st.persistence.SmartCartContentProvider;
import com.info.st.persistence.cartitems.CartItemsTable;
import com.info.st.persistence.masteritems.MasterItemsTable;
import com.info.st.smartcart.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 *
 */
public class ItemsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, MainTabFragment, OnItemLongClickListener, OnItemClickListener{
	List<Item> items;
	
	SimpleCursorAdapter adapter;

	
	public ItemsFragment(List<Item> items) {
		super();
		this.items = items;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
//	    List<Item> displayItems = (List<Item>)getActivity().getIntent().getExtras().get("DisplayItems");
//		ItemListAdapter adapter = new ItemListAdapter(getActivity(), items);
//	    setListAdapter(adapter);
	    fillData();
	    getListView().setOnItemLongClickListener(this);
	    getListView().setOnItemClickListener(this);
	}
	
	private void fillData() {

	    String[] from = new String[] { MasterItemsTable.COLUMN_NAME, MasterItemsTable.COLUMN_ICON, CartItemsTable.COLUMN_PURCHASE_STATE };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.label, R.id.icon, R.id.itempurchasesate };

		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_fragment, null, from,
				to, 0);
		SimpleCursorAdapter.ViewBinder viewBinder = new CustomCursorViewBinder();
		adapter.setViewBinder(viewBinder);

		setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
	    return inflater.inflate(R.layout.item_list, null, false);	    
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
		System.out.println("***************view: " + view);
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


}
