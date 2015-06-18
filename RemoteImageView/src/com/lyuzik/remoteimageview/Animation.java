package com.lyuzik.remoteimageview;

public interface Animation {

	/**
	 * Transforms the view.
	 * 
	 * @param view
	 * @param diffTime
	 * @return true if this animation should remain active. False otherwise.
	 */
	public boolean update(RGestureImageView view, long time);

}