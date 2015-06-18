package com.zari.matan.navigationdrawerexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.youtube.player.YouTubePlayer;
import com.zari.matan.navigationdrawerexample.com.zari.matan.fragment.BlueFragment;
import com.zari.matan.navigationdrawerexample.com.zari.matan.fragment.GreenFragment;
import com.zari.matan.navigationdrawerexample.com.zari.matan.fragment.RedFragment;
import com.zari.matan.navigationdrawerexample.fragments.DiscoverFragment;
import com.zari.matan.navigationdrawerexample.fragments.HomeFragment;
import com.zari.matan.navigationdrawerexample.fragments.ProfileFragment;
import com.zari.matan.navigationdrawerexample.helper.Utils;

import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends AppCompatActivity {

    public DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserLearnedDrawer;
    private static final String KEY_DRAWER_OPENED = "user_opened";
    public  NavigationView navDrawer;
    static AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    Fragment[] fragments = new Fragment[]{
            new HomeFragment(),
            new DiscoverFragment(),
            new ProfileFragment()


    };
    public static boolean isInternetAvailable;

    public final String SP_NAME = "MyPrefs";

    public static MediaPlayer mMediaPlayer;

    public SharedPreferences sp;
    public static YouTubePlayer mYoutubePlayer;
    private boolean drawerOpen;
    public FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.init(this);
        mUserLearnedDrawer = Boolean.valueOf(readFromPrefs(this, KEY_DRAWER_OPENED, null));
        sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        container = (FrameLayout) findViewById(R.id.container);
        navDrawer = (NavigationView) findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setDrawer(mDrawerLayout, toolbar);
        navDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);


                switch (menuItem.getItemId()) {
                    case R.id.red:
                        switchFragments(container.getId(), fragments[0]);
                        break;

                    case R.id.green:
                        switchFragments(container.getId(), fragments[1]);
                        break;

                    case R.id.blue:
                        switchFragments(container.getId(), fragments[2]);
                        break;

                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }


    public void setDrawer(final DrawerLayout drawerLayout, Toolbar toolbar) {


        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    saveToPrefs(MainActivity.this, KEY_DRAWER_OPENED, mUserLearnedDrawer + "");
                    supportInvalidateOptionsMenu();
                    drawerOpen = true;
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
                drawerOpen = false;
            }

        };


        drawerLayout.setDrawerListener(mDrawerToggle);

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.prefs_name), Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();

    }

    public static String readFromPrefs(Context context, String key, String defVal) {
        SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.prefs_name), Context.MODE_PRIVATE);
        return sp.getString(key, defVal);

    }

    public void switchFragments(int containerId, Fragment fragmentToShow) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(containerId, fragmentToShow).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();


    }

    @Override
    public void onBackPressed() {

        if (drawerOpen) {
            mDrawerLayout.closeDrawers();
            return;
        }

        super.onBackPressed();
    }

    public  boolean isInternetConnected(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

}
