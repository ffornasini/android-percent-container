package com.francescofornasini.percentcontainer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by franc on 11/05/2017.
 */

public class RecyclerPagerIndicator extends EnhancedRecyclerViewV3 {

    public static final boolean DEFAULT_WRAP = true;
    public static final boolean DEFAULT_STABLE = true;

    public static final int UPDATE_ALWAYS = 0;
    public static final int UPDATE_ON_SCROLL_STATUS_CHANGE = 1;


    private final IndicatorAdapter adapter;
    private final LinearSnapHelper linearSnapHelper;
    private boolean wrap = DEFAULT_WRAP;
    private boolean stable = DEFAULT_STABLE;
    private RecyclerView source;
    private int currentPosition = -1;
    private String tag;

    public RecyclerPagerIndicator(Context context) {
        this(context, null);
    }

    public RecyclerPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RecyclerPagerIndicator,
                0, 0);

        try {
            wrap = a.getBoolean(R.styleable.RecyclerPagerIndicator_wrap, DEFAULT_WRAP);
            stable = a.getBoolean(R.styleable.RecyclerPagerIndicator_stable, DEFAULT_STABLE);
        } finally {
            a.recycle();
        }

        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        setOverScrollMode(OVER_SCROLL_NEVER);
        adapter = new IndicatorAdapter();
        setAdapter(adapter);

        if (!wrap) {
            setAdjustPaddingHorizontal(true);
            setItemAnimator(null);
        }

        linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(this);
    }


    public void setRecycler(final EnhancedRecyclerViewV3 source, final int flags, String tag) {
        this.tag = tag;
        post(new Runnable() {
            @Override
            public void run() {
                RecyclerPagerIndicator.this.source = source;
                adapter.notifyDataSetChanged();

                updateCurrentPosition(source.getCurrentPage());

                if (flags == UPDATE_ALWAYS) {

                    source.addOnPageChangeListener(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int oldPage, int newPage) {
                            updateCurrentPosition(newPage);
                        }
                    });

                } else if (flags == UPDATE_ON_SCROLL_STATUS_CHANGE) {

                    source.addOnScrollListener(new OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            updateCurrentPosition(source.getCurrentPage());
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });
                }
            }
        });
    }

    private void updateCurrentPosition(int newPage) {
        int position = newPage;

        if (position != currentPosition && position >= 0 && position < adapter.getItemCount()) {
            int oldPosition = currentPosition;
            currentPosition = position;
            //TODO test me
            adapter.notifyItemRangeChanged(Math.min(oldPosition, currentPosition), Math.abs(currentPosition - oldPosition) + 1);
            innerScrollToPosition(this, position);
        }
    }

    private int findCenterPosition() {
        //TODO find why findFirstCompletelyVisibleItemPosition and findLastCompletelyVisibleItemPosition return always the same index..
        LinearLayoutManager layoutManager = (LinearLayoutManager) source.getLayoutManager();
        return layoutManager.findFirstCompletelyVisibleItemPosition();
        /*
        int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();

        Log.i(tag, "" + layoutManager.findFirstCompletelyVisibleItemPosition());
        Log.i(tag, "" + layoutManager.findLastCompletelyVisibleItemPosition());

        for (int i = firstCompletelyVisibleItemPosition; i < lastCompletelyVisibleItemPosition; i++) {
            View viewByPosition = getChildAt(i);

            if ((viewByPosition.getLeft() + viewByPosition.getWidth() / 2) == (getWidth() / 2)) {
                return i;
            }
        }
        */
    }

    public void setExternalHolder(IIndicatorHolder externalHolder) {
        this.externalHolder = externalHolder;
    }

    private class IndicatorAdapter extends Adapter<IndicatorHolder> {

        public IndicatorAdapter() {
            setHasStableIds(stable);
        }

        @Override
        public long getItemId(int position) {
            if (!stable) {
                return super.getItemId(position);
            }

            if (currentPosition == position) {
                return -1 /* * (position + 1) */;
            }

            if (position >= currentPosition) {
                return position - 1;
            }

            return position;
        }

        @Override
        public IndicatorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return externalHolder.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(IndicatorHolder holder, int position) {
            holder.bindView(position, currentPosition, source);
        }

        @Override
        public int getItemCount() {
            return source == null ? 0 : source.getAdapter().getItemCount();
        }
    }

    public static class IndicatorHolder extends ViewHolder {

        public IndicatorHolder(View itemView) {
            super(itemView);
        }

        public void bindView(int position, int currentPosition, final RecyclerView source) {
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    innerScrollToPosition(source, IndicatorHolder.this.getAdapterPosition());
                }
            });
        }
    }

    private IIndicatorHolder externalHolder = new IIndicatorHolder() {
        @Override
        public IndicatorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }
    };

    public interface IIndicatorHolder {
        IndicatorHolder onCreateViewHolder(ViewGroup parent, int viewType);
    }

    private static void innerScrollToPosition(RecyclerView recyclerView, int position) {
        //TODO this won't work if there is more than one element present in the same window at a time...
        recyclerView.smoothScrollToPosition(position);
    }
}
