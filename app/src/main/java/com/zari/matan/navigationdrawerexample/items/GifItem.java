package com.zari.matan.navigationdrawerexample.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyuzik.remoteimageview.RImageView;
import com.zari.matan.navigationdrawerexample.FeedAdapter;
import com.zari.matan.navigationdrawerexample.R;
import com.zari.matan.navigationdrawerexample.helper.Utils;

/**
 * Created by Matan on 5/27/2015
 */
public class GifItem implements FeedItem{

    Context context;
    ItemData itemData;
    ViewHolder holder;
    LinearLayout root;
    private static int ITEM_W_SIZE = Utils.getScreenWidth();
    private static int ITEM_H_SIZE = ITEM_W_SIZE * 9 / 16;

    public GifItem(Context context, ItemData itemData) {
        this.context = context;
        this.itemData = itemData;
    }

    @Override
    public int getViewType() {
        return FeedAdapter.types.GIF.ordinal();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gif_item_layout, null, false);
            holder = new ViewHolder();
            FrameLayout gifContainer = (FrameLayout) convertView.findViewById(R.id.gif_container);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            RImageView cover = (RImageView) convertView.findViewById(R.id.gifItemCover);
            WebView gif = (WebView) convertView.findViewById(R.id.gif);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            TextView text = (TextView) convertView.findViewById(R.id.text);
            root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.cover = cover;
            holder.root = root;
            holder.gif = gif;
            holder.name = name;
            holder.time = time;
            holder.text = text;
            holder.gifContainer = gifContainer;
            holder.gif.setLayoutParams(new FrameLayout.LayoutParams(ITEM_W_SIZE, ITEM_H_SIZE));
            holder.gif.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    holder.gif.setVisibility(View.VISIBLE);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int dp = Utils.getDP1();
        holder.cover.setTargetSize(35 * dp * 2, 35 * dp * 2);
        holder.cover.loadImageCircleBitmap(itemData.collData.cover);
        playGif(itemData.img,holder.gif);
        holder.time.setText(itemData.dateShort + " ago on " + context.getString(itemData.sourceStrId));
        holder.name.setText(itemData.collData.title);

        holder.text.setText(itemData.text);
        return convertView;
    }

    @Override
    public int getClickType() {
        return 0;


    }



    private class ViewHolder {
        TextView name;
        FrameLayout gifContainer;
        RImageView cover;
        WebView gif;
        TextView time;
        TextView text;
        LinearLayout root;

    }


    private void playGif(String url, WebView gif) {
        holder.gif.setVisibility(View.INVISIBLE);
        String data = "<html><body><img src=" + url
                + " width='100%' height='100%'/></body></html>";
        gif.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
    }

}
