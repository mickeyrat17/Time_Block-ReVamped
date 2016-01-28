package com.daseyffert.timeblock;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.daseyffert.timeblock.ApplicationTabs.TabFragment1;
import com.daseyffert.timeblock.ApplicationTabs.TabFragment2;
import com.daseyffert.timeblock.ApplicationTabs.Tab_List.ToDoList;

import java.util.ArrayList;
import java.util.List;

public class HostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Show Application Icon next to title
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.activity_host_viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.activity_host_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Add the Fragments into the adapter to setup data in
     * the adapter into the ViewPager
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Aggregate the Fragments and title to List
        adapter.addFragment(new ToDoList(), "LIST");
        adapter.addFragment(new TabFragment1(), "APPLICATIONS");
        adapter.addFragment(new TabFragment2(), "TIMEBLOCK");
        viewPager.setAdapter(adapter);
    }

    /**
     * Adapter of the ViewPager, used to keep track of tabs
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}