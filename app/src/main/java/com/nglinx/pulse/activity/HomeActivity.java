package com.nglinx.pulse.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.GroupAdapter;
import com.nglinx.pulse.adapter.GroupMemberAdapter;
import com.nglinx.pulse.adapter.NotificationsAdapter;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.models.NotificationModel;
import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.service.BackendService;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.session.SharedPrefUtility;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.MapUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;
import com.nglinx.pulse.utils.view.HorizontalView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AbstractActivity implements LocationListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnMyLocationButtonClickListener {

    public DrawerLayout drawerLayout;

    //Pager
//    private ViewPager mPager;
    private int currentPage = 0;
    //    private static final Integer[] XMEN= {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};

//    private final Integer[] XMEN= {R.drawable.cradle2};
//    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    //Group Details
    public ArrayList<GroupModel> groupsList;
    private GroupAdapter groupAdapter;
    private NavigationView nav_groups;
    ListView lv_groups;
    GroupClickListener groupClickListener;

    //Group Member Details
    public ArrayList<GroupMemberModel> groupsMembersList;
    private GroupMemberAdapter groupMemberAdapter;
    HorizontalView sv_group_members;
    GroupMemberClickListener groupMemberClickListener;

    //Frames on the screen
    private RelativeLayout lt_adds;
    private RelativeLayout lt_divider;
    private RelativeLayout lt_members_detail;
    private RelativeLayout lt_views;
    private RelativeLayout lt_members;
    //    private RelativeLayout lt_search_bar;
    private WebView webView;
    SupportMapFragment fm;

    //Right Group NavigationView Icons
    TextView tv_username_grouptabs;
    Button bt_addGroup;
    CreateGroupListener createGroupListener;
//    View headerLayout_groups;

    boolean canGetLocation = false;

    //Left Menu NavigationView
    TextView tv_username;

    //Google Map related
    GoogleMap googleMap;
    private MarkerOptions markerOptions;
    private LocationManager locationManager;
    private Location location;
    Fragment mapFragment;
    MapUtils mapUtils;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    //boolean canGetLocation = false;

    BackendService backendService;

    Timer timer_map_refresh;
    MapRefreshTimerTask mapRefreshTimerTask;

    List<NotificationModel> notificationList;
    NotificationsAdapter adapter;
    private ListView notifications_lv;
    private SwipeRefreshLayout swipeRefreshLayout_notifications;

    TextView tv_toolbar_groupname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        ds = DataSession.getInstance();
        mapUtils = new MapUtils();

        //Intialize all the Parent Abstract activities
        initializeParent();

        //Initialize all the icons on this screen and Navigation Menu screens
        initializeIcons();

        //Activate icons for the default look on screen
        activateDefaultVisibleItems();

        //Initlize all the group's related activities
        initializeGroups();

        //Initlize all the group members related things
        initializeGroupMembers();

        //Initialize the notifications
        initializeNotification();

        //Call to get the current location.
        getLocation();

        startTimerMapRefreshTask();

        refreshUserDetails();

//        initPager();

        createDefaultGroupIfNotPresent();

        Timer timer = new Timer();
        ds.setContext(getApplicationContext());
        BackendService backendService = new BackendService();
        timer.schedule(backendService, 5000, 10000);
    }


    /*private void initPager() {

        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager1);
        mPager.setAdapter(new ViewPagerAdapter(HomeActivity.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
             }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 10000);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        stopTimerMapRefreshTask();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startTimerMapRefreshTask();
    }

    private void startTimerMapRefreshTask() {
        timer_map_refresh = new Timer();
        mapRefreshTimerTask = new MapRefreshTimerTask();
        timer_map_refresh.schedule(mapRefreshTimerTask, 5000, 10000);
    }

    private void stopTimerMapRefreshTask() {
        if (timer_map_refresh != null) {
            timer_map_refresh.cancel();
            timer_map_refresh.purge();
            timer_map_refresh = null;
        }
    }

    protected void initializeIcons() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        lt_adds = (RelativeLayout) findViewById(R.id.lt_adds);
        lt_divider = (RelativeLayout) findViewById(R.id.lt_divider);
        lt_members_detail = (RelativeLayout) findViewById(R.id.lt_members_detail);
        lt_views = (RelativeLayout) findViewById(R.id.lt_views);
        lt_members = (RelativeLayout) findViewById(R.id.lt_members);
//        lt_search_bar = (RelativeLayout) findViewById(R.id.lt_search_bar);

        webView = (WebView) findViewById(R.id.wv_sensors);
        webView.setVisibility(View.GONE);

        lv_groups = (ListView) findViewById(R.id.lv_groups);
        lv_groups.setFastScrollEnabled(true);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.nav_header_group, lv_groups, false);
        lv_groups.addHeaderView(header);

        tv_username_grouptabs = (TextView) header.findViewById(R.id.tv_username_grouptabs);
        bt_addGroup = (Button) header.findViewById(R.id.bt_addGroup);

        sv_group_members = (HorizontalView) findViewById(R.id.sv_group_members);

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        headerLayout = navigationView.getHeaderView(0);
        TextView tv_username = headerLayout.findViewById(R.id.tv_username);
        tv_username.setText(ds.getUserModel().getName());
        TextView tv_email = headerLayout.findViewById(R.id.tv_email);
        tv_email.setText(ds.getUserModel().getEmail());
    }

    private void initializeGroups() {
        //Get all the groups
        groupsList = new ArrayList<GroupModel>();
        groupsList.addAll(ds.getUserModel().getGroups());

        //Initialize the Group Adapter with the groups
        groupAdapter = new GroupAdapter(getApplicationContext(), groupsList);

        //Associate adapter to the List View
        lv_groups.setAdapter(groupAdapter);

        groupClickListener = new GroupClickListener();
        lv_groups.setOnItemClickListener(groupClickListener);

        createGroupListener = new CreateGroupListener();
        bt_addGroup.setOnClickListener(createGroupListener);

        tv_username_grouptabs.setText(ds.getUserModel().getName());

        ImageView img_group_toolbar = (ImageView) inc_toolbar.findViewById(R.id.groups_toolbar);
        img_group_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.END);
            }
        });

        tv_toolbar_groupname = (TextView) inc_toolbar.findViewById(R.id.tv_toolbar_groupname);
    }

    private void initializeNotification() {
        notificationList = new ArrayList<NotificationModel>();
        adapter = new NotificationsAdapter(HomeActivity.this, (ArrayList<NotificationModel>) notificationList);

        notifications_lv = (ListView) findViewById(R.id.lv_notifications_home);

        notificationList = new ArrayList<NotificationModel>();
        adapter = new NotificationsAdapter(HomeActivity.this, (ArrayList<NotificationModel>) notificationList);

        notifications_lv.setAdapter(adapter);
        getNotifications(null);
        notifications_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onNotificationsClick(view);
            }
        });

        swipeRefreshLayout_notifications = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshNotification);
        swipeRefreshLayout_notifications.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotifications(swipeRefreshLayout_notifications);
            }
        });
    }

    private void initializeGroupMembers() {

        //Initialize the group members array with empty list.
        groupsMembersList = new ArrayList<GroupMemberModel>();

        //Initialize the Group Member Adapter
        groupMemberAdapter = new GroupMemberAdapter(getApplicationContext(), groupsMembersList);

        //Associate adapter to the List View
        sv_group_members.setAdapter(groupMemberAdapter);

        groupMemberClickListener = new GroupMemberClickListener();
        sv_group_members.setOnItemClickListener(groupMemberClickListener);
        sv_group_members.setOnItemLongClickListener(groupMemberClickListener);
    }

    //Activates the default login icons
    private void activateDefaultVisibleItems() {
        lt_adds.setVisibility(View.VISIBLE);
        lt_divider.setVisibility(View.VISIBLE);
        lt_members_detail.setVisibility(View.VISIBLE);
        lt_views.setVisibility(View.VISIBLE);
        lt_members.setVisibility(View.VISIBLE);
//        lt_search_bar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        fm.getView().setVisibility(View.GONE);
    }

    //Activates the tracking member related icons
    private void activateItemsOnTrackMemberSelect() {
        lt_adds.setVisibility(View.GONE);
        lt_divider.setVisibility(View.GONE);
        lt_members_detail.setVisibility(View.GONE);
        lt_views.setVisibility(View.GONE);
        lt_members.setVisibility(View.VISIBLE);
//        lt_search_bar.setVisibility(View.VISIBLE);
        fm.getView().setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
    }

    //Activates the Sensors related icons
    private void activateItemsOnSensorSelect() {
        lt_adds.setVisibility(View.GONE);
        lt_divider.setVisibility(View.GONE);
        lt_members_detail.setVisibility(View.GONE);
        lt_views.setVisibility(View.GONE);
        lt_members.setVisibility(View.GONE);
//        lt_search_bar.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        fm.getView().setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        ds.clearSelectedGroupMember();
        return true;
    }

    class GroupClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            GroupModel selectedGroup = groupAdapter.getItem(position - 1);
            tv_toolbar_groupname.setText(selectedGroup.getName());

            activateItemsOnTrackMemberSelect();
            drawerLayout.closeDrawers();

            //Update the selected group details
            ds.setSelectedGroup(selectedGroup.getId(), selectedGroup.getName());
            ds.clearSelectedGroupMember();
            SharedPrefUtility.saveSelectedGroup(getApplicationContext(), selectedGroup.getId(), selectedGroup.getName());
            SharedPrefUtility.clearSelectedGroupMember(getApplicationContext());

            //Display the selected group's members
            displaySelectedGroupMembers(selectedGroup);
//            DropdownBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0);
        }
    }

    private void displaySelectedGroupMembers(final GroupModel selectedGroup) {

        final ProgressDialog mProgressDialog2 = ProgressbarUtil.startProgressBar(this);

        groupsMembersList.clear();

        //Add the default Dummy Group
        groupsMembersList.add(ApplicationUtils.getDummyGroupMember());

        //Add all the group members
        groupsMembersList.addAll(ApplicationUtils.getGroupMembersByGroupId(ds.getUserModel().getGroups(), selectedGroup.getId()));

        groupMemberAdapter.setArr2(groupsMembersList);
        groupMemberAdapter.notifyDataSetChanged();
        mProgressDialog2.dismiss();
    }


    class GroupMemberClickListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            GroupMemberModel selectedMember = groupsMembersList.get(position);

            if (selectedMember.getId().equals("")) {
                Intent intent = new Intent(HomeActivity.this, AddMemberActivity.class);
                startActivityForResult(intent, ApplicationConstants.ACTIVITY_SUCCESS_CODE);
                finish();
            } else {
                ds.setSelected_group_member_id(selectedMember.getId());

                groupMemberAdapter.notifyDataSetChanged();

                SharedPrefUtility.saveSelectedGroupMemberId(getApplicationContext(), selectedMember.getId());

                //Send track request to the member.
                sendTrackReqToSelectedMember();

                //Update the user tracking location on map
                displaySelectedMember(selectedMember, true);
            }
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            final String group_member_id = groupsMembersList.get(position).getId();
            final String group_member_name = groupsMembersList.get(position).getName();

            ds.setSelectedGroupMember(group_member_id, group_member_name);
            SharedPrefUtility.saveSelectedGroupMember(getApplicationContext(), group_member_id, group_member_name);

            if (!ds.getSelected_group_id().equals("0")) {
                Intent intent = new Intent(getApplicationContext(), MemberSettingsActivity.class);
                startActivityForResult(intent, ApplicationConstants.SETTINGS_PAGE_REQUEST_CODE);
                return true;
            }

            return true;
        }
    }

    private void sendTrackReqToSelectedMember() {
        final ProgressDialog mProgressDialog2 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.trackMember(ds.getUserModel().getId(), ds.getSelected_group_id(), ds.getSelected_group_member_id(), new RetroResponse<GroupMemberModel>() {
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

    private void displaySelectedMember(GroupMemberModel selectedMember, boolean isPopUpRequired) {
        final String selectedGroupId = ds.getSelected_group_id();
        final String selectedMemberGroupId = ds.getSelected_group_member_id();

        GroupMemberModel member = null;
        if ((!selectedGroupId.equals("")) && (!selectedMemberGroupId.equals(""))) {
            member = ds.getMemberDetails(selectedGroupId, selectedMemberGroupId);
        } else if (null != selectedMember) {
            member = selectedMember;
        }

        if (member != null) {
            activateItemsOnTrackMemberSelect();
            if (member.getStatus().equalsIgnoreCase("0")) {
                if (isPopUpRequired)
                    DialogUtils.diaplayDialog(HomeActivity.this, "Invite Request Pending", "User not accepted the invite");
            } else if (member.getTrackingModel() == null) {
                if (isPopUpRequired)
                    DialogUtils.diaplayDialog(HomeActivity.this, "Failed to get User Location", "Failed to get User Location");
            } else if ((member.getTrackingModel().getLatitude() == null) || (member.getTrackingModel().getLongitude() == null)) {
                if (isPopUpRequired)
                    DialogUtils.diaplayDialog(HomeActivity.this, "Failed to get User Location", "User not logged-in");
            } else {
                SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                //Ensure synchronization.
                synchronized (this) {
                    mapUtils.updateMemberLocationOnMap(getApplicationContext(), fm, member);
                }
            }
        }
    }


    //Gets the latest Self location and updates the location in DataSession and map if no user is selected
    @Override
    public void onLocationChanged(Location location) {

        System.out.println("Location Change listener called onLocationChanged.Time  " + ApplicationUtils.getCurrentTime());

        //Update the Self location in DataSession.
        ds.setLocation(location);

        //If no group member is selected, update the location in Map
        //TODO: Check if the map is active in screen.
        if ((ds.getSelected_group_id().equals("")) || (ds.getSelected_group_member_id().equals("")) || (ds.getSelected_group_member_id().equals(ApplicationConstants.DEFAULT_GROUP_ID))) {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapUtils.updateMyLocationOnMap(getApplicationContext(), fm, ds.getMylatLng());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getLocation();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*//Get the location and request for location updates in regular intervals
    public Location getLocation() {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            } else {

                locationManager = (LocationManager) getApplicationContext()
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isGPSEnabled || isNetworkEnabled) {
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                1000,
                                0, this);
                    } else if (isGPSEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                1000,
                                0, this);
                    }

                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            ds.setLocation(location);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }*/

    public Location getLocation() {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            } else {

                locationManager = (LocationManager) getApplicationContext()
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                0,
                                0, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                ds.setLocation(location);
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    0,
                                    0, this);

                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    ds.setLocation(location);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onCameraChange(CameraPosition pos) {
        ds.setZoom(pos.zoom);
    }

    //Refresh the map in regular intervals
    public class MapRefreshTimerTask extends TimerTask {

        @Override
        public void run() {
            //If a member is selected and map is active,
            //then refresh the map
            if ((ds.getSelected_group_id().equals("")) || (ds.getSelected_group_member_id().equals("")) || (ds.getSelected_group_member_id().equals(ApplicationConstants.DEFAULT_GROUP_ID))) {
                //No User is selected. Ignore
                return;
            }

            //TODO: Check if map is active in screen.

            //Update the selected member location on map.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displaySelectedMember(null, false);
                }
            });
        }
    }

    class CreateGroupListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), CreateGroupActivty.class);
            startActivity(intent);
        }
    }

    private void refreshUserDetails() {

        final ProgressDialog mProgressDialog = ProgressbarUtil.startProgressBar(this);

        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getUserDetailsIncludingGroups(ds.getUserModel().getId(), new RetroResponse<UserModel>() {
            @Override
            public void onSuccess() {
                ProgressbarUtil.stopProgressBar(mProgressDialog);
                UserModel userModel = (UserModel) model;
                ds.setUserModel(userModel);
                groupsList.clear();
                groupsList.addAll(ds.getUserModel().getGroups());
                groupAdapter.notifyDataSetChanged();
                groupMemberAdapter.notifyDataSetChanged();
                if ((ds.getSelected_group_id() != null) && (!ds.getSelected_group_id().equalsIgnoreCase(""))) {
                    int pos = getGroupPositionInList(groupsList, ds.getSelected_group_id());
                    lv_groups.performItemClick(groupAdapter.getView(pos, null, null), pos + 1, pos);
                } else {
                    listAllGroupMembers();
                }
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog);
                DialogUtils.diaplayErrorDialog(HomeActivity.this, errorMsg);
            }
        });
    }

    private void listAllGroupMembers() {

        List<GroupMemberModel> allMembers = new ArrayList<>();

        //For each group
        for (GroupModel groupModel :
                ds.getUserModel().getGroups()) {
            //For each group Member
            for (GroupMemberModel memberModel :
                    groupModel.getMembers()) {
                if (!allMembers.contains(memberModel)) {
                    allMembers.add(memberModel);
                }
            }
        }

        groupsMembersList.clear();

        //Add all the group members
        groupsMembersList.add(ApplicationUtils.getDummyGroupMember());
        groupsMembersList.addAll(allMembers);
        groupMemberAdapter.setArr2(groupsMembersList);
        groupMemberAdapter.notifyDataSetChanged();
    }

    private int getGroupPositionInList(ArrayList<GroupModel> groups, String groupId) {
        int pos = 0;
        for (int i = 0; i < groups.size(); i++) {
            GroupModel model = groups.get(i);
            if (model.getId().equalsIgnoreCase(groupId)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public void onNotificationsClick(View view) {
        Intent intent9 = new Intent(getApplicationContext(), NotificationActivity.class);
        startActivity(intent9);
        finish();
    }

    public void onFenceClick(View view) {
        Intent intent9 = new Intent(getApplicationContext(), FenceActivity.class);
        startActivity(intent9);
        finish();
    }

    public void onBuyNowClick(View view) {
        Intent intent9 = new Intent(getApplicationContext(), BuyNowActivity.class);
        startActivity(intent9);
        finish();
    }

    public void onDeviceClick(View view) {
        Intent intent9 = new Intent(getApplicationContext(), DeviceActivity.class);
        startActivity(intent9);
        finish();
    }

    private void getNotifications(final SwipeRefreshLayout swipeRefreshLayout_notifications) {
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllNotifications(DataSession.getInstance().getUserModel().getId(), new RetroResponse<NotificationModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    notificationList.clear();
                    notificationList.addAll(models);
                    Collections.sort((List<NotificationModel>) notificationList);
                    adapter.notifyDataSetChanged();
                    if (null != swipeRefreshLayout_notifications)
                        swipeRefreshLayout_notifications.setRefreshing(false);
                }
            }

            @Override
            public void onFailure() {
                DialogUtils.diaplayFailureDialog(HomeActivity.this, errorMsg);
            }
        });
    }

    public void initialzeDefaultHomeView(AdapterView<?> adapterView, View view, int position, long l) {

        GroupModel selectedGroup = groupAdapter.getItem(position - 1);

        activateItemsOnTrackMemberSelect();
        drawerLayout.closeDrawers();

        //Update the selected group details
        ds.setSelectedGroup(selectedGroup.getId(), selectedGroup.getName());
        ds.clearSelectedGroupMember();
        SharedPrefUtility.saveSelectedGroup(getApplicationContext(), selectedGroup.getId(), selectedGroup.getName());
        SharedPrefUtility.clearSelectedGroupMember(getApplicationContext());

        //Display the selected group's members
        displaySelectedGroupMembers(selectedGroup);
//            DropdownBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ds.setHomePageOn(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ds.setHomePageOn(false);

    }

    protected void createDefaultGroupIfNotPresent() {

        DataSession.getInstance().fetchDefaultGroupDetails();

        final DataSession ds = DataSession.getInstance();
        if((ds.getDefaultGroupId() != null) && (!ds.getDefaultGroupId().equalsIgnoreCase("")))
        {
            return;
        }

        final GroupModel groupModel = new GroupModel();
        groupModel.setName(ApplicationConstants.DEFAULT_GROUP_NAME);

        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.createGroup(ds.getUserModel().getId(), groupModel, new RetroResponse<GroupModel>(getApplicationContext()) {
            @Override
            public void onSuccess() {
                ds.setDefaultGroupId(model.getId());
                ds.setDefaultGroup(model);
            }

            @Override
            public void onFailure() {
                System.out.print("Failed to create group");
            }
        });
    }
}