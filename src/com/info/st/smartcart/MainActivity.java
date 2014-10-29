package com.info.st.smartcart;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.info.st.activities.SelectItemGridActivity;
import com.info.st.activities.StoreItemListActivity;
import com.info.st.adapters.TablistAdapter;
import com.info.st.data.aggregators.ItemAggregator;
import com.info.st.data.aggregators.StoreAggregator;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
    private TablistAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Stores", "Items"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        
        
        StoreAggregator storeAg = new StoreAggregator();
//        getIntent().putExtra("StoreAggregator", storeAg);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("DisplayStores", (ArrayList<Store>)storeAg.getInitStores());
        
        ItemAggregator itemAg = ItemAggregator.getInstance();
//        bundle.putSerializable("DisplayItems", (ArrayList<Item>)itemAg.getInitItems());
//        getIntent().putExtras(bundle);
        
        mAdapter = new TablistAdapter(getSupportFragmentManager(), itemAg.getInitItems(), storeAg.getInitStores());
        viewPager.setAdapter(mAdapter);
//        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
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
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void startAddItemActivity () {
		Intent itemGridIntent = new Intent(this, SelectItemGridActivity.class);
		startActivity(itemGridIntent);
		
	}
	
	private void startAddStoreActivity () {
		
	}
	
	
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		System.out.println("##############################Back Pressed");
//		if(viewPager.getCurrentItem() == 0) {
//            if (mAdapter.getItem(0) instanceof StoreItemsFragment) {
//                ((StoreItemsFragment) mAdapter.getItem(0)).backPressed();
//            }
//            else if (mAdapter.getItem(0) instanceof MainTabFragment) {
//                finish();
//            }
//        }
//	}
	

	
}
