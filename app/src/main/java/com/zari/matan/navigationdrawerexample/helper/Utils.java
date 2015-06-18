package com.zari.matan.navigationdrawerexample.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.MediaController;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Created by Matan on 5/28/2015
 */
public class Utils {

    private static int DP1 = -1;
    private static int screenWidth = -1;
    private static int screenHeight = -1;
    private static MediaController mc;

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void init(Context context) {
        float n = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1F,
                context.getResources().getDisplayMetrics());
        DP1 = (int) Math.ceil(n);
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Activity.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        } else {
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }
        mc = new MediaController(context);
    }

    public static int getDP1() {
        if (DP1 != -1) {
            return DP1;
        }
        return 2;
    }

    public static String getLN() {
        return Locale.getDefault().getLanguage();
    }

    public static int getScreenWidth() {
        if (screenWidth != -1) {
            return screenWidth;
        }
        return 480;
    }

    public static int getScreenHeight() {
        if (screenHeight != -1)
            return screenHeight;
        return 640;
    }

    public static String stringByAddingPercentEscapesUsingEncoding(
            String input, String charset) throws UnsupportedEncodingException {
        final byte[] bytes = input.getBytes(charset);
        StringBuilder sb = new StringBuilder(bytes.length);
        for (int i = 0; i < bytes.length; ++i) {
            int cp = bytes[i] < 0 ? bytes[i] + 256 : bytes[i];
            if (cp <= 0x20
                    || cp >= 0x7F
                    || (cp == 0x22 || cp == 0x25 || cp == 0x3C || cp == 0x3E
                    || cp == 0x20 || cp == 0x5B || cp == 0x5C
                    || cp == 0x5D || cp == 0x5E || cp == 0x60
                    || cp == 0x7b || cp == 0x7c || cp == 0x7d)) {
                sb.append(String.format("%%%02X", cp));
            } else {
                sb.append((char) cp);
            }
        }
        return sb.toString();
    }

    public static String stringByAddingPercentEscapesUsingEncoding(String input) {
        try {
            return stringByAddingPercentEscapesUsingEncoding(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(
                    "Java platforms are required to support UTF-8");
        }
    }

    public static MediaController getMediaController() {
        return mc;
    }



}
