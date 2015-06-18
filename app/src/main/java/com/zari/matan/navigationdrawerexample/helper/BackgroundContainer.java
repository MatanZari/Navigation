package com.zari.matan.navigationdrawerexample.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.zari.matan.navigationdrawerexample.R;


/**
 * Created by Matan on 5/31/2015
 */
public class BackgroundContainer extends FrameLayout {

    boolean mShowing = false;
    Drawable mShadowedBackground;
    int mOpenAreaTop, mOpenAreaBottom, mOpenAreaHeight;
    boolean mUpdateBounds = false;
    public int color;

    public BackgroundContainer(Context context) {
        super(context);
        init();



    }

    public BackgroundContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackgroundContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mShadowedBackground = getResources().getDrawable(R.drawable.shadowed_background);
        setBackgroundColor(color);
    }

    public void showBackground(int top, int bottom) {
        mShowing = true;
        setWillNotDraw(false);
        mOpenAreaTop = top;
        mOpenAreaHeight = bottom;
        mUpdateBounds = true;
        Log.e("showBackground","showBackground");
    }

    public void hideBackground() {
        setWillNotDraw(true);
        mShowing = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowing) {

            Log.e("onDraw","onDraw");
            if (mUpdateBounds) {
                mShadowedBackground.setBounds(0, 0, getWidth(), mOpenAreaHeight);

            }
            //canvas.drawColor(R.color.colorAccent);
            canvas.save();
            canvas.translate(0, mOpenAreaTop);
           // canvas.drawColor(R.color.colorAccent);
            mShadowedBackground.draw(canvas);
            canvas.restore();
        }
    }

    public void setColor(int color) {
        this.color = color;
    }


}
