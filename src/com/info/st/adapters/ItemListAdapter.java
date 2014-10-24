package com.info.st.adapters;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.info.st.common.ItemComparator;
import com.info.st.models.Item;
import com.info.st.smartcart.R;

public class ItemListAdapter extends BaseAdapter {
	
	private final Context context;
	private final List<Item> values;
	
	public ItemListAdapter(Context context, List<Item> values) {
		this.context = context;
	    this.values = values;
	}
	
	static class ViewHolder {
	    protected TextView text;
	    protected ImageView image;
	    protected CheckBox checkbox;
	    protected ItemListAdapter adapter;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_fragment, null);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) convertView.findViewById(R.id.label);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.icon);
		    viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.itempurchasesate);
		    viewHolder.adapter = this;
		    viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					Item item = (Item) viewHolder.checkbox
			                  .getTag();
			        item.setPurchaseState(buttonView.isChecked());
//			        if (buttonView.isChecked()) {
//			        	viewHolder.text.setPaintFlags(viewHolder.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//			        } else {
//			        	
//			        }
			        viewHolder.adapter.notifyDataSetChanged();
					
				}
			});
		    convertView.setTag(viewHolder);
		    viewHolder.checkbox.setTag(values.get(position));
		} else {
			((ViewHolder) convertView.getTag()).checkbox.setTag(values.get(position));
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
	    holder.text.setText(values.get(position).getName());
	    holder.checkbox.setChecked(values.get(position).isPurchaseState());
		
//	    Collections.sort(values, new ItemComparator());
	    TextView textView = (TextView) convertView.findViewById(R.id.label);
	    ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
	    CheckBox check = (CheckBox)convertView.findViewById(R.id.itempurchasesate);
	    
	    textView.setText(values.get(position).getName());
	    if (values.get(position).isPurchaseState() == true) {
	    	textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	    } else {
	    	textView.setPaintFlags(textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
	    }
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
	public Item getItem(int position) {
		// TODO Auto-generated method stub
		return values.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return values.indexOf(getItem(position));
	}
	
	@Override
	public void notifyDataSetChanged() {
		Collections.sort(values, new ItemComparator());
		super.notifyDataSetChanged();
	}
	
	

}
