package com.francescofornasini.percentcontainer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.francescofornasini.percentcontainer.EnhancedRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by franc on 11/05/2017.
 */

public class EnhancedRecyclerViewV2 extends EnhancedRecyclerView {
    public static final String TAG = "EnhancedRecyclerViewV2";

    private List<OnPageChangeListener> pageChangeListeners = new ArrayList<>();

    private LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
    private int currentPage = NO_POSITION;


    public EnhancedRecyclerViewV2(Context context) {
        this(context, null);
    }

    public EnhancedRecyclerViewV2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnhancedRecyclerViewV2(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.d(TAG, "onScrolled() called with: recyclerView = [" + recyclerView + "], dx = [" + dx + "], dy = [" + dy + "]");
                //Log.d(TAG, "onScrolled()" + recyclerView.getScrollX());
                //Log.d(TAG, "onScrolled()" + getLayoutManager().getPosition(new LinearSnapHelper().findSnapView(getLayoutManager())));
                int snapPosition = getCurrentPage();

                if (snapPosition != currentPage) {
                    int oldPage = currentPage;
                    currentPage = snapPosition;

                    for (OnPageChangeListener pageChangeListener : pageChangeListeners) {
                        pageChangeListener.onPageChanged(oldPage, currentPage);
                    }
                }
            }
        });
    }

    public int getCurrentPage() {
        View snapView = linearSnapHelper.findSnapView(getLayoutManager());
        return snapView == null ? 0 : getLayoutManager().getPosition(snapView);
    }

    public interface OnPageChangeListener {
        void onPageChanged(int oldPage, int newPage);
    }

    //TODO remove also listeners!
    public void addOnPageChangeListener(OnPageChangeListener l) {
        pageChangeListeners.add(l);
    }


}
