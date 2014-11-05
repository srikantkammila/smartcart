package com.info.st.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import com.info.st.activities.StoreDetailsActivity;
import com.info.st.activities.StoreItemListActivity;
import com.info.st.common.DialogHandler;
import com.info.st.persistence.SmartCartContentProvider;
import com.info.st.persistence.store.StoresTable;
import com.info.st.smartcart.R;


public class StoresFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemLongClickListener, OnItemClickListener {
	
	SimpleCursorAdapter adapter;
	
	public StoresFragment() {
		super();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    fillData();
	    getListView().setOnItemLongClickListener(this);
	    getListView().setOnItemClickListener(this);
	}
	
	private void fillData() {

	    String[] from = new String[] { StoresTable.COLUMN_NAME, StoresTable.COLUMN_ICON, StoresTable.STORE_ITEM_COUNT };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.label, R.id.icon, R.id.storeitemcount };

		getLoaderManager().initLoader(0, null, this);
		adapter = new CustomCursorAdapter(getActivity(), R.layout.store_fragment, null, from,
				to, 0);
//		SimpleCursorAdapter.ViewBinder viewBinder = new CustomCursorViewBinder();
//		adapter.setViewBinder(viewBinder);

		setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    return inflater.inflate(R.layout.store_list, container, false);	    
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> viewGroup, View view, int position, long id) {
		//Show item details view
		Intent i = new Intent(getActivity(), StoreDetailsActivity.class);
		Uri storeUri = Uri.parse(SmartCartContentProvider.STORES_CONTENT_URI + "/" + id);
		i.putExtra(SmartCartContentProvider.STORE_ITEM_TYPE, storeUri);
		startActivity(i);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> viewGroup, View view, int position, long id) {
		//Show store items list 
		Intent i = new Intent(getActivity(), StoreItemListActivity.class);
		Uri storeUri = Uri.parse(SmartCartContentProvider.STORES_CONTENT_URI + "/" + id);
		i.putExtra(SmartCartContentProvider.STORE_ITEM_TYPE, storeUri);
		startActivity(i);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = StoresTable.projection;
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				SmartCartContentProvider.STORES_CONTENT_URI, projection, null, null, StoresTable.STORE_ITEM_COUNT + " desc");
		
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
	            	String name = cursor.getString(cursor.getColumnIndex(StoresTable.COLUMN_NAME));
			        DialogHandler appdialog = new DialogHandler();
			        boolean dlg = appdialog.Confirm(getActivity(), "Delete: " + name + " ?", "Do you want to delete : " + name + " from the list?",
			                "Cancel", "OK", okRun(), cancelRun());
			        
			    }

			    public Runnable okRun(){
			    	return new Runnable() {
			            public void run() {
			            	int id = cursor.getInt(cursor.getColumnIndex(StoresTable.COLUMN_ID));
//							Uri itemUri = Uri.parse(SmartCartContentProvider.CART_ITEMS_CONTENT_URI + "/" + id);
							Uri storeUri = Uri.parse(SmartCartContentProvider.STORES_CONTENT_URI + "/" + id);
							getActivity().getContentResolver().delete(storeUri, null, null);
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
