package com.lyuzik.remoteimageview;

public interface ZoomAnimationListener {
	public void onZoom(float scale, float x, float y);

	public void onComplete();
}