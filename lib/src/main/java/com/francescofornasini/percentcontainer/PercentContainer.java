package com.francescofornasini.percentcontainer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by franc on 05/05/2017.
 */

public class PercentContainer extends FrameLayout {
    private static final String TAG = "PercentContainer";

    private float percent = 0.1f;

    public PercentContainer(@NonNull Context context) {
        this(context, null);
    }

    public PercentContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);

        Log.d(TAG, "onMeasure() called with: widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        Log.d(TAG, "onMeasure() called with: heightMeasureSpecSize = [" + size + "], heightMeasureSpecMode = [" + mode + "]");

        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.round(size * percent), MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);

    }
}
