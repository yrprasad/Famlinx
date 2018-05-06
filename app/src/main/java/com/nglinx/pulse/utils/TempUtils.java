package com.nglinx.pulse.utils;

import android.widget.ArrayAdapter;

import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.AddressModel;
import com.nglinx.pulse.models.DeviceModel;
import com.nglinx.pulse.models.DeviceType;
import com.nglinx.pulse.models.DeviceTypesModel;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.models.UserLoginModel;
import com.nglinx.pulse.models.UserTrackingModel;
import com.nglinx.pulse.models.UserType;
import com.nglinx.pulse.session.DataSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import retrofit.client.Header;


/**
 * Created by yelisetr on 1/26/2017.
 */

public class TempUtils {

    public static ArrayList<AddressModel> getUserDummyAddressList() {

        ArrayList<AddressModel> addressModels = new ArrayList<>(3);

        AddressModel address1 = new AddressModel();
        address1.setLandmark("landmark1");
        address1.setLocation("location1");
        address1.setStreet("Street1");

        AddressModel address2 = new AddressModel();
        address2.setLandmark("landmark2");
        address2.setLocation("location2");
        address2.setStreet("Street2");

        AddressModel address3 = new AddressModel();
        address3.setLandmark("landmark3");
        address3.setLocation("location3");
        address3.setStreet("Street3");

        addressModels.add(address1);
        addressModels.add(address2);
        addressModels.add(address3);

        return addressModels;
    }
}