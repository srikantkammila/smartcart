package com.info.st.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.info.st.adapters.ItemListAdapter;
import com.info.st.models.Item;
import com.info.st.smartcart.R;

public class StoreItemListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_item_list);
		HashMap<String, ArrayList<Item> > storeItemsBundle = (HashMap<String, ArrayList<Item> >)getIntent().getExtras().getSerializable("StoreItemsBundle");
		ArrayList<Item> storeItems = storeItemsBundle.get("StoreItems");
		String storeName = getIntent().getStringExtra("StoreName");
		ListAdapter adapter = new ItemListAdapter(this, storeItems);
		setListAdapter(adapter);
		setTitle(storeName);
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		System.out.println("Store Item Click");
		super.onListItemClick(l, v, position, id);
	}
	
	
}
