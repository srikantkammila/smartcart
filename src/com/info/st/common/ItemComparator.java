package com.info.st.common;

import java.util.Comparator;

import com.info.st.models.Item;

public class ItemComparator implements Comparator<Item>{
	
	Item selectedItem;
	public ItemComparator() {
		// TODO Auto-generated constructor stub
	}
	
	public ItemComparator(Item selectedItem) {
		this.selectedItem = selectedItem;
	}

	@Override
	public int compare(Item lhs, Item rhs) {
		// TODO Auto-generated method stub
		System.out.println("*************Comparing items");
		boolean selectedItemState = this.selectedItem.isPurchaseState();
		if (selectedItemState) { // true -- item purchased. Sort based on true state of the purchase flag
			if (lhs != null && lhs.isPurchaseState() == true && !lhs.getItemId().toString().equals(this.selectedItem.getItemId().toString())) { //true -- item purchased. 
				if (rhs.isPurchaseState() == true && !rhs.getItemId().toString().equals(this.selectedItem.getItemId().toString())) { //true -- item purchased. 
//					return lhs.getName().compareTo(rhs.getName()); //natural ordering
					return 0;
	 			} else {
					return 1;
				}
			} else { // false -- item need to purchase.
				if (rhs != null && rhs.isPurchaseState() == true && !rhs.getItemId().toString().equals(this.selectedItem.getItemId().toString())) { //true -- item purchased. 
					return -1;
				} else {
//					return lhs.getName().compareTo(rhs.getName()); //natural ordering
					return 0;
				}
			}
		} else { // false -- item need to purchase. Sort based on false state of the purchase flag
			if (lhs != null && lhs.isPurchaseState() == false && !lhs.getItemId().toString().equals(this.selectedItem.getItemId().toString())) { //true -- item purchased. 
				if (rhs.isPurchaseState() == false && !rhs.getItemId().toString().equals(this.selectedItem.getItemId().toString())) { //true -- item purchased. 
//					return lhs.getName().compareTo(rhs.getName()); //natural ordering
					return 0;
	 			} else {
					return -1;
				}
			} else { // false -- item need to purchase.
				if (rhs != null && rhs.isPurchaseState() == false && !rhs.getItemId().toString().equals(this.selectedItem.getItemId().toString())) { //true -- item purchased. 
					return 1;
				} else {
//					return lhs.getName().compareTo(rhs.getName()); //natural ordering
					return 0;
				}
			}
		}
		
	} 
	

}
