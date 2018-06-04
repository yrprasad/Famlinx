package com.nglinx.pulse;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yelisetr on 4/10/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    DataSession ds = null;

    public MyFirebaseMessagingService() {
        ds = DataSession.getInstance();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if ((ds == null) || (ds.getUserModel() == null) || (null == ds.getUserModel().getLocation())) {
            return;
        }
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
