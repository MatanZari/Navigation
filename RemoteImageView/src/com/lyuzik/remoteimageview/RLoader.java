package com.lyuzik.remoteimageview;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.URLUtil;

public class RLoader {

	private RImageView mImg;
	private Bitmap mBitmap;
	private LoadTask mLoadTask;
	private Waiting mWaiting;
	private boolean mSkipCache = false;
	private boolean mIsCircle = false;
	private static RCache mCache;
	private AlphaAnimation mSetBitmapAnimation;

	private final static int corePoolSizeImg = 15;
	private final static int maxPoolSizeImg = 25;
	private final static int timeOutInSecsImg = 20;
	private final static LinkedBlockingQueue<Runnable> workQueueImg = new LinkedBlockingQueue<Runnable>(
			maxPoolSizeImg);
	private final static ExecutorService executor = new ThreadPoolExecutor(
			corePoolSizeImg, maxPoolSizeImg, timeOutInSecsImg,
			TimeUnit.SECONDS, workQueueImg);

	private boolean mWithAnimation = true;

	public void setWithAnimation(boolean withAnimation) {
		this.mWithAnimation = withAnimation;
	}

	public void setSkipCache(boolean skipCache) {
		this.mSkipCache = skipCache;
	}

	public RLoader(RImageView img) {
		mImg = img;
		if (mCache == null) {
			ActivityManager am = (ActivityManager) mImg.getContext()
					.getSystemService(Context.ACTIVITY_SERVICE);
			long memClassBytes = am.getMemoryClass() * RCache.MB;
			int cacheSize = (int) (memClassBytes * RImageView.getCachePercent());
			mCache = new RCache(cacheSize);

		}
		mSetBitmapAnimation = new AlphaAnimation(0, 1);
		mSetBitmapAnimation.setDuration(200);
		mSetBitmapAnimation.setAnimationListener(new SetBitmapListener());
	}

	protected void load() {
		if (mLoadTask != null && !mLoadTask.isCancelled()) {
			mLoadTask.cancel(true);
		}
		if (mImg.isAutoVisibility()) {
			mImg.setVisibility(View.INVISIBLE);
		}
		if (mWaiting != null) {
			RImageView.removeWaiting(mWaiting.getImg());
		}
		mBitmap = getFromCache();
		if (mBitmap == null) {
			executeLoadInBG();
		} else {
			setBitmap();
			if (RImageView.INFO) {
				Log.i(RImageView.TAG,
						"cash:" + mImg.getUrl() + "%" + mIsCircle + "%"
								+ mImg.getTargetWidth() + "X"
								+ mImg.getTargetHeight());
			}
		}
	}

	private class LoadTask extends AsyncTask<Void, Void, Void> {
		private int requestCount = -1;
		private String url;

