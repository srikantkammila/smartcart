package com.info.st.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.info.st.adapters.ItemGridAdapter;
import com.info.st.data.aggregators.ItemHistoryAggregator;
import com.info.st.smartcart.R;

public class SelectItemGridActivity extends Activity implements OnItemClickListener{
	
	GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_item_grid);
 
		gridView = (GridView) findViewById(R.id.itemsgridview);
 
		gridView.setAdapter(new ItemGridAdapter(this, new ItemHistoryAggregator().getItems()));
 
		gridView.setOnItemClickListener(this);

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (view.getTag().equals("${#New#}$")) {
			//new Item activity
			Intent itemGridIntent = new Intent(this, CreateEditItemActivity.class);
			startActivity(itemGridIntent);
			
		} else {
			String  displayMsg = "Item : " + ((TextView) view.findViewById(R.id.grid_item_label))
					   .getText() + " added to cart";
			Toast.makeText(
			   getApplicationContext(), displayMsg, Toast.LENGTH_LONG).show();
		}
		

	}
}
