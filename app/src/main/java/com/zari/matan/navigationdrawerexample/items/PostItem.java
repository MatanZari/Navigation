package com.zari.matan.navigationdrawerexample.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lyuzik.remoteimageview.RImageView;
import com.zari.matan.navigationdrawerexample.FeedAdapter;
import com.zari.matan.navigationdrawerexample.InAppViewUrlActivity;
import com.zari.matan.navigationdrawerexample.R;
import com.zari.matan.navigationdrawerexample.helper.Utils;


/**
 * Created by Matan on 6/2/2015
 */

public class PostItem implements FeedItem, View.OnClickListener {

    Context context;
    ItemData itemData;
    ViewHolder holder;
    int dp = Utils.getDP1();

    int coverSize = 35 * 2 * dp;
    int imageWidth = Utils.getScreenWidth();
    int imageHeight = 200 * dp;


    public PostItem(Context context, ItemData itemData) {
        this.context = context;
        this.itemData = itemData;
    }

    @Override
    public int getViewType() {
        return FeedAdapter.types.POST.ordinal();
    }
    @SuppressLint("InflateParams")
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.post_item_layout, null, false);
            holder = new ViewHolder();
            holder.cover = (RImageView) convertView.findViewById(R.id.postItemCover);
            holder.name = (TextView) convertView.findViewById(R.id.postItemName);
            holder.time = (TextView) convertView.findViewById(R.id.postItemTime);
            holder.title = (TextView) convertView.findViewById(R.id.postItemTitle);
            holder.desc = (TextView) convertView.findViewById(R.id.postItemDesc);
            holder.link = (TextView) convertView.findViewById(R.id.postItemLink);
            holder.image = (RImageView) convertView.findViewById(R.id.postItemImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cover.setTargetSize(coverSize, coverSize);
        holder.cover.loadImageCircleBitmap(itemData.collData.cover);
        holder.name.setText(itemData.collData.title);
        holder.time.setText(itemData.dateShort + " ago on " + context.getString(itemData.sourceStrId));
        holder.title.setText(itemData.text);
        holder.desc.setText(itemData.description);
        holder.link.setText(itemData.externalUrl);
        if (itemData.img != null) {
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setTargetSize(imageWidth, imageHeight);
            holder.image.loadImageBitmap(itemData.img);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.link.setTextColor(Color.BLUE);
        holder.link.setOnClickListener(this);
        return convertView;
    }

    @Override
    public int getClickType() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, InAppViewUrlActivity.class);
        intent.putExtra("itemData", itemData.toJson().toString());
        intent.putExtra("url", holder.link.getText());
        context.startActivity(intent);
    }

    private class ViewHolder {
        TextView name;
        TextView time;
        TextView title;
        TextView desc;
        TextView link;
        RImageView cover;
        RImageView image;
    }
}
