package com.info.st.models;

import java.io.Serializable;

public class Item implements Serializable{
	private String name;
	private int icon;
	private float quantity;
	private Store store;
	private double price;
	private String note;
	
	
	
	public Item(String name, int icon, float quantity, Store store, double price, String note) {
		super();
		this.name = name;
		this.icon = icon;
		this.quantity = quantity;
		this.store = store;
		this.price = price;
		this.note = note;
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
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}	
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName();
	}

}
