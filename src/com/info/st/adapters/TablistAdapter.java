package com.info.st.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.info.st.fragments.ItemsFragment;
import com.info.st.fragments.StoresFragment;
import com.info.st.models.Item;
import com.info.st.models.Store;

/**
 * Created by Srikanth Kammila on 10/11/2014.
 */
public class TablistAdapter extends FragmentPagerAdapter {
	FragmentManager fm;
	Fragment fragPos1;
	Fragment fragPos0;

    public TablistAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }
    

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
            	if (fragPos0 == null) {
            		fragPos0 = new ItemsFragment();
            	}
                return fragPos0;
                
            case 1:
                if (fragPos1 == null) {
                	fragPos1 = new StoresFragment();
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
