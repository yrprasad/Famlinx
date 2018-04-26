package com.nglinx.pulse.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nglinx.pulse.models.FenceModel;

import java.util.List;

public class FenceAdapter extends ArrayAdapter<FenceModel> {

    private List<FenceModel> fenceList;

    private LayoutInflater mInflater;
    private Context context;

    public FenceAdapter(Context context, List<FenceModel> fenceList) {
        super(context, 0, fenceList);
        this.context = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fenceList = fenceList;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return fenceList.size();
    }

    @Override
    public FenceModel getItem(int position) {
        return fenceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(fenceList.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(fenceList.get(position).getName());

        return label;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

