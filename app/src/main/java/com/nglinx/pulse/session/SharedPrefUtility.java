package com.nglinx.pulse.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.nglinx.pulse.constants.ApplicationConstants;

import java.util.HashMap;

public class SharedPrefUtility {

    public static SharedPreferences getSharedPreferncesForRead(Context context) {
        return context.getSharedPreferences(ApplicationConstants.PROFILE, context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getSharedPreferencesForEdit(Context context) {
        SharedPreferences pref = context.getSharedPreferences(ApplicationConstants.PROFILE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        return editor;
    }

    public static boolean getBoolValue(Context context, String paramName) {
        return getSharedPreferncesForRead(context).getBoolean(paramName, false);
    }

    public static String getStringValue(Context context, String paramName, String returnValueIfNotPresent) {
        return getSharedPreferncesForRead(context).getString(paramName, returnValueIfNotPresent);
    }

    public static void updateBoolValue(Context context, String paramName, boolean value) {
        getSharedPreferencesForEdit(context).putBoolean(paramName, value).commit();
    }

    public static void updateStringValue(Context context, String paramName, String value) {
        getSharedPreferencesForEdit(context).putString(paramName, value).commit();
    }

    public static void OneTimeLoggedIn(Context context) {
        updateBoolValue(context, ApplicationConstants.LOG_IN, true);
    }

    public static boolean isOneTimeLoggedIn(Context context) {
        return getBoolValue(context, ApplicationConstants.LOG_IN);
    }

    public static void clearprofile(Context context) {
        SharedPreferences.Editor editor = getSharedPreferencesForEdit(context);
        editor.clear();
        editor.commit();
    }

    public static void clearValue(Context context, String paramName) {
        SharedPreferences.Editor editor = getSharedPreferencesForEdit(context);
        editor.remove(paramName);
        editor.commit();
    }

    public static void saveGcmId(Context context, String registrationId, String appVersion) {
        updateStringValue(context, ApplicationConstants.GCM_REGISTRATION_ID, registrationId);
        updateStringValue(context, ApplicationConstants.GCM_APP_VERSION, appVersion);
    }

    public static HashMap<String, String> getGcmData(Context context) {
        HashMap<String, String> gcmReg = new HashMap<String, String>();
        gcmReg.put(ApplicationConstants.GCM_REGISTRATION_ID, getStringValue(context, ApplicationConstants.GCM_REGISTRATION_ID, ""));
        gcmReg.put(ApplicationConstants.GCM_APP_VERSION, getStringValue(context, ApplicationConstants.GCM_APP_VERSION, ""));
        return gcmReg;
    }


    public static void saveUserName(Context context, String username) {
        updateStringValue(context, ApplicationConstants.USERNAME, username);
    }

    public static String getUserName(Context context) {
        return getStringValue(context, ApplicationConstants.USERNAME, "");
    }

    public static void savePassword(Context context, String password) {
        updateStringValue(context, ApplicationConstants.PASSWORD, password);
    }

    public static String getPassword(Context context) {
        return getStringValue(context, ApplicationConstants.PASSWORD, "");
    }

    public static void saveSelectedGroupId(Context context, String selectedGroupId) {
        updateStringValue(context, ApplicationConstants.SELECTED_GROUP_ID, selectedGroupId);
    }

    public static void saveSelectedGroupName(Context context, String selectedGroupName) {
        updateStringValue(context, ApplicationConstants.SELECTED_GROUP_NAME, selectedGroupName);
    }

    public static String getSelectedGroupId(Context context) {
        return getStringValue(context, ApplicationConstants.SELECTED_GROUP_ID, "");
    }

    public static String getSelectedGroupName(Context context) {
        return getStringValue(context, ApplicationConstants.SELECTED_GROUP_NAME, "");
    }

    public static void saveSelectedGroupMemberId(Context context, String selectedGroupId) {
        updateStringValue(context, ApplicationConstants.SELECTED_GROUP_MEMBER_ID, selectedGroupId);
    }

    public static String getSelectedGroupMemberId(Context context) {
        return getStringValue(context, ApplicationConstants.SELECTED_GROUP_MEMBER_ID, "");
    }

    public static void saveSelectedGroupMember(Context context, String selectedGroupMemberId, String selectedGroupMemberName) {
        updateStringValue(context, ApplicationConstants.SELECTED_GROUP_MEMBER_ID, selectedGroupMemberId);
        updateStringValue(context, ApplicationConstants.SELECTED_GROUP_MEMBER_NAME, selectedGroupMemberName);
    }

    public static void clearSelectedGroupMember(Context context) {
        clearValue(context, ApplicationConstants.SELECTED_GROUP_MEMBER_ID);
        clearValue(context, ApplicationConstants.SELECTED_GROUP_MEMBER_NAME);
    }

    public static void saveSelectedGroup(Context context, String selectedGroupId, String selectedGroupName) {
        updateStringValue(context, ApplicationConstants.SELECTED_GROUP_ID, selectedGroupId);
        updateStringValue(context, ApplicationConstants.SELECTED_GROUP_NAME, selectedGroupName);
    }

    public static void clearSelectedGroup(Context context) {
        clearValue(context, ApplicationConstants.SELECTED_GROUP_ID);
        clearValue(context, ApplicationConstants.SELECTED_GROUP_NAME);
    }

    public static void saveUserLoggedInDetails(Context context, String username, String password) {
        updateStringValue(context, ApplicationConstants.USERNAME, username);
        updateStringValue(context, ApplicationConstants.PASSWORD, password);
        updateBoolValue(context, ApplicationConstants.LOG_IN, true);
    }
}