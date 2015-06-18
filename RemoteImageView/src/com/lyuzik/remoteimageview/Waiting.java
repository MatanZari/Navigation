package com.lyuzik.remoteimageview;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class Waiting {

	private Bitmap mBitmap;
	private RImageView mImg;
	private AlphaAnimation mSetBitmapAnimation;
	private int requestCount = -1;

	public RImageView getImg() {
		return mImg;
	}

	public Waiting(int requestCount, Bitmap bitmap, RImageView img,
			boolean withAnimation) {
		this.requestCount = requestCount;
		mBitmap = bitmap;
		mImg = img;
		if (withAnimation) {
			mSetBitmapAnimation = new AlphaAnimation(0, 1);
			mSetBitmapAnimation.setDuration(200);
			mSetBitmapAnimation.setAnimationListener(new SetBitmapListener());
		}
	}

	public void setBitmap() {
		if (mImg.getContext() == null || mImg.getContext().isRestricted()) {
			return;
		}
		if (requestCount == mImg.getRequestCount() && mBitmap != null) {
			if (mSetBitmapAnimation != null) {
				mImg.startAnimation(mSetBitmapAnimation);
			} else {
				mImg.setImageBitmap(mBitmap);
				if (mImg.isAutoVisibility()) {
					mImg.setVisibility(View.VISIBLE);
				}
				if (mImg.getSuccessCB() != null) {
					mImg.getSuccessCB().complete();
					mImg.setSuccessCB(null);
				}
			}
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
}
