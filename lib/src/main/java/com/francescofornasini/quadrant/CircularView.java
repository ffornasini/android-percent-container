package com.francescofornasini.quadrant;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.francescofornasini.percentcontainer.R;

/**
 * Created by francesco on 02/01/15.
 */
public class CircularView extends View {

    private static final float DEFAULT_RADIANS_MIN = 0f;
    private static final float DEFAULT_RADIANS_MAX = 2f;

    protected float mRadiansMin;
    protected float mRadiansMax;

    public CircularView(Context context) {
        this(context, null);
    }

    public CircularView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircularView,
                0, 0);

        try {

            mRadiansMin = a.getFloat(R.styleable.CircularView_radians_min, DEFAULT_RADIANS_MIN);
            mRadiansMax = a.getFloat(R.styleable.CircularView_radians_max, DEFAULT_RADIANS_MAX);

        } finally {
            a.recycle();
        }
    }
}
