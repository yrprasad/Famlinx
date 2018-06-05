package com.nglinx.pulse.service;

import com.nglinx.pulse.models.GroupMemberModel;
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

    private void sendTrackRequest() {
        if (ds.isHomePageOn()) {
            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(DataSession.getInstance().getContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
            apiEndpointInterface.trackMember(ds.getUserModel().getId(), ds.getSelected_group_id(), ds.getSelected_group_member_id(), new RetroResponse<GroupMemberModel>() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure() {
                    if ((errorMsg == null) || (errorMsg.trim().isEmpty())) {
                        errorMsg = "Server Error";
                    }
                }
            });
        }
    }

    @Override
    public void run() {

        sendTrackRequest();

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        System.out.println("Location Fetched" + currentDateTimeString);

        boolean skipLocationUpdate = false;
        //First time sometimes location can be null
        String latitude = null;
        String longitude = null;
        try {
            latitude = ds.getUserModel().getLocation().getLatitude();
            longitude = ds.getUserModel().getLocation().getLongitude();
        } catch (Exception e) {
            skipLocationUpdate = true;
        }

        if ((latitude == null) || (longitude == null))
            skipLocationUpdate = true;

        //Sometimes google is giving 0.0 and 0.0 as coordinates.
        //Skip the updation of the coordinates in that case.
        if (latitude.startsWith("0.0"))
            skipLocationUpdate = true;

        if (!skipLocationUpdate) {
            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(DataSession.getInstance().getContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
            apiEndpointInterface.getGroups(ds.getUserModel().getId(), latitude, longitude, new RetroResponse<GroupModel>() {
                @Override
                public void onSuccess() {
                    System.out.println("Successfully updated the location details to server");
                    List<GroupModel> groupsList = new ArrayList<GroupModel>(models);
                    ds.getUserModel().setGroups(groupsList);
                }

                @Override
                public void onFailure() {
                    System.out.println("Location update Failed. Msg : " + errorMsg);
                }
            });
        } else {
            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(DataSession.getInstance().getContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
            apiEndpointInterface.getGroups(ds.getUserModel().getId(), new RetroResponse<GroupModel>() {
                @Override
                public void onSuccess() {
                    List<GroupModel> groupsList = new ArrayList<GroupModel>(models);
                    ds.getUserModel().setGroups(groupsList);
                }

                @Override
                public void onFailure() {
                    System.out.println("Failed to get the groups. Msg : " + errorMsg);
                }
            });
        }
    }
}