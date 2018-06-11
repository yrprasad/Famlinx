package com.nglinx.pulse.constants;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class ApplicationConstants {

    public static final String GROUP_STATUS_ACTIVE = "ACTIVE";
    public static String DEFAULT_GROUP_NAME = "Home";
    public static String DEFAULT_GROUP_ID = "0";

    public static String SHARING_PENDING_INVITES_HEADER = "Pending Invites";
    public static String SHARING_TRACKING_ME_HEADER = "Tracking Me";

    public static String DEVICES_DEVICE_HEADER = "Devices";
    public static String DEVICES_MEMBER_PROFILES_HEADER = "PROFILES";

    public static int SHARING_PENDING_INVITES_INDEX = 0;
    public static int SHARING_TRACKING_ME_INDEX = 1;

    public static int SHARING_PENDING_INVITES_STATUS = 0;
    public static int SHARING_TRACKING_ME_STATUS = 1;

    public static int SETTINGS_NORMAL_FINISH = 0;
    public static int SETTINGS_DELETE_MEMBER_FINISH = 2;

    public static int SETTINGS_PAGE_REQUEST_CODE = 1;

    public static int ACTIVITY_SUCCESS_CODE = 1;

    public static String JSESSION_ID_HEADER = "JSESSIONID";
    public static String REMEMBER_ME_HEADER = "remember-me";

    public static String RESPONSE_HEADER_SEPARATOR = ";";

    public static String COOKIE_KEY_VALUE_SEPARATOR = "=";

    public static String COOKIE_HEADER = "Set-Cookie";

    public static String SERVER_URL = "http://device.nglinx.com:8500/";

    public static String REST_RESPONSE_SUCCESS = "Success";

    public static String REST_AUTH_ERROR = "authError";

    public static String ERROR_MSG = "errorMsg";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public static int FENCE_UNITS_METRES = 0;
    public static int FENCE_UNITS_KMS = 1;
    public static int FENCE_UNITS_MILES = 2;

    // Set Duration of the Splash Screen
    public static long SPLASH_SCREEN_DELAY = 2000;


    public static String ALERT_DLG_TITLE_ERROR = "ERROR";

    //Shared Preferences Constants
    public static final String LOG_IN = "logged_in";
    public static final String USER = "user";
    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";
    public static final String USER_ID = "userId";
    public static final String SELECTED_GROUP_ID = "selected_group_id";
    public static final String SELECTED_GROUP_NAME = "selected_group_name";
    public static final String SELECTED_GROUP_MEMBER_ID = "selected_group_member_id";
    public static final String SELECTED_GROUP_MEMBER_NAME = "selected_group_member_name";
    public static final String GCM_REGISTRATION_ID = "registration_id";
    public static final String GCM_APP_VERSION = "appVersion";
    public static final String PROFILE = "profile";

    public static final String DEVICE_TYPE_SENSOR= "SENSOR";

    public static final String DEVICE_GRAPH_URL = "http://device.nglinx.com:8500/#/sensors?udid=";

    public static final String SIDE_MENU_HOME= "Home";

    public static final String DUMMY_USER_CLEAR_ID = "1a";

    public static final String BUY_NOW_DESC_BULLET_PREFIX = "         •   ";

    public static final int SHIPPING_COST = 20;

    public static final String FAILED_ITEM_COUNT_STR = "failedItemsCount";
    public static final String TOTAL_ITEM_COUNT_STR = "totalItemsCount";
    public static final String TOTAL_ORDER_COST_STR = "failedOrderTotalCost";

    public static final String SELECTED_FENCE = "SelectedFence";

    public static final String CHILD_PROFILE_SELECTED_USERID = "selectedChildProfile";

    public static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    public static final int GRACE_PERIOD_TO_ACTIVATION = 7;
}
