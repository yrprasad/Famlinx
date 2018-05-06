package com.nglinx.pulse.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.nglinx.pulse.R;
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
import com.nglinx.pulse.session.SharedPrefUtility;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.security.acl.Group;
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

public class ApplicationUtils {

    public static UserLoginModel getUserLoginModelForAuthenticate() {
        final UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setUsername(DataSession.getInstance().getUsername());
        userLoginModel.setPassword(DataSession.getInstance().getPassword());
        userLoginModel.setAppKey(DataSession.getInstance().getAppKey());
        userLoginModel.setType(UserType.android);
        return userLoginModel;
    }

    public static String getJSessionId(List<Header> headers) {

        Header jSessHeader = getHeader(headers, ApplicationConstants.JSESSION_ID_HEADER);

        if (jSessHeader == null) {
            return null;
        }

        return getCookieValue(jSessHeader, ApplicationConstants.JSESSION_ID_HEADER);
    }


    private static Header getHeader(List<Header> headers, String headerStr) {
        Header header = null;

        for (Header tmp : headers) {
            if (tmp.getName().equalsIgnoreCase(ApplicationConstants.COOKIE_HEADER)) {
                if (tmp.getValue().contains(headerStr)) {
                    header = tmp;
                    break;
                }
            }
        }
        return header;
    }

    private static String getCookieValue(Header header, String cookieKey) {
        String[] cookies = header.getValue().split(ApplicationConstants.RESPONSE_HEADER_SEPARATOR);

        if (cookies.length == 0)
            return null;

        for (String cookie :
                cookies) {
            if (cookie.contains(cookieKey)) {
                String[] tmp = cookie.split(ApplicationConstants.COOKIE_KEY_VALUE_SEPARATOR);
                if (tmp.length != 2) {
                    return null;
                }
                return tmp[1];
            }
        }

        return null;
    }


    public static String getRememberMe(List<Header> headers) {

        Header rememberMeHeader = getHeader(headers, ApplicationConstants.REMEMBER_ME_HEADER);

        if (rememberMeHeader == null) {
            return null;
        }

        return getCookieValue(rememberMeHeader, ApplicationConstants.REMEMBER_ME_HEADER);
    }

    public static String constructMemberTitle(UserTrackingModel userTrackingModel) {
        String time = getTimeByLocalTimeZone(userTrackingModel.getModifiedTime());
        String title = "Time : " + time;

        if (userTrackingModel != null) {
            /*if ((userTrackingModel.getAlert_status() != null) && (userTrackingModel.getAlert_status() == 0)) {
                title += "\n" + "Status: Active";
            } else {
                title += "\n" + "Status: InActive";
            }*/

//            if ((userTrackingModel.getBattery() != null) && (userTrackingModel.getUserType() == UserType.device)) {
            if (userTrackingModel.getBattery() != null) {
                title += "\n" + "Battery: " + userTrackingModel.getBattery();
            }

            if (userTrackingModel.getPresence() != null) {
                title += "\n" + "Presence: " + userTrackingModel.getPresence();
            }

            if (userTrackingModel.getAccuracy() != null) {
                title += "\n" + "Accuracy: " + userTrackingModel.getAccuracy() + " mts";
            }

            double distance = getDistance(userTrackingModel.getLatitude(), userTrackingModel.getLongitude(), DataSession.getInstance().getUserModel().getLocation().getLatitude(), DataSession.getInstance().getUserModel().getLocation().getLongitude(), ApplicationConstants.FENCE_UNITS_MILES);
            title += "\n" + "Distance: " + distance + " miles";
        }
        return title;
    }

    private static double getDistance(String lat1, String lon1, String lat2,
                                      String lon2, int unit) {
        return getDistance(Double.parseDouble(lat1), Double.parseDouble(lon1), Double.parseDouble(lat2), Double.parseDouble(lon2), unit);
    }

    private static double getDistance(double lat1, double lon1, double lat2,
                                      double lon2, int unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == ApplicationConstants.FENCE_UNITS_METRES) {
            dist = dist * 1.609344 * 1000;
        } else if (unit == ApplicationConstants.FENCE_UNITS_KMS) {
            dist = dist * 1.609344;
        } else if (unit == ApplicationConstants.FENCE_UNITS_MILES) {
            dist = dist * 0.8684;
        }

