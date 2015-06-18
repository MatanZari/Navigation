package com.zari.matan.navigationdrawerexample;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawer extends Fragment {

    private static final String KEY_DRAWER_OPENED = "user_opened";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private boolean mIsDrawerOpened;
    private boolean mFromSavedInstanceState;
    private View view;

    public NavigationDrawer() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsDrawerOpened = Boolean.valueOf(readFromPrefs(getActivity(),KEY_DRAWER_OPENED,null));
        if (savedInstanceState != null)
            mFromSavedInstanceState = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list_view);

    }

    public void setDrawer(int viewID, DrawerLayout drawerLayout, Toolbar toolbar){
        view = getActivity().findViewById(viewID);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_closed){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mIsDrawerOpened){
                    NavigationDrawer.SaveToPrefs(getActivity(),KEY_DRAWER_OPENED,mIsDrawerOpened+"");

                    getActivity().supportInvalidateOptionsMenu();

                }


            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().supportInvalidateOptionsMenu();
            }

        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);


        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
             mDrawerToggle.syncState();
            }
        });
    }

    public static void SaveToPrefs(Context context,String key, String value){
        SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.prefs_name),Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();

    }

    public static String readFromPrefs(Context context,String key, String defVal){
        SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.prefs_name),Context.MODE_PRIVATE);
        return sp.getString(key, defVal);

    }


}
