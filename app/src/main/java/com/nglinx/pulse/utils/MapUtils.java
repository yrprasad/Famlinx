package com.nglinx.pulse.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.nglinx.pulse.activity.HomeActivity;
import com.nglinx.pulse.adapter.CustomInfoViewAdaptor;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.DeviceType;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.models.UserLoginModel;
import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.models.UserTrackingModel;
import com.nglinx.pulse.models.UserType;
import com.nglinx.pulse.session.DataSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit.client.Header;


/**
 * Created by yelisetr on 1/26/2017.
 */

public class MapUtils {
    private LatLng getMemberLocation(final GroupMemberModel member) {
        LatLng latLng;
        double dlatitude = Double.valueOf(member.getTrackingModel().getLatitude());
        double dlongitude = Double.valueOf(member.getTrackingModel().getLongitude());
        latLng = new LatLng(dlatitude, dlongitude);
        return latLng;
    }

    private MarkerOptions getMarkerOption(final LatLng memberLocation, final String memberName) {
        MarkerOptions markerOptions = new MarkerOptions().position(memberLocation).title(memberName);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.device_marker));
        return markerOptions;
    }

    public void updateMemberLocationOnMap(final Context context, final SupportMapFragment fm, final GroupMemberModel member) {

        final LatLng memberLocation = getMemberLocation(member);

        fm.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {

                MarkerOptions markerOptions = getMarkerOption(memberLocation, member.getName());

                final GoogleMap googleMap = map;
                googleMap.clear();

                Marker marker = googleMap.addMarker(markerOptions);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        DataSession.getInstance().switchDisplayInfoWindowEnabled();
                        return true;
                    }
                });

                if (DataSession.getInstance().isShowMyLocation()) {
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        return;
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(marker.getPosition());
                    double dlatitude = Double.valueOf(DataSession.getInstance().getUserModel().getLocation().getLatitude());
                    double dlongitude = Double.valueOf(DataSession.getInstance().getUserModel().getLocation().getLongitude());
                    builder.include(new LatLng(dlatitude, dlongitude));
                    int padding = 0; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 150);
                    googleMap.moveCamera(cu);
                    googleMap.animateCamera(cu);
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(memberLocation, 150));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

                googleMap.setMyLocationEnabled(true);
                googleMap.setInfoWindowAdapter(new CustomInfoViewAdaptor(context, member.getTrackingModel()));
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setIndoorEnabled(false);

                if (DataSession.getInstance().isDisplayInfoWindowEnabled())
                    marker.showInfoWindow();
            }
        });
    }


    public void updateMyLocationOnMap(final Context context, final SupportMapFragment fm, final LatLng myLatLng) {

        //TODO: Need to check the crash
        if(1 == 1)
            return;

        fm.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {

                final GoogleMap googleMap = map;
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(DataSession.getInstance().getZoom()));
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setIndoorEnabled(false);

                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return;

                googleMap.setMyLocationEnabled(true);
            }
        });
    }
}