        if (dist != 0)
            dist = Math.round(dist * 100.0) / 100.0;
        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static String getTimeByLocalTimeZone(String time) {

        String dateFormat = "EEE MMM dd HH:mm:ss Z yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        formatter.setTimeZone(TimeZone.getDefault());
        formatter.applyPattern(dateFormat);

        Date scheduleTime = null;
        try {
            scheduleTime = formatter.parse(time);
        } catch (Exception e) {
            System.out.println("Failed to convert time");
            return time;
        }
        return scheduleTime.toString();
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }

    public static GroupMemberModel getDummyGroupMember() {
        GroupMemberModel dummyMember = new GroupMemberModel();
        dummyMember.setName("");
        dummyMember.setId("");
        dummyMember.setStatus(ApplicationConstants.GROUP_STATUS_ACTIVE);
        dummyMember.setImage("");
        return dummyMember;
    }

    public static boolean isDummyMember(final GroupMemberModel member) {
        if ((null == member.getId()) || (member.getId().equals("")))
            return true;
        else
            return false;
    }

    public static String getUserFistName(final String name) {
        String first_name;
        String nameTokens[];

        if (name != null) {
            nameTokens = name.split(" ");
        } else {
            nameTokens = name.split(" ");
        }


        if (nameTokens[0].toCharArray().length > 5) {
            first_name = nameTokens[0].substring(0, 5) + "...";
        } else {
            first_name = nameTokens[0];
        }

        return first_name;
    }


    public static boolean isMobileUser(final GroupMemberModel member) {
        if ((member.getTrackingModel() != null) && (member.getTrackingModel().getType() != null) && ((member.getTrackingModel().getType() == DeviceType.ios) || (member.getTrackingModel().getType() == DeviceType.android)))
            return true;
        else
            return false;
    }

    public static List<GroupMemberModel> getGroupMembersByGroupId(final List<GroupModel> groups, final String groupId) {

        List<GroupMemberModel> groupMembers = null;

        for (GroupModel group :
                groups) {
            if (group.getId().equalsIgnoreCase(groupId)) {
                groupMembers = group.getMembers();
                break;
            }
        }

        return groupMembers;
    }

    public static List<DeviceModel> getNonSensorDevices(final HashSet<DeviceModel> models) {

        List<DeviceModel> nonSensorModels = new ArrayList<>();
        for (DeviceModel model :
                models) {
            if (!model.getType().equals(DeviceType.SENSOR))
                nonSensorModels.add(model);
        }
        return nonSensorModels;
    }

    public static List<DeviceTypesModel> getNonSensorDeviceTypes(final HashSet<DeviceTypesModel> models) {

        List<DeviceTypesModel> nonSensorModels = new ArrayList<>();
        for (DeviceTypesModel model :
                models) {
            if (!model.getType().equals(DeviceType.SENSOR))
                nonSensorModels.add(model);
        }
        return nonSensorModels;
    }

    public static HashSet<DeviceTypesModel> updateDefaultValueIfDeviceDescIsNull(final HashSet<DeviceTypesModel> models) {

        for (DeviceTypesModel model :
                models) {
            if (model.getSizes() == null) {
                List<Integer> sizes = new ArrayList<>(3);
                sizes.add(1);
                sizes.add(2);
                sizes.add(3);
                model.setSizes(sizes);
            }

            if (model.getDescription() == null) {
                List<String> description = new ArrayList<>(3);
                description.add(model.getName() + "Description1");
                description.add(model.getName() + "Description2");
                description.add(model.getName() + "Description3");
                model.setDescription(description);
            }

            if (model.getCare() == null) {
                List<String> care = new ArrayList<>(3);
                care.add(model.getName() + "Care1");
                care.add(model.getName() + "Care2");
                care.add(model.getName() + "Care3");
                model.setCare(care);
            }

            if (model.getType().equals(DeviceType.A9)) {
                model.setCost(10);
            } else if (model.getType().equals(DeviceType.V7)) {
                model.setCost(11);
            } else if (model.getType().equals(DeviceType.V16)) {
                model.setCost(12);
            } else if (model.getType().equals(DeviceType.MQTT)) {
                model.setCost(13);
            }
        }
        return models;
    }


    public static int getDeviceCost(DeviceType deviceType) {
        if (deviceType.equals(DeviceType.A9)) {
            return 10;
        } else if (deviceType.equals(DeviceType.V7)) {
            return 11;
        } else if (deviceType.equals(DeviceType.V16)) {
            return 12;
        } else if (deviceType.equals(DeviceType.MQTT)) {
            return 13;
        }
        return 0;
    }

    public static String getAddress(AddressModel addressModel) {
        String address = addressModel.getLocation() + "," + addressModel.getStreet() + "," + addressModel.getLandmark();
        return address;
    }

}