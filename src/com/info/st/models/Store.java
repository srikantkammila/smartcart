package com.info.st.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Store implements Serializable{
	private String name;
	private int icon;
	private String address;
	private String status;
	private List<Item> storeItems;
	private int itemCount;
	
	public Store() {
		this.storeItems = new ArrayList<Item>();
	}
	public Store(String name, int icon, String address, String status) {
		this.name = name;
		this.icon = icon;
		this.address = address;
		this.status = status;
		this.storeItems = new ArrayList<Item>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Item> getStoreItems() {
		return storeItems;
	}
	
	public void setStoreItems(List<Item> storeItems) {
		this.storeItems = storeItems;
	}
	public void addStoreItem(Item itm) {
		for (Item item: this.storeItems) {
			if (item.getName() == itm.getName()) {
				//Duplicate item. Do nothing. Return from the function
				return;
			}
		}
		this.storeItems.add(itm);
		this.itemCount = this.storeItems.size();
	}
	public int getItemCount() {
		return this.itemCount;
	}
	public void removeStoreItem(Item itm) {
		for (Item item: this.storeItems) {
			if (item.getName() == itm.getName()) {
				this.storeItems.remove(item);
				this.itemCount = this.storeItems.size();
				break;
			}
		}
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName();
	}
	
}
