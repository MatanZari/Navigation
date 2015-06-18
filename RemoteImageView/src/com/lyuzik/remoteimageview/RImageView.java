package com.lyuzik.remoteimageview;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class RImageView extends ImageView {

	public RImageView(Context context) {
		this(context, null);
	}

	public RImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mLoader = new RLoader(this);
	}

	protected static final String TAG = RImageView.class.getSimpleName();
	public static final int ERROR_LOGLEVEL = 4;
	public static final int WARN_LOGLEVEL = 3;
	public static final int INFO_LOGLEVEL = 2;
	public static final int DEBUG_LOGLEVEL = 1;
	public static final int VERBOSE_LOGLEVEL = 0;

	private static int LOGLEVEL = ERROR_LOGLEVEL;
	protected static boolean ERROR = LOGLEVEL < 5;
	protected static boolean WARN = LOGLEVEL < 4;
	protected static boolean INFO = LOGLEVEL < 3;
	protected static boolean DEBUG = LOGLEVEL < 2;
	protected static boolean VERBOSE = LOGLEVEL < 1;

	private boolean autoVisibility = true;

	private OnCompleteCB mCompleteCB;
	private RLoader mLoader;
	private String mUrl;
	private int mTargetWidth;
	private int mTargetHeight;
	private int requestCount = 0;
	private static float mCachePercent = 0.3f;
	private static boolean mIsOnPause = false;
	private final static ArrayList<Waiting> mWaiting = new ArrayList<Waiting>();
	public static final int BUFF_SIZE = 5 * 1024 * 1024;

	protected String getUrl() {
		return mUrl;
	}

	protected int getTargetWidth() {
		return mTargetWidth;
	}

	protected int getTargetHeight() {
		return mTargetHeight;
	}

	public static boolean isOnPause() {
		return mIsOnPause;
	}

	public static void setOnPause(boolean isOnPause) {
		RImageView.mIsOnPause = isOnPause;
		if (!mIsOnPause) {
			synchronized (mWaiting) {
				for (int i = mWaiting.size() - 1; i >= 0; i--) {
					mWaiting.remove(i).setBitmap();
				}
			}
		}
	}

	protected static void addWaiting(Waiting waiting) {
		removeWaiting(waiting.getImg());
		synchronized (mWaiting) {
			mWaiting.add(waiting);
		}
	}

	protected static void removeWaiting(RImageView rImageView) {
		synchronized (mWaiting) {
			for (int i = 0; i < mWaiting.size(); i++) {
				if (mWaiting.get(i).getImg().equals(rImageView)) {
					mWaiting.remove(mWaiting.get(i));
					return;
				}
			}
		}
	}

	protected static float getCachePercent() {
		return mCachePercent;
	}

	public static void setCachePercent(float percent) {
		if (percent > 0 && percent < 1) {
			RImageView.mCachePercent = percent;
		} else {
			if (RImageView.WARN) {
				Log.w(TAG, "not valid cache percent:" + percent
						+ " (0<value<1)");
			}
		}
	}

	public static int getLogLevel() {
		return LOGLEVEL;
	}

	public static void setLogLevel(int logLevel) {
		RImageView.LOGLEVEL = logLevel;
		ERROR = LOGLEVEL < 5;
		WARN = LOGLEVEL < 4;
		INFO = LOGLEVEL < 3;
		DEBUG = LOGLEVEL < 2;
		VERBOSE = LOGLEVEL < 1;
	}

	protected OnCompleteCB getSuccessCB() {
		return mCompleteCB;
	}

	public void setSuccessCB(OnCompleteCB successCB) {
		this.mCompleteCB = successCB;
	}

	protected boolean isAutoVisibility() {
		return autoVisibility;
	}

	public void setAutoVisibility(boolean autoVisibility) {
		this.autoVisibility = autoVisibility;
	}

	public void setTargetSize(int width, int height) {
		if (width > 0 && height > 0) {
			mTargetWidth = width;
			mTargetHeight = height;
		} else {
			if (RImageView.ERROR) {
				Log.e(TAG, "not valid target width&height:" + width + "&"
						+ height);
			}
		}
	}

	public void loadImageBitmap(String url) {
		if (mUrl == null || !mUrl.equals(url)) {
			requestCount++;
			mUrl = url;
			mLoader.load();
		} else {
			if (getSuccessCB() != null) {
				getSuccessCB().complete();
				setSuccessCB(null);
			}
		}
	}

	public void loadImageBitmap(String url, OnCompleteCB completeCB) {
		mCompleteCB = completeCB;
		loadImageBitmap(url);
	}

	public void loadImageBitmap(String url, boolean skipCache) {
		mLoader.setWithAnimation(false);
		mLoader.setSkipCache(skipCache);
		loadImageBitmap(url);
	}

	public void loadImageCircleBitmap(String url) {
		mLoader.setIsCircle(true);
		loadImageBitmap(url);
	}

	public void loadImageCircleBitmap(String url, OnCompleteCB completeCB) {
		mCompleteCB = completeCB;
		mLoader.setIsCircle(true);
		loadImageBitmap(url);
	}

	public void loadImageBitmap(String url, OnCompleteCB completeCB,
			boolean skipCache) {
		mLoader.setWithAnimation(false);
		mLoader.setSkipCache(skipCache);
		loadImageBitmap(url, completeCB);
	}

	protected int getRequestCount() {
		return requestCount;
	}
}