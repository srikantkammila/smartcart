package com.info.st.fragments;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.info.st.activities.ItemDetailsActivity;
import com.info.st.adapters.ItemListAdapter;
import com.info.st.models.Item;
import com.info.st.smartcart.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 *
 */
public class ItemsFragment extends ListFragment implements MainTabFragment, OnItemLongClickListener, OnItemClickListener {
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
	    getListView().setOnItemLongClickListener(this);
	    getListView().setOnItemClickListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	    return inflater.inflate(R.layout.item_list, null, false);
	    
	}

	
//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//		Item selectedIem = this.items.get(position);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("SelectedItem", selectedIem);
//		
//		Intent itemDetailsIntent = new Intent(getActivity(), ItemDetailsActivity.class);	
//		itemDetailsIntent.putExtras(bundle);
//		startActivity(itemDetailsIntent);
//	}

	@Override
	public boolean onItemLongClick(AdapterView<?> viewGroup, View view, int position, long id) {
		System.out.println("####################long click");
		Item selectedIem = this.items.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("SelectedItem", selectedIem);
		
		Intent itemDetailsIntent = new Intent(getActivity(), ItemDetailsActivity.class);	
		itemDetailsIntent.putExtras(bundle);
		startActivity(itemDetailsIntent);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> viewGroup, View view, int position, long id) {
		// TODO Auto-generated method stub
		System.out.println("*************************click");
		((ItemListAdapter)getListAdapter()).getItem(position).togglePurchaseState();
		((ItemListAdapter)getListAdapter()).notifyDataSetChanged();
//		getListView().removeViewAt(position);
//		getListView().addView(view);
//		Item selectedIem = this.items.get(position);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("SelectedItem", selectedIem);
//		
//		Intent itemDetailsIntent = new Intent(getActivity(), ItemDetailsActivity.class);	
//		itemDetailsIntent.putExtras(bundle);
//		startActivity(itemDetailsIntent);
	}
	


}
