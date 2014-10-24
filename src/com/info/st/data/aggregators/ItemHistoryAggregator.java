package com.info.st.data.aggregators;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.info.st.models.Item;
import com.info.st.smartcart.R;

public class ItemHistoryAggregator {
	
	private List<Item> items;
	private String[] itemList = new String[] { "Bread", "Rice", "Banana", "Juice", "Milk", "${#New#}$" };
	
	public ItemHistoryAggregator() {
		this.items = new ArrayList<Item>();
		for (String itm : itemList) {
			Item item = new Item(itm, R.drawable.ic_launcher, "0", null, 11.25, "Sample Note", false, new Date(), Item.QuantityMeasure.GALON );
			items.add(item);
		}
	}

	public List<Item> getItems() {
		return items;
	}
}
