package com.zari.matan.navigationdrawerexample.items;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Matan on 5/17/2015.
 */
public interface FeedItem {

    public int getViewType();

    public View getView(LayoutInflater inflater, View convertView);

    public int getClickType();

//    public ItemData getItemData();
//
//    public CollData getCollData();
}
