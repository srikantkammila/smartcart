package com.info.st.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.info.st.adapters.ItemListAdapter;
import com.info.st.models.Item;
import com.info.st.smartcart.R;

public class StoreItemsFragment extends ListFragment {
	
	List<Item> items;
	
	public StoreItemsFragment(List<Item> items) {
		super();
		this.items = items;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
//	    List<Item> displayItems = (List<Item>)getActivity().getIntent().getExtras().get("DisplayItems");
		ItemListAdapter adapter = new ItemListAdapter(getActivity(), items);
	    setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	    return inflater.inflate(R.layout.item_list, null, false);
	    
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		System.out.println("Click on list item fragment");
		
	}
	
}
