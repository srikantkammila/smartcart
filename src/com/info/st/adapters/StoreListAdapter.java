package com.info.st.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.info.st.models.Store;
import com.info.st.smartcart.R;

public class StoreListAdapter extends BaseAdapter {
	
	private final Context context;
	private final List<Store> values;

	public StoreListAdapter(Context context, List<Store> values) {		
		this.context = context;
	    this.values = values;
	    
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.store_fragment, null);
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
		
		return values.size();
	}



	@Override
	public Object getItem(int position) {
		return values.get(position);
	}



	@Override
	public long getItemId(int position) {
		return values.indexOf(getItem(position));
	}

	
	

}
