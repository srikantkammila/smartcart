package com.info.st.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.info.st.activities.StoreItemListActivity;
import com.info.st.adapters.StoreListAdapter;
import com.info.st.models.Item;
import com.info.st.models.Store;
import com.info.st.smartcart.R;


public class StoresFragment extends ListFragment implements MainTabFragment {
	
	List<Store> stores;
	
	public StoresFragment(List<Store> stores) {
		super();
		this.stores = stores;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
//	    List<Store> displayStores = (List<Store>)getActivity().getIntent().getExtras().get("DisplayStores");		
	    StoreListAdapter adapter = new StoreListAdapter(getActivity(), this.stores);
	    setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	    return inflater.inflate(R.layout.store_list, null, false);
	    
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
//		getActivity().getActionBar().removeAllTabs();
//		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
//		Store selectedStore = this.stores.get(position);
//		List<Item> storeItems = selectedStore.getStoreItems();
//		StoreItemsFragment newFragment = new StoreItemsFragment(storeItems);
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//		transaction.replace(getId(), newFragment);
//		transaction.addToBackStack(null);	
//
//		// Commit the transaction
//		transaction.commit();
//		getFragmentManager().executePendingTransactions();
		Bundle bundle = new Bundle();
		HashMap<String, ArrayList<Item> > storeItems = new HashMap<String, ArrayList<Item>>();
		storeItems.put("StoreItems", (ArrayList<Item>)this.stores.get(position).getStoreItems());
		bundle.putSerializable("StoreItemsBundle", storeItems);
		
		Intent storeItemsIntent = new Intent(getActivity(), StoreItemListActivity.class);	
		storeItemsIntent.putExtras(bundle);
		storeItemsIntent.putExtra("StoreName", this.stores.get(position).getName());
		startActivity(storeItemsIntent);
		
		
//		listener.onSwitchToNextFragment();
	}
	
		
	
	
}
