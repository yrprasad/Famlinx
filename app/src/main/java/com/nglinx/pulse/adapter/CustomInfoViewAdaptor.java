package com.nglinx.pulse.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nglinx.pulse.models.UserTrackingModel;
import com.nglinx.pulse.utils.ApplicationUtils;

public class CustomInfoViewAdaptor implements GoogleMap.InfoWindowAdapter {

    Context mContext;
    UserTrackingModel userTrackingModel;

    public CustomInfoViewAdaptor(Context context, UserTrackingModel userTrackingModel) {
        this.mContext = context;
        this.userTrackingModel = userTrackingModel;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        LinearLayout info = new LinearLayout(mContext);
        info.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(mContext);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        title.setText(marker.getTitle());

        TextView snippet = new TextView(mContext);
        snippet.setTextColor(Color.GRAY);
        snippet.setText(ApplicationUtils.constructMemberTitle(this.userTrackingModel));
        info.addView(title);
        info.addView(snippet);

        return info;
    }
}
