package com.francescofornasini.percentcontainerdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franc on 11/05/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    static List<String> data;

    {
        data = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            data.add("asd" + i);
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.bindView(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
