package com.info.st.models;

import java.io.Serializable;
import java.util.Date;

public class Item implements Serializable{
	private String name;
	private int icon;
	private String quantity;
	private Store store;
	private double price;
	private String note;
	private boolean purchaseState; //true -- item purchased
								   //false -- Need to purchase
	private Date dueDateTime;
	private QuantityMeasure measure;
	
	public enum QuantityMeasure{
		LB, OUNCE, KG, GRAM, GALON, LITER, MILLI_LITER, ITEMS, KILO_METER, METER, MILLI_METER, MILE, YARD, INCH
	}
	
	
	public Item(String name, int icon, String quantity, Store store, double price, String note, boolean purchaseState, Date dueDateTime, QuantityMeasure measure ) {
		super();
		this.name = name;
		this.icon = icon;
		this.quantity = quantity;
		this.store = store;
		this.price = price;
		this.note = note;
		this.purchaseState = purchaseState;
		this.dueDateTime = dueDateTime;
		this.measure = measure;
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
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
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
	

	public boolean isPurchaseState() {
		return purchaseState;
	}


	public void setPurchaseState(boolean purchaseState) {
		this.purchaseState = purchaseState;
	}	
	
	
	public Date getDueDateTime() {
		return dueDateTime;
	}


	public void setDueDateTime(Date dueDateTime) {
		this.dueDateTime = dueDateTime;
	}


	public QuantityMeasure getMeasure() {
		return measure;
	}


	public void setMeasure(QuantityMeasure measure) {
		this.measure = measure;
	}


	public void togglePurchaseState() {
		if (this.purchaseState == true) {
			this.purchaseState = false;
		} else {
			this.purchaseState = true;
		}
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName();
	}

}