		public LoadTask(int requestCount, String url) {
			this.requestCount = requestCount;
			this.url = url;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (mImg.getTargetWidth() > 0 && mImg.getTargetHeight() > 0) {
				mBitmap = loadBitmap(mImg, url, mIsCircle, mSkipCache);
			} else {
				if (RImageView.WARN) {
					Log.w(RImageView.TAG,
							"not valid target width&height:"
									+ mImg.getTargetWidth() + "&"
									+ mImg.getTargetHeight());
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (mBitmap == null) {
				if (RImageView.WARN) {
					Log.w(RImageView.TAG, "bitmap is null , url: " + url);
				}
				return;
			}
			if (mImg.getContext() == null || mImg.getContext().isRestricted()) {
				return;
			}
			if (isCancelled()) {
				return;
			}
			setBitmap(requestCount);
			super.onPostExecute(result);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void executeLoadInBG() {
		mLoadTask = new LoadTask(mImg.getRequestCount(), mImg.getUrl());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (workQueueImg.size() == maxPoolSizeImg) {
				workQueueImg.poll();
			}
			mLoadTask.executeOnExecutor(executor);
		} else {
			try {
				mLoadTask.execute();
			} catch (RejectedExecutionException e) {
				if (RImageView.WARN) {
					Log.w(RImageView.TAG, "execute", e);
				}
			}
		}
	}

	private void setBitmap(int requestCount) {
		if (RImageView.isOnPause()) {
			mWaiting = new Waiting(requestCount, mBitmap, mImg, mWithAnimation);
			RImageView.addWaiting(mWaiting);
		} else {
			if (requestCount == mImg.getRequestCount() && mBitmap != null) {
				if (mWithAnimation) {
					mImg.startAnimation(mSetBitmapAnimation);
				} else {
					setBitmap();
				}
			}
		}
	}

	private void setBitmap() {
		mImg.setImageBitmap(mBitmap);
		if (mImg.getSuccessCB() != null) {
			mImg.getSuccessCB().complete();
			mImg.setSuccessCB(null);
		}
		if (mImg.isAutoVisibility()) {
			mImg.setVisibility(View.VISIBLE);
		}
	}

	private class SetBitmapListener implements AnimationListener {
		@Override
		public void onAnimationEnd(Animation animation) {
			if (mImg.getSuccessCB() != null) {
				mImg.getSuccessCB().complete();
				mImg.setSuccessCB(null);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {
			mImg.setImageBitmap(mBitmap);
			if (mImg.isAutoVisibility()) {
				mImg.setVisibility(View.VISIBLE);
			}
		}
	}

	private Bitmap getFromCache() {
		String key = mImg.getUrl() + "%" + mIsCircle + "%"
				+ mImg.getTargetWidth() + "X" + mImg.getTargetHeight();
		return mCache.get(key);
	}

	private static void addToCache(RImageView img, String url, Bitmap value,
			boolean isCircle) {
		String key = url + "%" + isCircle + "%" + img.getTargetWidth() + "X"
				+ img.getTargetHeight();
		mCache.set(key, value);
	}

	private static Bitmap loadBitmap(RImageView img, String urlStr,
			boolean isCircle, boolean skipCache) {
		if (urlStr != null && URLUtil.isValidUrl(urlStr)) {
			URL url = null;
			try {
				url = new URL(urlStr);
			} catch (MalformedURLException e) {
				if (RImageView.WARN) {
					Log.w(RImageView.TAG, "getUrl failed. url:" + img.getUrl(),
							e);
				}
			}
			if (url != null) {
				BitmapFactory.Options options = null;
				InputStream input = null;
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) url.openConnection();
					connection.connect();
					input = connection.getInputStream();
					options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(input, null, options);
				} catch (IOException e) {
					if (RImageView.WARN) {
						Log.w(RImageView.TAG, "loadOptions failed. url:" + url,
								e);
					}
				} catch (OutOfMemoryError e) {
					mCache.clear();
					System.gc();
					if (RImageView.ERROR) {
						Log.e(RImageView.TAG, "loadOptions failed", e);
					}
				} finally {
					try {
						if (input != null) {
							input.close();
						}
						if (connection != null) {
							connection.disconnect();
						}
					} catch (Exception e) {
						if (RImageView.WARN) {
							Log.w(RImageView.TAG,
									"loadOptions close&disconnect failed", e);
						}
					}
				}
				if (options != null) {
					try {
						connection = (HttpURLConnection) url.openConnection();
						connection.setDoInput(true);
						connection.connect();
						input = connection.getInputStream();
						options.inSampleSize = 1;
						if (options.outHeight > img.getTargetHeight()
								|| options.outWidth > img.getTargetWidth()) {
							while ((options.outHeight / options.inSampleSize) > img
									.getTargetHeight()
									|| (options.outWidth / options.inSampleSize) > img
											.getTargetWidth()) {
								options.inSampleSize *= 2;
							}
							if (RImageView.INFO) {
								Log.i(RImageView.TAG,
										"scale("
												+ options.inSampleSize
												+ ")"
												+ " target:"
												+ img.getTargetWidth()
												+ "X"
												+ img.getTargetHeight()
												+ " , from:"
												+ options.outWidth
												+ "X"
												+ options.outHeight
												+ " to:"
												+ (options.outWidth / options.inSampleSize)
												+ "X"
												+ (options.outHeight / options.inSampleSize));
							}
						}
						options.inJustDecodeBounds = false;
						options.inPurgeable = true;// avoiding memory leaks
						Bitmap result = BitmapFactory.decodeStream(input, null,
								options);
						if (isCircle && result != null) {
							result = RLoader.getCircle(result);
						}
						if (!skipCache && result != null) {
							RLoader.addToCache(img, urlStr, result, isCircle);
						}
						return result;
					} catch (IOException e) {
						if (RImageView.WARN) {
							Log.w(RImageView.TAG, "load bitmap failed. url:"
									+ url, e);
						}
					} catch (OutOfMemoryError e) {
						mCache.clear();
						System.gc();
						if (RImageView.ERROR) {
							Log.e(RImageView.TAG, "loadBitmap failed", e);
						}
					} finally {
						try {
							if (input != null) {
								input.close();
							}
							if (connection != null) {
								connection.disconnect();
							}
						} catch (Exception e) {
							if (RImageView.WARN) {
								Log.w(RImageView.TAG,
										"loadBitmap close&disconnect failed", e);
							}
						}
					}
				}
			}
		} else {
			if (RImageView.ERROR) {
				Log.e(RImageView.TAG, "not valid url:" + urlStr);
			}
		}
		return null;
	}

	protected void setIsCircle(boolean isCircle) {
		this.mIsCircle = isCircle;
	}

	private static Bitmap getCircle(Bitmap source) {
		float width = source.getWidth();
		float height = source.getHeight();
		float diameter = Math.min(height, width);
		int d = Math.round(diameter);
		int radius = (int) (diameter / 2);
		final Bitmap result = Bitmap.createBitmap(d, d, Config.ARGB_8888);
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		final Canvas canvas = new Canvas(result);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(radius, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		if (result != source) {
			source.recycle();
		}
		return result;
	}
}
