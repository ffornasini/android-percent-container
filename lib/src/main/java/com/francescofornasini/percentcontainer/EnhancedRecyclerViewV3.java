package com.francescofornasini.percentcontainer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.ACTION_MOVE;


/**
 * Created by franc on 11/05/2017.
 */

public class EnhancedRecyclerViewV3 extends EnhancedRecyclerViewV2 {
    public static final String TAG = "EnhancedRecyclerViewV3";
    public static final boolean DEFAULT_USER_SCROLLABLE = true;
    private boolean userScrollable = DEFAULT_USER_SCROLLABLE;

    public EnhancedRecyclerViewV3(Context context) {
        this(context, null);
    }

    public EnhancedRecyclerViewV3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnhancedRecyclerViewV3(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EnhancedRecyclerViewV3,
                0, 0);

        try {
            userScrollable = a.getBoolean(R.styleable.EnhancedRecyclerViewV3_user_scrollable, DEFAULT_USER_SCROLLABLE);
        } finally {
            a.recycle();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return (!userScrollable && ev.getAction() == ACTION_MOVE) || super.dispatchTouchEvent(ev);
    }
}
