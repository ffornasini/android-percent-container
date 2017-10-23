package com.francescofornasini.percentcontainerdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import com.francescofornasini.percentcontainer.EnhancedRecyclerViewV3;
import com.francescofornasini.percentcontainer.LinePagerIndicatorDecoration;
import com.francescofornasini.percentcontainer.RecyclerPagerIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    EnhancedRecyclerViewV3 mRecycler;

    @BindView(R.id.titler_indicator)
    RecyclerPagerIndicator mRecyclerTitlerIndicator;

    @BindView(R.id.pager_indicator)
    RecyclerPagerIndicator mRecyclerPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> mRecycler.smoothScrollToPosition(7));

        mRecycler.setAdapter(new MyAdapter());
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        SnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRecycler);

        mRecycler.addItemDecoration(new LinePagerIndicatorDecoration());

        mRecyclerTitlerIndicator.setRecycler(mRecycler, RecyclerPagerIndicator.UPDATE_ALWAYS, "TITLER");
        mRecyclerPagerIndicator.setRecycler(mRecycler, RecyclerPagerIndicator.UPDATE_ON_SCROLL_STATUS_CHANGE, "PAGER");

        //TODO bug when there is not enough space to scroll the view before padding is applied
        mRecycler.smoothScrollToPosition(1);

        mRecyclerPagerIndicator.setExternalHolder((parent, viewType) -> new RecyclerPagerIndicator.IndicatorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_indicator, parent, false)) {

            @Override
            public void bindView(int position, int currentPosition, RecyclerView source) {
                super.bindView(position, currentPosition, source);
                if (position == currentPosition) {
                    itemView.setBackgroundResource(R.drawable.circle);
                } else {
                    itemView.setBackgroundResource(R.drawable.circle_light);
                }
            }
        });

        mRecyclerTitlerIndicator.setExternalHolder((parent, viewType) -> new MyTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_titler, parent, false)));

    }

}
