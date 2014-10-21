package com.info.st.adapters;

import java.util.List;

import com.info.st.models.Item;
import com.info.st.models.Store;
import com.info.st.smartcart.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter {
	
	private final Context context;
	  private final List<Item> values;

	public ItemListAdapter(Context context, List<Item> values) {
		this.context = context;
	    this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_fragment, null);
		}
		
	    
	    TextView textView = (TextView) convertView.findViewById(R.id.label);
	    ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
	    textView.setText(values.get(position).getName());
	    int icon = values.get(position).getIcon();
	    imageView.setImageResource(icon);

	    return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return values.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return values.indexOf(getItem(position));
	}
	
	

}
