package com.nglinx.pulse.session;

import android.content.Context;
import android.location.Location;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.android.gms.maps.model.LatLng;
import com.nglinx.pulse.models.AddressModel;
import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.models.DeviceModel;
import com.nglinx.pulse.models.DeviceType;
import com.nglinx.pulse.models.DeviceTypesModel;
import com.nglinx.pulse.models.FenceModel;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.models.InviteModel;
import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit.client.Header;

public class DataSession {

    private static DataSession myObj;

    private boolean isHomePageOn;

    public boolean isHomePageOn() {
        return isHomePageOn;
    }

    public void setHomePageOn(boolean homePageOn) {
        isHomePageOn = homePageOn;
    }

    private List<InviteModel> pendingInvites;
    private List<InviteModel> trackingMeInvites;

    private List<DeviceModel> devicesList;

    private List<ChildUserModel> childProfilesList;

    List<FenceModel> fenceList = null;

    private AddressModel selectedAddress;
    ArrayList<DeviceTypesModel> deviceTypesList;
    Map<DeviceType, Integer> availableDevices;

    public static DataSession getInstance() {

        if (myObj == null) {
            myObj = new DataSession();

        }
        return myObj;
    }

    private DataSession() {
        selected_group_id = "";
        selected_group_member_id = "";
        zoom = 16;
        sharing_group_position_open_status = new boolean[2];
        sharing_group_position_open_status[0] = false;
        sharing_group_position_open_status[1] = false;
        isGPSEnabled = false;
        isNetworkEnabled = false;
        canGetLocation = false;
        appKey = FirebaseInstanceId.getInstance().getToken();
    }

    private String username;

    private String password;

    private UserModel userModel;

    private TimeZone localTimeZone;

    public View getSelected_member_view() {
        return selected_member_view;
    }

    public void setSelected_member_view(View selected_member_view) {
        this.selected_member_view = selected_member_view;
    }

    private String selected_group_id;

    private String selected_group_name;

    private String selected_group_member_id;
    private View selected_member_view;

    private float zoom;

    //private GroupAdapter groupAdapter;

    private String selected_group_member_name;

    private boolean[] sharing_group_position_open_status;

    private String jSessionId;

    private String rememberMeId;

    public static String version;

    public static String location;

    public String gcmId;

    public String appKey;

    private Context context;

    private boolean showMyLocation;

    public boolean isGPSEnabled() {
        return isGPSEnabled;
    }

    public void setGPSEnabled(boolean GPSEnabled) {
        isGPSEnabled = GPSEnabled;
    }

    public boolean isNetworkEnabled() {
        return isNetworkEnabled;
    }

    public void setNetworkEnabled(boolean networkEnabled) {
        isNetworkEnabled = networkEnabled;
    }

    public boolean isCanGetLocation() {
        return canGetLocation;
    }

    public void setCanGetLocation(boolean canGetLocation) {
        this.canGetLocation = canGetLocation;
    }

    private boolean isGPSEnabled;

    private boolean isNetworkEnabled;

    private boolean canGetLocation;

    private LatLng mylatLng;

    public void clearLoginRelatedInfo(Context context) {
        getInstance().clearSessionDetails();
        getInstance().clearSelectedGroupMember();
        getInstance().clearLoginDetails();
    }

    public void setLoginRelatedInfoToDataSession(Context context) {
        DataSession.getInstance().setUsername(SharedPrefUtility.getUserName(context));
        DataSession.getInstance().setPassword(SharedPrefUtility.getPassword(context));

        String selectedGroupId = SharedPrefUtility.getSelectedGroupId(context);
        String selectedGroupName = SharedPrefUtility.getSelectedGroupName(context);

        if (!selectedGroupId.equalsIgnoreCase("") && !selectedGroupName.equalsIgnoreCase("")) {
            DataSession.getInstance().setSelectedGroup(selectedGroupId, selectedGroupName);
        }
    }


    public boolean saveSessionDetails(List<Header> headers) {
        String jSessionId = RetroUtils.getJSessionId(headers);
        String rememberMeId = RetroUtils.getRememberMe(headers);

        if ((jSessionId == null) || (rememberMeId == null) || (jSessionId.trim().length() == 0) || (rememberMeId.trim().length() == 0)) {
            return false;
        } else {
            DataSession.getInstance().setjSessionId(jSessionId);
            DataSession.getInstance().setRememberMeId(rememberMeId);
            return true;
        }
    }

    public String getSelectedDeviceId() {
        return selectedDeviceId;
    }

    public void setSelectedDeviceId(String selectedDeviceId) {
        this.selectedDeviceId = selectedDeviceId;
    }

    private String selectedDeviceId;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelected_group_name() {
        return selected_group_name;
    }

    public void setSelected_group_name(String selected_group_name) {
        this.selected_group_name = selected_group_name;
    }

    public String getSelected_group_member_name() {
        return selected_group_member_name;
    }

    public void setSelected_group_member_name(String selected_group_member_name) {
        this.selected_group_member_name = selected_group_member_name;
    }

    public boolean isSharingGroupOpened(int groupPosition) {
        return sharing_group_position_open_status[groupPosition];
    }

    public void setSharingGroupOpenStatus(int groupPosition, boolean openStatus) {
        this.sharing_group_position_open_status[groupPosition] = openStatus;
    }

