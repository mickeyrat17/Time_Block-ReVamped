package com.daseyffert.timeblock;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.daseyffert.timeblock.ApplicationTabs.TabFragment1;
import com.daseyffert.timeblock.ApplicationTabs.TabFragment2;
import com.daseyffert.timeblock.ApplicationTabs.Tab_List.ToDoList;

/**
 * Created by Daniel on 12/21/2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private TabFragment1 mTab1;
    private TabFragment2 mTab2;
    private ToDoList mTab3;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    /**
     * Create proper Fragment in proper position
     * @param position of what tab application is viewing
     * @return new proper Fragment instance of corresponding position
     */
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return mTab1 = new TabFragment1();
            case 1:
                return mTab2 = new TabFragment2();
            case 2:
                return mTab3 = new ToDoList();
            default:
                return null;
        }
    }

    /**
     * Get count of total tabs
     * @return total number of tabs
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}