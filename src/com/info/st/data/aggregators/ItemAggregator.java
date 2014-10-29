package com.info.st.data.aggregators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.info.st.models.Item;
import com.info.st.smartcart.R;


public class ItemAggregator implements Serializable{
	
	private List<Item> initItems;
	private String[] itemList = new String[] { "Bread", "Rice", "Banana", "Juice", "Milk" };
	private static ItemAggregator itemAg;
	
	public static ItemAggregator getInstance() {
		if (itemAg == null) {
			itemAg = new ItemAggregator();
		}
		return itemAg;
	}
	
	private ItemAggregator() {
		this.initItems = new ArrayList<Item>();
		for (String itm : itemList) {
			Item item = new Item(itm, R.drawable.ic_launcher, "0", null, 11.25, "Sample Note", false, new Date(), Item.QuantityMeasure.GALON );
			initItems.add(item);
		}
	}

	public List<Item> getInitItems() {
		return initItems;
	}
	
	public void addItem(Item item) {
		if (initItems != null) {
			initItems.add(item);
		}
	}
}
