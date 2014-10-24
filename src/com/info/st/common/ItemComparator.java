package com.info.st.common;

import java.util.Comparator;

import com.info.st.models.Item;

public class ItemComparator implements Comparator<Item>{

	@Override
	public int compare(Item lhs, Item rhs) {
		// TODO Auto-generated method stub
		System.out.println("*************Comparing items");
		if (lhs != null && lhs.isPurchaseState() == true) { //true -- item purchased. 
			if (rhs.isPurchaseState() == true) { //true -- item purchased. 
//				return lhs.getName().compareTo(rhs.getName()); //natural ordering
				return 0;
 			} else {
				return 1;
			}
		} else { // false -- item need to purchase.
			if (rhs != null && rhs.isPurchaseState() == true) { //true -- item purchased. 
				return -1;
			} else {
//				return lhs.getName().compareTo(rhs.getName()); //natural ordering
				return 0;
			}
		}
	} 
	

}
