package com.info.st.data.aggregators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.info.st.models.Store;
import com.info.st.smartcart.R;

public class StoreAggregator implements Serializable{
	
	private List<Store> initStores;
	private String[] storeList = new String[] { "Walmart", "Costco", "Target",
	        "SamsClub", "ICC", "Publix", "Other" };
	
//	private JSONArray storeList = new JSONArray("[{ 'name': 'Walmart', 'name':'Costco', "Target",
//        "SamsClub", "ICC", "Publix" }]"
//			);
	
	
	public StoreAggregator() {
		this.initStores = new ArrayList<Store>();
		for (String store : storeList) {
			Store st = new Store(store, R.drawable.ic_launcher, "", "Active");
			st.setStoreItems(new ItemAggregator().getInitItems());
			initStores.add(st);
		}
		
	}

	public List<Store> getInitStores() {
		return initStores;
	}

	
	


}
