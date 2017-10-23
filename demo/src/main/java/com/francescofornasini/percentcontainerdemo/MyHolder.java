package com.francescofornasini.percentcontainerdemo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.francescofornasini.percentcontainer.PercentContainer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by franc on 11/05/2017.
 */

class MyHolder extends RecyclerView.ViewHolder {

    int[] colors = {
            Color.GRAY,
            Color.GREEN,
            Color.CYAN
    };

    @BindView(R.id.percent)
    PercentContainer mPercentView;

    @BindView(R.id.text)
    TextView mTextView;

    public MyHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(String s, int position) {
        //mPercentView.setPercentX(0.1f * ((position % 3) + 2));
        //mPercentView.setPercentX(0.8f);
        mTextView.setText(s);
        mTextView.setBackgroundColor(colors[position % colors.length]);
    }
}
