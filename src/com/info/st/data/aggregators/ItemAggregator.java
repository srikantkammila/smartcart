package com.info.st.data.aggregators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.info.st.models.Item;
import com.info.st.smartcart.R;


public class ItemAggregator implements Serializable{
	
	private List<Item> initItems;
	private String[] itemList = new String[] { "Bread", "Rice", "Banana", "Juice", "Milk" };
	
	public ItemAggregator() {
		this.initItems = new ArrayList<Item>();
		for (String itm : itemList) {
			Item item = new Item(itm, R.drawable.ic_launcher, 0, null, 11.25, "Sample Note" );
			initItems.add(item);
		}
	}

	public List<Item> getInitItems() {
		return initItems;
	}	
	
}
