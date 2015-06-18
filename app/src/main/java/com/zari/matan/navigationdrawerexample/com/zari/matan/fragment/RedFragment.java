package com.zari.matan.navigationdrawerexample.com.zari.matan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zari.matan.navigationdrawerexample.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedFragment extends Fragment implements View.OnClickListener {


    @InjectView(R.id.btnSearch)
    Button btn;

    public RedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_red, container, false);
        ButterKnife.inject(this,layout);
        btn.setOnClickListener(this);
        return layout;
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(),"clicked",Toast.LENGTH_SHORT).show();
    }
}
