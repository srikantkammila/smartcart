package com.info.st.smartcart;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import com.info.st.activities.SelectItemGridActivity;
import com.info.st.activities.StoreDetailsActivity;
import com.info.st.adapters.TablistAdapter;
import com.info.st.fragments.ItemsFragment;
import com.info.st.fragments.StoresFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
    private TablistAdapter mAdapter;
    private ActionBar actionBar;
//    private boolean deleteButtonActive = false;
    private List<Tab> mainTabs = new ArrayList<Tab>();
    // Tab titles
    private String[] tabs = { "Items", "Stores" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        actionBar.setDisplayShowTitleEnabled(false);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        

        
        mAdapter = new TablistAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
//        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
        	Tab t = actionBar.newTab();
        	t.setText(tab_name).setTabListener(this);
            actionBar.addTab(t);
            this.mainTabs.add(t);
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar = getActionBar();
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		System.out.println("*******************Menu Item Id" + (item.getItemId() == R.id.action_add_item ));
//		return super.onOptionsItemSelected(item);
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_add_item:
	        	startAddItemActivity();
	            return true;
	        case R.id.action_add_store:
	        	startAddStoreActivity();
	            return true;
	        case R.id.action_delete:
	        	startDeleteActivity();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void startAddItemActivity () {
		Intent itemGridIntent = new Intent(this, SelectItemGridActivity.class);
		startActivity(itemGridIntent);		
	}
	
	private void startAddStoreActivity () {
		Intent itemGridIntent = new Intent(this, StoreDetailsActivity.class);
		startActivity(itemGridIntent);
	}
	
	private void startDeleteActivity () {
//		if (this.deleteButtonActive) {
//			this.deleteButtonActive = false;
//		} else {
//			this.deleteButtonActive = true;
//		}
		
		for (int i=0; i<mAdapter.getCount(); i++) {
			ListFragment ims = (ListFragment)mAdapter.getItem(i);
			if (ims instanceof ItemsFragment) {
				((ItemsFragment.CustomCursorAdapter)ims.getListAdapter()).toggleHideDeleteButton();
				((SimpleCursorAdapter)ims.getListAdapter()).notifyDataSetChanged();
			} else if (ims instanceof StoresFragment) {
				((StoresFragment.CustomCursorAdapter)ims.getListAdapter()).toggleHideDeleteButton();
				((SimpleCursorAdapter)ims.getListAdapter()).notifyDataSetChanged();
			}
			
		}

	}
	

	
}
