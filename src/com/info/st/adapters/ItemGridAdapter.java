package com.info.st.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.info.st.models.Item;
import com.info.st.smartcart.R;

public class ItemGridAdapter extends BaseAdapter {
	List<Item> items;
	Context context;

	public ItemGridAdapter(Context context, List<Item> items) {
		this.items = items;
		this.context = context;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Item getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.select_item_grid_fragment, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			String itemName = items.get(position).getName();
			// set image based on selected text
			ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
			if ("${#New#}$".equals(itemName)) {
				textView.setText("New");
				gridView.setTag("${#New#}$");
				imageView.setImageResource(R.drawable.add);
			} else {
				textView.setText(itemName);
				imageView.setImageResource(items.get(position).getIcon());
			}
			

			
			
			


		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

}
