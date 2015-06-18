package com.zari.matan.navigationdrawerexample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lyuzik.remoteimageview.RImageView;
import com.zari.matan.navigationdrawerexample.FilterPagerAdapter;
import com.zari.matan.navigationdrawerexample.MainActivity;
import com.zari.matan.navigationdrawerexample.R;
import com.zari.matan.navigationdrawerexample.helper.FragmentUiLifeCycleHelper;
import com.zari.matan.navigationdrawerexample.tabs.SlidingTabLayout;


/**
 * Created by Matan on 4/19/2015.
 */
public class ProfileFragment extends Fragment implements FragmentUiLifeCycleHelper {

    public static final String TAG = ProfileFragment.class.getName();
    RImageView profilePicture;
    TextView userName;
    ViewPager viewPager;
    SlidingTabLayout filterTabs;
    MainActivity activity;
    FilterPagerAdapter pagerAdapter;
    String[] titles = {"All", "Facebook", "Twitter", "Instagram","Tumblr","Youtube"};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.profile_fragment_layout,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profilePicture = (RImageView) view.findViewById(R.id.user_profile_picture);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        filterTabs = (SlidingTabLayout) view.findViewById(R.id.filter_tabs);
        userName = (TextView) view.findViewById(R.id.user_name);
        pagerAdapter = new FilterPagerAdapter(getChildFragmentManager(),getActivity(),titles.length,titles);
        viewPager.setAdapter(pagerAdapter);
        filterTabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        filterTabs.setCustomTabView(R.layout.custom_tab, R.id.textPageTitle);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        filterTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimaryDark);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        filterTabs.setViewPager(viewPager);

        viewPager.setOffscreenPageLimit(5);




    }





    @Override
    public void onPauseFragment() {
        if (activity == null)
            activity = (MainActivity) getActivity();
        else
        Toast.makeText(activity, TAG + " onPauseFragment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentResumed() {
        if (activity == null)
            activity = (MainActivity) getActivity();
        else
        Toast.makeText(activity,TAG+" onFragmentResumed",Toast.LENGTH_SHORT).show();
    }
}
