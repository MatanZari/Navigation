package com.zari.matan.navigationdrawerexample.items;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.webkit.URLUtil;


import com.zari.matan.navigationdrawerexample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Matan on 5/18/2015
 */
public class ItemData {

    private static final String TAG = ItemData.class.getName();
    public String _id;
    public String type;
    public String source;
    public int sourceStrId;
    public String externalId;
    public boolean iLiked = false;
    public long time;
    public String date;
    public String dateShort;
    public String text;
    public String img;
    public String description;
    public String videoUrl;
    public String externalUrl;
    public String link;
    public CollData collData;
    public String shareBase64;
    public String thumbnail;
    public int socialBgColor;

    HashMap<Integer,Integer> socialBgColors = getSocialBgColors();
    private static final HashMap<String, Integer> sourceToDrowIdMap = getSourceToDrowIdMap();

    public JSONObject toJson() {
        JSONObject jObjItem = new JSONObject();
        try {
            jObjItem.put("type", type);
            jObjItem.put("collData", collData.toJson());
            jObjItem.put("source", source);
            jObjItem.put("sourceStrId", sourceStrId);
            jObjItem.put("iLiked", iLiked);
            jObjItem.put("dateShort", dateShort);
            jObjItem.put("date", date);
            jObjItem.put("time", time);
            jObjItem.put("_id", _id);
            if (externalId != null) {
                jObjItem.put("externalId", externalId);
            }
            if (text != null) {
                jObjItem.put("text", text);
            }
            if (description != null) {
                jObjItem.put("description", description);
            }
            if (img != null) {
                jObjItem.put("img", img);
            }
            if (videoUrl != null) {
                jObjItem.put("videoUrl", videoUrl);
            }
            if (thumbnail != null){
                jObjItem.put("thumbnail",thumbnail);
            }
            if (externalUrl != null) {
                jObjItem.put("externalUrl", externalUrl);
            }
            if (link != null) {
                jObjItem.put("link", link);
            }
            if (shareBase64 != null) {
                jObjItem.put("shareBase64", shareBase64);
            }
        } catch (JSONException e) {

                Log.e(TAG, "toJson", e);
        } catch (NullPointerException e) {

                Log.w(TAG, "parsCollectionUrl", e);
        }
        return jObjItem;
    }


    public static ItemData pars(String jObjStr) {
        try {
            return pars(new JSONObject(jObjStr));
        } catch (JSONException e) {
                Log.e(TAG, " pars string", e);
        }
        return null;
    }

    public static ItemData pars(JSONObject jObj) {
        try {
            ItemData item = new ItemData();
            item.type = jObj.getString("type");
            item.collData = CollData.pars(jObj.getJSONObject("collData")
                    .toString());
            item.source = jObj.getString("source");
            item.sourceStrId = jObj.getInt("sourceStrId");
            item.iLiked = jObj.getBoolean("iLiked");
            item.dateShort = jObj.getString("dateShort");
            item.date = jObj.getString("date");
            item.time = jObj.getLong("time");
            item._id = jObj.getString("_id");
            if (jObj.has("externalId")) {
                item.externalId = jObj.getString("externalId");
            }
            if (jObj.has("text")) {
                item.text = jObj.getString("text");
            }
            if (jObj.has("description")) {
                item.description = jObj.getString("description");
            }
            if (jObj.has("img")) {
                item.img = jObj.getString("img");
            }
            if (jObj.has("videoUrl")) {
                item.videoUrl = jObj.getString("videoUrl");
            }
            if (jObj.has("thumbnail")){
                item.thumbnail = jObj.getString("thumbnail");
            }
            if (jObj.has("externalUrl")) {
                item.externalUrl = jObj.getString("externalUrl");
            }
            if (jObj.has("link")) {
                item.link = jObj.getString("link");
            }
            if (jObj.has("shareBase64")) {
                item.shareBase64 = jObj.getString("shareBase64");
            }

            return item;
        } catch (JSONException e) {

                Log.e(TAG, "pars json", e);
        } catch (NullPointerException e) {

                Log.e(TAG, "pars json", e);
        }
        return null;
    }

    public ItemData clone() {
        ItemData itemData = new ItemData();
        itemData._id = this._id;
        itemData.collData = CollData.pars(this.collData.toJson().toString());
        itemData.dateShort = this.dateShort;
        itemData.date = this.date;
        itemData.time = this.time;
        itemData.description = this.description;
        itemData.externalId = this.externalId;
        itemData.externalUrl = this.externalUrl;
        itemData.link = this.link;
        itemData.iLiked = this.iLiked;
        itemData.img = this.img;
        itemData.source = this.source;
        itemData.sourceStrId = this.sourceStrId;
        itemData.text = this.text;
        itemData.type = this.type;
        itemData.videoUrl = this.videoUrl;
        itemData.shareBase64 = this.shareBase64;
        return itemData;
    }



    private static final long MIN = DateUtils.MINUTE_IN_MILLIS;
    private static final long HOUR = DateUtils.HOUR_IN_MILLIS;
    private static final long DAY = DateUtils.DAY_IN_MILLIS;
    private static final long WEEK = DateUtils.WEEK_IN_MILLIS;
    private static final long YEAR = DateUtils.YEAR_IN_MILLIS;

