package com.zari.matan.navigationdrawerexample.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyuzik.remoteimageview.RImageView;
import com.zari.matan.navigationdrawerexample.FeedAdapter;
import com.zari.matan.navigationdrawerexample.MainActivity;
import com.zari.matan.navigationdrawerexample.R;
import com.zari.matan.navigationdrawerexample.fragments.HomeFragment;
import com.zari.matan.navigationdrawerexample.helper.BackgroundContainer;
import com.zari.matan.navigationdrawerexample.helper.ExpendImage;
import com.zari.matan.navigationdrawerexample.helper.ListViewItemAnimation;
import com.zari.matan.navigationdrawerexample.helper.Utils;

/**
 * Created by Matan on 5/18/2015
 */
public class ImageItem implements FeedItem, View.OnClickListener, ListViewItemAnimation.SwipeCallback {
    private ExpendImage zoom;
    ItemData itemData;
    Context context;
    ViewHolder holder = null;
    MainActivity activity;
    HomeFragment homeFragment;
    BackgroundContainer backgroundContainer;
    ListViewItemAnimation itemAnimation;
    ListView listView;
    RelativeLayout webViewContainer;
    View homeFragmentLayout;


    public ImageItem(Context context, ItemData itemData, ExpendImage zoom) {



        this.context = context;
        this.itemData = itemData;
        this.itemData.socialBgColor = itemData.socialBgColors.get(ItemData.getSourceStringId(itemData.source));
        this.zoom = zoom;
        activity = (MainActivity) context;
        homeFragment = new HomeFragment();
        homeFragmentLayout = homeFragment.getViewInstance(activity);
        listView = (ListView) homeFragmentLayout.findViewById(R.id.home_feed);

    }


    @Override
    public int getViewType() {
        return FeedAdapter.types.IMAGE.ordinal();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(LayoutInflater inflater, View convertView) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_item_layout, null, false);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            RImageView cover = (RImageView) convertView.findViewById(R.id.imageItemCover);
            RImageView image = (RImageView) convertView.findViewById(R.id.item_image);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView text = (TextView) convertView.findViewById(R.id.text);
            FrameLayout itemBackground = (FrameLayout) convertView.findViewById(R.id.itemBackground);
            backgroundContainer = (BackgroundContainer) homeFragmentLayout.findViewById(R.id.listBackgroundContainer);
            webViewContainer = (RelativeLayout) homeFragmentLayout.findViewById(R.id.webViewContainer);
            holder = new ViewHolder();
            holder.webViewContainer = webViewContainer;
            holder.cover = cover;
            holder.name = name;
            holder.backgroundContainer = backgroundContainer;
            holder.image = image;
            holder.time = time;
            holder.text = text;
            holder.itemBackground = itemBackground;
            convertView.setTag(holder);
            Log.e("getView","newView");
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int dp = Utils.getDP1();
        holder.cover.setTargetSize(35 * dp * 2, 35 * dp * 2);
        holder.cover.loadImageCircleBitmap(itemData.collData.cover);
        holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // holder.image.setAdjustViewBounds(true);
        int fff = dp * 300;
        holder.image.setTargetSize(fff, fff);
        holder.image.loadImageBitmap(itemData.img);
        holder.time.setText(itemData.dateShort + " ago on " + context.getString(itemData.sourceStrId));
        holder.name.setText(itemData.collData.title);
        holder.text.setText(itemData.text);
        holder.image.setOnClickListener(this);

        if (itemData.externalUrl != null) {
            Log.e("itemData.socialBgColor",itemData.socialBgColor+"");
            itemAnimation = new ListViewItemAnimation(context, holder.image,listView, this, holder.itemBackground);
            itemAnimation.setSocialBgColor(itemData.socialBgColor);
            holder.image.setOnTouchListener(itemAnimation.mTouchListener);
        }

        return convertView;
    }

    @Override
    public int getClickType() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (zoom != null)
            zoom.openZoom(itemData);
    }

    @Override
    public void onSwipe() {
        openWeb(itemData.externalUrl);
    }


    private class ViewHolder {
        TextView time;
        TextView name;
        RImageView cover;
        RImageView image;
        TextView text;
        BackgroundContainer backgroundContainer;
        RelativeLayout webViewContainer;
        FrameLayout itemBackground;
    }


    @SuppressLint("SetJavaScriptEnabled")
    public void openWeb(String url) {


        final WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        RImageView x = new RImageView(context);
        LinearLayout linearLayout = new LinearLayout(context);


        x.setImageResource(R.drawable.x);
        x.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        x.layout(linearLayout.getLeft(), linearLayout.getTop(), 0, 0);

        holder.webViewContainer.addView(webView);
        RelativeLayout.LayoutParams webViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        webView.setId(MainActivity.generateViewId());
        RelativeLayout.LayoutParams linearLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        linearLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        holder.webViewContainer.addView(linearLayout);
        linearLayout.setId(MainActivity.generateViewId());
        webViewLayoutParams.addRule(RelativeLayout.BELOW, linearLayout.getId());
        webView.setLayoutParams(webViewLayoutParams);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.addView(x);
        linearLayout.setBackgroundColor(Color.parseColor("#80000000"));
        holder.webViewContainer.setVisibility(View.VISIBLE);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.clearHistory();
                holder.webViewContainer.removeAllViews();
                holder.webViewContainer.setVisibility(View.GONE);


            }
        });
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });
    }
}
