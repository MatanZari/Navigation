package com.zari.matan.navigationdrawerexample.items;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lyuzik.remoteimageview.RImageView;
import com.zari.matan.navigationdrawerexample.FeedAdapter;
import com.zari.matan.navigationdrawerexample.MainActivity;
import com.zari.matan.navigationdrawerexample.R;
import com.zari.matan.navigationdrawerexample.helper.Utils;

import java.io.IOException;


/**
 * Created by Matan on 5/19/2015
 */
public class MP4VideoItem implements FeedItem, TextureView.SurfaceTextureListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
        View.OnClickListener, MediaPlayer.OnErrorListener, MediaController.MediaPlayerControl {
    Context context;
    ItemData itemData;
    private MediaPlayer mMediaPlayer;
    public static boolean isPlaying = false;
    ViewHolder holder = null;
    public static MediaController mController;
    private Handler handler = new Handler();
    FrameLayout videoContainer;
    int dp = Utils.getDP1();
    public MP4VideoItem(Context context, ItemData itemData) {
        this.context = context;
        this.itemData = itemData;
        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

    }

    @Override
    public int getViewType() {
        return FeedAdapter.types.MP4VIDEO.ordinal();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(LayoutInflater inflater, View convertView) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mp4_video_item_layout, null, false);
            holder = new ViewHolder();
            TextureView video = (TextureView) convertView.findViewById(R.id.video);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            RImageView cover = (RImageView) convertView.findViewById(R.id.videoItemCover);
            ImageButton play = (ImageButton) convertView.findViewById(R.id.play);
            RImageView thumbnail = (RImageView) convertView.findViewById(R.id.thumbnail);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            ProgressBar loader = (ProgressBar) convertView.findViewById(R.id.loader);
            videoContainer = (FrameLayout) convertView.findViewById(R.id.video_container);
            holder.cover = cover;
            holder.thumbnail = thumbnail;
            holder.play = play;
            holder.name = name;
            holder.video = video;
            holder.time = time;
            holder.loader = loader;
            holder.videoContainer = videoContainer;
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cover.setTargetSize(35 * dp * 2, 35 * dp * 2);
        holder.cover.loadImageCircleBitmap(itemData.collData.cover);
        holder.time.setText(itemData.dateShort + " ago on " + itemData.source);
        holder.name.setText(itemData.collData.title);
        holder.thumbnail.setTargetSize(300 * dp, 300 * dp);
        holder.thumbnail.loadImageBitmap(itemData.thumbnail);
        holder.thumbnail.setAdjustViewBounds(true);
        holder.thumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.play.setOnClickListener(this);
        holder.play.setSoundEffectsEnabled(false);
        holder.video.setTag(itemData.time);
        holder.video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mController != null && holder.video.isAvailable()) {
                    if (mController.isShowing()) {
                        mController.hide();
                    } else {
                        mController.show(1000);
                    }
                }


                return false;
            }
        });


        return convertView;
    }

    @Override
    public int getClickType() {
        return 0;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);
        try {
            if (MainActivity.mMediaPlayer != null) {
                MainActivity.mMediaPlayer = null;
            }
            MainActivity.mMediaPlayer = new MediaPlayer();
            mMediaPlayer = MainActivity.mMediaPlayer;
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(itemData.videoUrl);
            mMediaPlayer.setSurface(s);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mController = new MediaController(context);
        } catch (IllegalArgumentException
                | IllegalStateException
                | SecurityException
                | IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (holder != null) {
            mMediaPlayer.release();
            if (mController.isShowing())
                mController.hide();
            mController = null;
            setVideoLoadingAnimation(holder.video, 1.0f, 0);
            setVideoLoadingAnimation(holder.thumbnail, 0, 1.0f);
            holder.play.setVisibility(View.VISIBLE);

        }
        isPlaying = false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mController.setMediaPlayer(this);
        handler.post(new Runnable() {

            public void run() {
                mController.setEnabled(true);
                mController.show(1000);
            }
        });
        mController.setAnchorView(holder.video);
        setVideoLoadingAnimation(holder.thumbnail, 1.0f, 0);
        setVideoLoadingAnimation(holder.video, 0, 1.0f);
        holder.loader.setVisibility(View.GONE);
        mMediaPlayer.start();
        isPlaying = true;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }

    @Override
    public void onClick(View v) {
        if (holder != null && holder.video != null) {
            if (mMediaPlayer != null) {
                if (isPlaying)
                    mMediaPlayer.stop();
                mMediaPlayer.release();
                MainActivity.mMediaPlayer = null;
                holder.video.setSurfaceTextureListener(null);
            }
            if (holder.video.isAvailable()) {
                onSurfaceTextureAvailable(holder.video.getSurfaceTexture(), holder.video.getWidth(), holder.video.getHeight());
            }
            holder.play.setVisibility(View.GONE);
            holder.video.setSurfaceTextureListener(this);
            holder.loader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(what + "", extra + "");
        return false;
    }

    @Override
    public void start() {
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
        if (holder != null) {
            mMediaPlayer.release();
            if (mController.isShowing())
                mController.hide();
            mController = null;
            setVideoLoadingAnimation(holder.video, 1.0f, 0);
            setVideoLoadingAnimation(holder.thumbnail, 0, 1.0f);
            holder.play.setVisibility(View.VISIBLE);
        }
        isPlaying = false;
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null) {
            try {
                return mMediaPlayer.getDuration();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        try {
            return mMediaPlayer.getCurrentPosition();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void seekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {

        try {
            return mMediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mMediaPlayer.getAudioSessionId();
    }




    private class ViewHolder {
        TextureView video;
        TextView name;
        RImageView cover;
        RImageView thumbnail;
        ImageButton play;
        TextView time;
        ProgressBar loader;
        public FrameLayout videoContainer;
    }


    public static void setVideoLoadingAnimation(final View toAnimate, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the
        anim.setDuration(300);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                toAnimate.setHasTransientState(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toAnimate.setHasTransientState(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        toAnimate.startAnimation(anim);

    }
}