    private static String getShortDateFormat(Context context, long time) {
        String letter;
        int shortTime = 0;
        long interval = new Date().getTime() - time;
        if (interval / YEAR > 0) {
            shortTime = (int) (interval / YEAR);
            letter = context.getString(R.string.short_year);
        } else if (interval / WEEK > 0) {
            shortTime = (int) (interval / WEEK);
            letter = context.getString(R.string.short_week);
        } else if (interval / DAY > 0) {
            shortTime = (int) (interval / DAY);
            letter = context.getString(R.string.short_day);
        } else if (interval / HOUR > 0) {
            shortTime = (int) (interval / HOUR);
            letter = context.getString(R.string.short_hour);
        } else {
            shortTime = (int) (interval / MIN);
            letter = context.getString(R.string.short_min);
        }
        return Integer.toString(shortTime) + letter;
    }


    public static ItemData parsUrl(Context context, JSONObject jObj) {
        try {
            ItemData item = new ItemData();
            item.type = jObj.getString("type");
            item.collData = CollData.parsUrl(jObj.getJSONObject("wall"));
            item.source = jObj.getString("source");
            item.sourceStrId = getSourceStringId(item.source);
            item._id = jObj.getString("_id");
            if (jObj.has("externalId")) {
                item.externalId = jObj.getString("externalId");
            }
            if (jObj.has("liked")) {
                item.iLiked = jObj.getBoolean("liked");
            }
            Date date;
            if (jObj.has("rewalled")) {
                try {
                    TimeZone utc = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat f = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    f.setTimeZone(utc);
                    GregorianCalendar cal = new GregorianCalendar(utc);
                    cal.setTime(f.parse(jObj.getString("rewalled")));
                    date = cal.getTime();
                } catch (Exception e) {

                        Log.w(TAG, "pars date format failed.", e);
                    date = new Date();
                }
            } else {
                date = new Date();
            }
            item.time = date.getTime();
            item.dateShort = getShortDateFormat(context, date.getTime());
            String newDate = DateUtils.getRelativeTimeSpanString(
                    date.getTime(), new Date().getTime(),
                    DateUtils.SECOND_IN_MILLIS).toString();
            item.date = newDate;
            if (jObj.has("title") && jObj.getString("title").length() > 0) {
                item.text = jObj.getString("title");
            }
            if (jObj.has("description")
                    && jObj.getString("description").length() > 0) {
                item.description = jObj.getString("description");
            }
            if (jObj.has("image")) {
                String imgUrl = jObj.getString("image");
                if (URLUtil.isValidUrl(imgUrl)) {
                    item.img = imgUrl;
                } else {

                        Log.w(TAG, "not valid url:" + imgUrl);
                }
            }
            if (jObj.has("videoUrl")) {
                item.videoUrl = jObj.getString("videoUrl");
            }
            if (jObj.has("thumbnail")){
                item.thumbnail = jObj.getString("thumbnail");
            }
            if (jObj.has("externalUrl")) {
                item.externalUrl = jObj.getString("externalUrl");
            }
            if (jObj.has("link")) {
                item.link = jObj.getString("link");
            }
            if (jObj.has("base64")) {
                item.shareBase64 = jObj.getString("base64");
            }
            return item;
        } catch (JSONException e) {

                Log.e(TAG, "parsUrl", e);

                Log.e(TAG, "parsUrl", e);
        }
        return null;
    }


    public static HashMap<String, Integer> getSourceToDrowIdMap() {
        HashMap<String, Integer> newMap = new HashMap<String, Integer>();
        newMap.put("instagram", R.string.instagram_src);
        newMap.put("twitter", R.string.twitter_src);
        newMap.put("flickr", R.string.flickr_src);
        newMap.put("youtube", R.string.youtube_src);
        newMap.put("soundcloud", R.string.soundcloud_src);
        newMap.put("vimeo", R.string.vimeo_src);
        newMap.put("pinterest", R.string.pinterest_src);
        newMap.put("rss", R.string.rss_src);
        newMap.put("tumblr", R.string.tumblr_src);
        newMap.put("bandsintown", R.string.bandsintown_src);
        newMap.put("spotify", R.string.spotify_src);
        newMap.put("facebook", R.string.facebook_src);
        newMap.put("vine", R.string.vine_src);
        newMap.put("googleplus", R.string.googleplus_src);
        newMap.put("linkedin", R.string.linkedin_src);
        return newMap;
    }

    public static int getSourceStringId(String itemSource) {
        Integer id = sourceToDrowIdMap.get(itemSource);
        if (id == null)
            return R.string.none;
        return id;
    }

    public  int getSocialBgColor(int key){
        return socialBgColors.get(key);
    }


    public HashMap<Integer, Integer> getSocialBgColors() {
        HashMap<Integer, Integer> newMap = new HashMap<>();
        newMap.put(R.string.twitter_src, R.color.twitter);
        newMap.put(R.string.instagram_src, R.color.instagram);
        newMap.put(R.string.flickr_src, R.color.flickr);
        newMap.put(R.string.youtube_src, R.color.youtube);
        newMap.put(R.string.soundcloud_src, R.color.soundcloud);
        newMap.put(R.string.vimeo_src, R.color.vimeo);
        newMap.put(R.string.pinterest_src, R.color.pinterest);
        newMap.put(R.string.rss_src, R.color.rss);
        newMap.put(R.string.tumblr_src, R.color.tumblr);
        newMap.put(R.string.facebook_src, R.color.facebook);
        newMap.put(R.string.vine_src, R.color.vine);
        newMap.put(R.string.googleplus_src, R.color.googleplus);
        newMap.put(R.string.linkedin_src, R.color.linkedin);
        return newMap;
    }
}
