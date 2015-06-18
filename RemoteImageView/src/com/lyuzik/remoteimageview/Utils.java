package com.lyuzik.remoteimageview;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;

public class Utils {

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public final static long sizeOf(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return bitmap.getAllocationByteCount();
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		} else {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}

}