    public boolean[] getSharingGroupPositions() {
        return sharing_group_position_open_status;
    }


    /*public GroupAdapter getGroupAdapter() {
        return groupAdapter;
    }

    public void setGroupAdapter(Context context, ArrayList<GroupModel> groupsList) {
        this.groupAdapter = new GroupAdapter(context, groupsList);
    }*/

    public String getSelected_group_id() {
        return selected_group_id;
    }

    public void setSelected_group_id(String selected_group_id) {
        this.selected_group_id = selected_group_id;
    }

    public String getSelected_group_member_id() {
        return selected_group_member_id;
    }

    public void setSelected_group_member_id(String selected_group_member_id) {
        this.selected_group_member_id = selected_group_member_id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getZoom() {
        return zoom;
    }

    public void setSelectedGroup(String selected_group_id, String selected_group_name) {
        this.selected_group_id = selected_group_id;
        this.selected_group_name = selected_group_name;
    }

    public void setSelectedGroupMember(String selected_group_member_id, String selected_group_member_name) {
        this.selected_group_member_id = selected_group_member_id;
        this.selected_group_member_name = selected_group_member_name;
    }

    public void clearSelectedGroupMember() {
        this.selected_group_member_id = "";
        this.selected_group_member_name = "";
    }

    public void clearSelectedGroup() {
        this.selected_group_id = "";
        this.selected_group_name = "";
    }

    public GroupMemberModel getMemberDetails(final String groupId, final String memberId) {
        for (GroupModel group :
                getUserModel().getGroups()) {
            if (group.getId().equals(groupId)) {
                for (GroupMemberModel member :
                        group.getMembers()) {
                    if (member.getId().equals(memberId)) {
                        return member;
                    }
                }
            }
        }
        return null;
    }

    public void setLocalTimeZone(TimeZone localTimeZone) {
        this.localTimeZone = localTimeZone;
    }


    public String getjSessionId() {
        return jSessionId;
    }

    public void setjSessionId(String jSessionId) {
        this.jSessionId = jSessionId;
    }

    public void clearSessionDetails() {
        this.jSessionId = null;
        this.rememberMeId = null;
    }

    public void clearLoginDetails() {
        this.username = null;
        this.password = null;
    }

    public String getRememberMeId() {
        return rememberMeId;
    }

    public void setRememberMeId(String rememberMeId) {
        this.rememberMeId = rememberMeId;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void clearDataSession(final Context context) {
        this.myObj = null;
//        SharedPrefUtility.clearprofile(context);
    }

    public boolean isShowMyLocation() {
        return showMyLocation;
    }

    public void setShowMyLocation(boolean showMyLocation) {
        this.showMyLocation = showMyLocation;
    }

    //Updates the User latest location details.
    public void setLocation(final Location location) {
        userModel.getLocation().setLatitude(String.valueOf(location.getLatitude()));
        userModel.getLocation().setLongitude(String.valueOf(location.getLongitude()));

        mylatLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public LatLng getMylatLng() {
        return mylatLng;
    }


    public List<InviteModel> getPendingInvites() {
        return pendingInvites;
    }

    public void setPendingInvites(List<InviteModel> pendingInvites) {
        this.pendingInvites = pendingInvites;
    }

    public List<InviteModel> getTrackingMeInvites() {
        return trackingMeInvites;
    }

    public void setTrackingMeInvites(List<InviteModel> trackingMeInvites) {
        this.trackingMeInvites = trackingMeInvites;
    }

    public ArrayList<DeviceTypesModel> getDeviceTypesList() {
        return deviceTypesList;
    }

    public void setDeviceTypesList(ArrayList<DeviceTypesModel> deviceTypesList) {
        this.deviceTypesList = deviceTypesList;
    }

    public void clearDeviceTypes() {
        this.deviceTypesList = null;
    }

    public AddressModel getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressModel selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public Map<DeviceType, Integer> getAvailableDevices() {
        return availableDevices;
    }

    public void setAvailableDevices(Map<DeviceType, Integer> availableDevices) {
        this.availableDevices = availableDevices;
    }

    public List<DeviceModel> getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(List<DeviceModel> devicesList) {
        this.devicesList = devicesList;
    }

    public List<ChildUserModel> getChildProfilesList() {
        return childProfilesList;
    }

    public void setChildProfilesList(List<ChildUserModel> childProfilesList) {
        this.childProfilesList = childProfilesList;
    }

    public List<ChildUserModel> getUnAttachedChildUser() {
        List<ChildUserModel> unAttachedChilUsers = new ArrayList<>();
        for (ChildUserModel child :
                childProfilesList) {
            if (child.getStatus().equals(0))
                unAttachedChilUsers.add(child);
        }
        return unAttachedChilUsers;
    }

    public List<FenceModel> getFenceList() {
        return fenceList;
    }

    public void setFenceList(List<FenceModel> fenceList) {
        this.fenceList = fenceList;
    }

    public FenceModel getFenceById(final String fenceId) {
        if (fenceList == null)
            return null;

        for (FenceModel fence :
                fenceList) {
            if (fence.getId().equalsIgnoreCase(fenceId))
                return fence;
        }

        return null;
    }
}
