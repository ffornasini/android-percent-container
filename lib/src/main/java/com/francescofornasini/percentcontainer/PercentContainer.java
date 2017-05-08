package com.francescofornasini.percentcontainer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by franc on 05/05/2017.
 */

public class PercentContainer extends FrameLayout {

    private float percentX = -1f;
    private float percentY = -1f;

    public PercentContainer(@NonNull Context context) {
        this(context, null);
    }

    public PercentContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PercentContainer,
                0, 0);

        try {
            percentX = a.getFloat(R.styleable.PercentContainer_x, percentX);
            percentY = a.getFloat(R.styleable.PercentContainer_y, percentY);
        } finally {
            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int defWidthMeasureSpec = percentX >= 0 ?
                MeasureSpec.makeMeasureSpec(Math.round(width * percentX), MeasureSpec.EXACTLY) :
                widthMeasureSpec;

        int defHeightMeasureSpec = percentY >= 0 ?
                MeasureSpec.makeMeasureSpec(Math.round(height * percentY), MeasureSpec.EXACTLY) :
                heightMeasureSpec;

        super.onMeasure(defWidthMeasureSpec, defHeightMeasureSpec);
    }
}
