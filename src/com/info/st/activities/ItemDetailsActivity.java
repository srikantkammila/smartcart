package com.info.st.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.info.st.models.Item;
import com.info.st.smartcart.R;

public class ItemDetailsActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_details);
		Item selectedItem = (Item)getIntent().getExtras().get("SelectedItem");
		TextView name = (TextView)findViewById(R.id.itemname);
		TextView note = (TextView)findViewById(R.id.itemnote);
		TextView price = (TextView)findViewById(R.id.itemprice);
		TextView quantity = (TextView)findViewById(R.id.itemquantity);
		
		name.setText(selectedItem.getName());
		note.setText(selectedItem.getNote());
		price.setText(Double.toString(selectedItem.getPrice()));
		quantity.setText(Float.toString(selectedItem.getQuantity()));
		setTitle(selectedItem.getName());
	}

}
