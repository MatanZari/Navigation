package com.zari.matan.navigationdrawerexample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zari.matan.navigationdrawerexample.fragments.HomeFragment;


/**
 * Created by Matan on 5/21/2015
 */
public class FilterPagerAdapter extends FragmentStatePagerAdapter {

    public Context context;
    private int numOfTabs;
    protected String[] titles;

    public FilterPagerAdapter(FragmentManager fm, Context context, int numbOfTabs, String[] titles) {
        super(fm);
        this.context = context;
        numOfTabs = numbOfTabs;
        this.titles = titles;
    }




    @Override
    public Fragment getItem(int position) {
        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
