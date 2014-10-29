package com.info.st.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.info.st.models.Store;
import com.info.st.smartcart.R;


public class StoreSpinnerListAdapter extends ArrayAdapter {
	
	private final Context context;
	private final List<Store> values;
	

	public StoreSpinnerListAdapter(Context context, int resourceId, List<Store> values) {		
		super(context, resourceId, values);
		this.context = context;
	    this.values = values;
	    
	    Store spinnerFirstElement = new Store("Select Store", R.drawable.ic_launcher, "", "active");
	    this.values.add(spinnerFirstElement);
	    
	}
	
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(android.R.layout.simple_spinner_item, null);
		}
	    TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
//	    ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
	    textView.setText(values.get(position).getName());	    
//	    int icon = values.get(position).getIcon();
//	    imageView.setImageResource(icon);
	    
	    return convertView;
	}

	
	@Override
	public int getCount() {
		
		int count =  values.size();
		return count > 0 ? (count - 1) : count;
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
