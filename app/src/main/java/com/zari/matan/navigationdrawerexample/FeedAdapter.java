package com.zari.matan.navigationdrawerexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.zari.matan.navigationdrawerexample.items.FeedItem;

import java.util.ArrayList;


/**
 * Created by Matan on 5/18/2015
 */
public class FeedAdapter extends ArrayAdapter<FeedItem> {
    Context context;
    ArrayList<FeedItem> data;
    LayoutInflater inflater;

    public static enum types {
        IMAGE, MP4VIDEO, RSSPOST, YOUTUBEVIDEO,GIF,POST
    }

    public FeedAdapter(Context context, int resource, ArrayList<FeedItem> data) {
        super(context, resource, data);
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(inflater, convertView);
    }


    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public int getViewTypeCount() {
        return types.values().length;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
