package com.zari.matan.navigationdrawerexample.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyuzik.remoteimageview.RImageView;
import com.zari.matan.navigationdrawerexample.R;


/**
 * Created by Matan on 4/19/2015
 */
public class DiscoverFragment extends Fragment {
    private CallActivity listener;
    private RImageView rImageView;
    private Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View pageLayout = inflater.inflate(R.layout.user_profile_fragment,container,false);
        //rImageView = (RImageView) pageLayout.findViewById(R.id.profile_picture);
        toolbar = (Toolbar) pageLayout.findViewById(R.id.toolbar);


        return  pageLayout;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CallActivity){
            listener = (CallActivity) activity;



        }
    }

    public interface CallActivity{
        public void getLayout(View v);
    }




}
