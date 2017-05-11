package com.francescofornasini.percentcontainer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by franc on 11/05/2017.
 */

public class EnhancedRecyclerView extends RecyclerView {

    private boolean adjustPaddingHorizontal;
    private boolean adjustPaddingVertical;
    private int paddingStart;
    private int paddingEnd;
    private int paddingTop;
    private int paddingBottom;

    private void refreshPadding() {

        int newPaddingStart;
        int newPaddingEnd;
        int newPaddingTop;
        int newPaddingBottom;

        if (getAdapter() == null || getLayoutManager() == null) {
            return;
        }

        if (getAdapter().getItemCount() == 0) {
            newPaddingStart = 0;
            newPaddingEnd = 0;
            newPaddingTop = 0;
            newPaddingBottom = 0;
        } else {

            if (adjustPaddingHorizontal) {
                try {
                    newPaddingStart = (getWidth() / 2) - (getLayoutManager().findViewByPosition(0).getWidth() / 2);
                } catch (Exception e) {
                    newPaddingStart = paddingStart;
                }

                try {
                    newPaddingEnd = (getWidth() / 2) - (getLayoutManager().findViewByPosition(getAdapter().getItemCount() - 1).getWidth() / 2);
                } catch (Exception e) {
                    newPaddingEnd = paddingEnd;
                }
            } else {
                newPaddingStart = paddingStart;
                newPaddingEnd = paddingEnd;
            }

            if (adjustPaddingVertical) {
                try {
                    newPaddingTop = (getHeight() / 2) - (getLayoutManager().findViewByPosition(getAdapter().getItemCount() - 1).getHeight() / 2);
                } catch (Exception e) {
                    newPaddingTop = paddingTop;
                }

                try {
                    newPaddingBottom = (getHeight() / 2) - (getLayoutManager().findViewByPosition(getAdapter().getItemCount() - 1).getHeight() / 2);
                } catch (Exception e) {
                    newPaddingBottom = paddingBottom;
                }
            } else {
                newPaddingTop = paddingTop;
                newPaddingBottom = paddingBottom;
            }
        }

        if (newPaddingStart != paddingStart || newPaddingEnd != paddingEnd || newPaddingTop != paddingTop || newPaddingBottom != paddingBottom) {
            setPadding(newPaddingStart, newPaddingTop, newPaddingEnd, newPaddingBottom);
            paddingStart = newPaddingStart;
            paddingEnd = newPaddingEnd;
            paddingTop = newPaddingTop;
            paddingBottom = newPaddingBottom;
        }
    }

    public EnhancedRecyclerView(Context context) {
        this(context, null);
    }

    public EnhancedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnhancedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setClipToPadding(false);
        setClipChildren(false);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EnhancedRecyclerView,
                0, 0);

        try {
            adjustPaddingHorizontal = a.getBoolean(R.styleable.EnhancedRecyclerView_adjust_padding_horizontal, false);
            adjustPaddingVertical = a.getBoolean(R.styleable.EnhancedRecyclerView_adjust_padding_vertical, false);
        } finally {
            a.recycle();
        }

    }

    public void setAdjustPaddingHorizontal(boolean adjustPaddingHorizontal) {
        this.adjustPaddingHorizontal = adjustPaddingHorizontal;
    }

    public void setAdjustPaddingVertical(boolean adjustPaddingVertical) {
        this.adjustPaddingVertical = adjustPaddingVertical;
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (adjustPaddingHorizontal || adjustPaddingVertical) {
            child.post(new Runnable() {
                @Override
                public void run() {
                    EnhancedRecyclerView.this.refreshPadding();
                }
            });
        }
    }
}
