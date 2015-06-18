package com.zari.matan.navigationdrawerexample.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lyuzik.remoteimageview.OnCompleteCB;
import com.lyuzik.remoteimageview.RGestureImageView;
import com.zari.matan.navigationdrawerexample.FeedAdapter;
import com.zari.matan.navigationdrawerexample.MainActivity;
import com.zari.matan.navigationdrawerexample.Network.Callback;
import com.zari.matan.navigationdrawerexample.Network.Executor;
import com.zari.matan.navigationdrawerexample.Network.MainHttpTask;
import com.zari.matan.navigationdrawerexample.Network.Request;
import com.zari.matan.navigationdrawerexample.R;
import com.zari.matan.navigationdrawerexample.helper.ExpendImage;
import com.zari.matan.navigationdrawerexample.helper.FragmentUiLifeCycleHelper;
import com.zari.matan.navigationdrawerexample.items.FeedItem;
import com.zari.matan.navigationdrawerexample.items.GifItem;
import com.zari.matan.navigationdrawerexample.items.ImageItem;
import com.zari.matan.navigationdrawerexample.items.ItemData;
import com.zari.matan.navigationdrawerexample.items.MP4VideoItem;
import com.zari.matan.navigationdrawerexample.items.PostItem;
import com.zari.matan.navigationdrawerexample.items.RSSPostItem;
import com.zari.matan.navigationdrawerexample.items.YouTubeVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import static com.zari.matan.navigationdrawerexample.MainActivity.mMediaPlayer;
import static com.zari.matan.navigationdrawerexample.MainActivity.mYoutubePlayer;


/**
 * Created by Matan on 4/19/2015
 */

public class HomeFragment extends Fragment implements FragmentUiLifeCycleHelper, ExpendImage, AbsListView.OnScrollListener {

    public ListView listView;
    View v;
    MainActivity activity;
    FeedAdapter adapter;
    ArrayList<FeedItem> data;
    RelativeLayout zoomContainer;
    RGestureImageView gestureImageView;
    ProgressBar imagePreLoader;
    ProgressBar feedPreLoader;
    MainHttpTask httpTask;
    Executor executor;
    private boolean isPlayerReset;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.home_fragment_layout, container, false);
        listView = (ListView) v.findViewById(R.id.home_feed);
        zoomContainer = (RelativeLayout) v.findViewById(R.id.zoom_image_container);
        imagePreLoader = (ProgressBar) LayoutInflater.from(getActivity()).inflate(R.layout.image_preloader, zoomContainer, false);
        feedPreLoader = (ProgressBar) v.findViewById(R.id.feedPreLoader);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activity.isInternetConnected()) {
            httpTask = new MainHttpTask(activity);
            Request request = new Request();
            request.urlStr = "http://wolflo.com/walls/system/killswitchengage/all?skip=0&limit=30";
            request.callback = homeCallback;
            executor = new Executor();
            executor.execute(httpTask, request);
            feedPreLoader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onFragmentResumed() {

    }


    @Override
    public void openZoom(ItemData itemData) {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        alphaAnimation.setDuration(300);
        zoomContainer.removeAllViews();
        zoomContainer.addView(imagePreLoader);
        alphaAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
                zoomContainer.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {

            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }
        });
        zoomContainer.startAnimation(alphaAnimation);
        gestureImageView = (RGestureImageView) LayoutInflater.from(getActivity()).inflate(R.layout.expended_image, zoomContainer, false);
        gestureImageView.setTargetSize(500, 500);
        gestureImageView.setAdjustViewBounds(true);
        gestureImageView.loadImageBitmap(itemData.img, onCompleteCB, true);

    }


    OnCompleteCB onCompleteCB = new OnCompleteCB() {
        @Override
        public void complete() {
            zoomContainer.removeAllViews();
            zoomContainer.addView(gestureImageView);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            gestureImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeZoomMode();
                }
            });
        }
    };


    private void closeZoomMode() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                zoomContainer.setVisibility(View.GONE);
            }
        });
        zoomContainer.startAnimation(alphaAnimation);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }



    Callback homeCallback = new Callback() {
        @Override
        public void handleResponse(String response) {
            try {
                if (response != null) {
                    activity.sp.edit().putString("savedResult", response).apply();
                }
                if (feedPreLoader.getVisibility() == View.VISIBLE) {
                    feedPreLoader.setVisibility(View.GONE);
                }
                JSONArray items = new JSONArray(response);
                if (data == null)
                    data = new ArrayList<>();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    ItemData itemData = ItemData.parsUrl(getActivity(), item);

                    assert itemData != null;
                    if (itemData.type.equals("image")) {

                        if (!itemData.img.contains(".gif"))
                            data.add(new ImageItem(activity, itemData, HomeFragment.this));

                        else
                            data.add(new GifItem(activity,itemData));

                    } else if (itemData.type.equals("video") && !itemData.source.equals("youtube") && !itemData.source.equals("vimeo")) {
                        data.add(new MP4VideoItem(activity, itemData));
                    } else if (itemData.type.equals("video") && itemData.source.equals("youtube")) {
                        data.add(new YouTubeVideo(activity, itemData));
                    } else if (itemData.type.equals("post")) {
                        if (itemData.source.equals("rss"))
                            data.add(new RSSPostItem(itemData, activity));
                        else
                            data.add(new PostItem(activity, itemData));
                    }
                }
                if (adapter == null) {

                    adapter = new FeedAdapter(activity, 0, data);
                    listView.setAdapter(adapter);
                    listView.setOnScrollListener(HomeFragment.this);
                } else {
                    adapter.notifyDataSetChanged();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        for (int i = 0; i < visibleItemCount; i++) {
            View listItem = view.getChildAt(i);
            int viewType = adapter.getItemViewType(firstVisibleItem);

            if (i == 0) {
                if (mMediaPlayer != null && viewType == FeedAdapter.types.MP4VIDEO.ordinal()) {

                    if (listItem.getTop() < -listItem.getHeight() * 0.75) {
                        if (!isPlayerReset) {
                            MP4VideoItem item = (MP4VideoItem) adapter.getItem(firstVisibleItem);
                            item.onCompletion(mMediaPlayer);
                            mMediaPlayer = null;
                            isPlayerReset = true;
                        } else
                            return;
                    }

                } else if (mYoutubePlayer != null
                        && viewType == FeedAdapter.types.YOUTUBEVIDEO.ordinal()) {
                    if (listItem.getTop() < -listItem.getHeight() / 2) {
                        YouTubeVideo ytv = (YouTubeVideo) adapter.getItem(firstVisibleItem);
                        ytv.onVideoEnded();
                    }

                } else {
                    isPlayerReset = false;
                    return;
                }

            }
        }
    }

    public  View getViewInstance(Context context){

        return LayoutInflater.from(context).inflate(R.layout.home_fragment_layout,null,false);
    }


}