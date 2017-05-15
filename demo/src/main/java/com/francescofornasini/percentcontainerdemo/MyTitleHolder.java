package com.francescofornasini.percentcontainerdemo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.francescofornasini.percentcontainer.PercentContainer;
import com.francescofornasini.percentcontainer.RecyclerPagerIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by franc on 11/05/2017.
 */

class MyTitleHolder extends RecyclerPagerIndicator.IndicatorHolder {

    @BindView(R.id.percent)
    PercentContainer percent;

    @BindView(R.id.text)
    TextView text;

    public MyTitleHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView(int position, int currentPosition, RecyclerView source) {
        super.bindView(position, currentPosition, source);
        percent.setPercentX(0.51f);
        text.setText("Title #" + position);

        if (position == currentPosition) {
            text.setTextColor(Color.MAGENTA);
            text.setScaleX(1.1f);
            text.setScaleY(1.1f);
        } else {
            text.setTextColor(Color.LTGRAY);
            text.setScaleX(1f);
            text.setScaleY(1f);
        }

    }
}
