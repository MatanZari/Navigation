package com.zari.matan.navigationdrawerexample.items;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class CollData {

	private static final String TAG = CollData.class.getName();
	public String _id;
	public String username;
	public String name;
	public String title;
	public String cover;
	public boolean agelimit = false;
	public boolean followed = false;
	public String bgColor;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CollData))
			return false;
		CollData collData = (CollData) o;
		return collData._id.equals(this._id);
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("_id", _id);
			json.put("username", username);
			json.put("name", name);
			json.put("cover", cover);
			json.put("title", title);
			json.put("agelimit", agelimit);
			json.put("followed", followed);
			if (bgColor != null)
				json.put("bgColor", bgColor);
		} catch (JSONException e) {

				Log.e(TAG, "toJson", e);
		}
		return json;
	}

	public static CollData parsUrl(JSONObject jObj) {
		try {
			CollData collData = new CollData();
			collData._id = jObj.getString("_id");
			collData.username = "system";
			collData.name = jObj.getString("name");
			collData.title = jObj.getString("title");
			collData.cover = jObj.getString("cover");
			if (jObj.has("agelimit")) {
				collData.agelimit = jObj.getBoolean("agelimit");
			}
			if (jObj.has("followed")) {
				collData.followed = jObj.getBoolean("followed");
			}
			if (jObj.has("bgColor")) {
				collData.bgColor = jObj.getString("bgColor").replace("0x", "#");
			}
			return collData;
		} catch (JSONException e) {

				Log.e(TAG, " parsUrl", e);
		}
		return null;
	}

	public static CollData pars(String jObjStr) {
		try {
			return parsUrl(new JSONObject(jObjStr));
		} catch (JSONException e) {

				Log.e(TAG, " pars", e);
		}
		return null;
	}
}
