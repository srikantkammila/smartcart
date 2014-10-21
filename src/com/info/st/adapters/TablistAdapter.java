package com.info.st.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.info.st.fragments.ItemsFragment;
import com.info.st.fragments.MainTabFragment;
import com.info.st.fragments.StoreItemsFragment;
import com.info.st.fragments.StoresFragment;
import com.info.st.models.Item;
import com.info.st.models.Store;

/**
 * Created by Srikanth Kammila on 10/11/2014.
 */
public class TablistAdapter extends FragmentPagerAdapter {
	List<Item> items;
	List<Store> stores;
	FragmentManager fm;
	Fragment fragPos1;
	Fragment fragPos0;

    public TablistAdapter(FragmentManager fm, List<Item> items, List<Store> stores) {
        super(fm);
        this.items = items;
        this.stores = stores;
        this.fm = fm;
    }
    

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
            	if (fragPos0 == null) {
            		fragPos0 = new StoresFragment(this.stores);
            	}
                return fragPos0;
                
            case 1:
                if (fragPos1 == null) {
                	fragPos1 = new ItemsFragment(this.items);
                }
                return fragPos1;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}