package com.nglinx.pulse.service;

import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class BackendService extends TimerTask {

    DataSession ds = DataSession.getInstance();

    @Override
    public void run() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        System.out.println("Location Fetched" + currentDateTimeString);
        final String latitude = ds.getUserModel().getLocation().getLatitude();
        final String longitude = ds.getUserModel().getLocation().getLongitude();

        System.out.println("Fetched the location. latitude : " + latitude + ". longitude : " + longitude);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(DataSession.getInstance().getContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getGroups(ds.getUserModel().getId(), latitude, longitude, new RetroResponse<GroupModel>() {
            @Override
            public void onSuccess() {
                System.out.println("Successfully updated the location details to server");
                List<GroupModel> groupsList = new ArrayList<GroupModel>(models);
                ds.getUserModel().setGroups(groupsList);
                ds.getUserModel().getLocation().setLatitude(latitude);
                ds.getUserModel().getLocation().setLongitude(longitude);
            }

            @Override
            public void onFailure() {
                System.out.println("Location update Failed. Msg : " + errorMsg);
            }
        });
    }
}