package com.info.st.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.info.st.smartcart.R;

public class CreateEditItemActivity extends Activity{
	
	View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_item_details);
		 
//		view = (view) findViewById(R.id.itemsgridview);
// 
//		view.setAdapter(new ItemGridAdapter(this, new ItemHistoryAggregator().getItems()));
// 
//		view.setOnItemClickListener(this);
	}

}
