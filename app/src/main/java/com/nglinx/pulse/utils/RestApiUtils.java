package com.nglinx.pulse.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.CustomInfoViewAdaptor;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;


/**
 * Created by yelisetr on 1/26/2017.
 */

public class RestApiUtils {
    public void sendTrackReqToSelectedMember(final Context context, final String userId, final String groupId, final String memberId) {
        final ProgressDialog mProgressDialog2 = ProgressbarUtil.startProgressBar(context);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(context, RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.trackMember(userId, groupId, memberId, new RetroResponse<GroupMemberModel>() {
            @Override
            public void onSuccess() {
                ProgressbarUtil.stopProgressBar(mProgressDialog2);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog2);
                if ((errorMsg == null) || (errorMsg.trim().isEmpty())) {
                    errorMsg = "Server Error";
                }
            }
        });
    }
}