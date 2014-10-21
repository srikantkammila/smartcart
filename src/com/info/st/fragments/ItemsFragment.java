package com.info.st.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.info.st.activities.ItemDetailsActivity;
import com.info.st.activities.StoreItemListActivity;
import com.info.st.adapters.ItemListAdapter;
import com.info.st.adapters.StoreListAdapter;
import com.info.st.data.aggregators.StoreAggregator;
import com.info.st.models.Item;
import com.info.st.models.Store;
import com.info.st.smartcart.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 *
 */
public class ItemsFragment extends ListFragment implements MainTabFragment {
	List<Item> items;
	
	public ItemsFragment(List<Item> items) {
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
		Item selectedIem = this.items.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("SelectedItem", selectedIem);
		
		Intent itemDetailsIntent = new Intent(getActivity(), ItemDetailsActivity.class);	
		itemDetailsIntent.putExtras(bundle);
		startActivity(itemDetailsIntent);
	}


}
