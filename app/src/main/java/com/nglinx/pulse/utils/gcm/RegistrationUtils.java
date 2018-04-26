package com.nglinx.pulse.utils.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nglinx.pulse.R;
import com.nglinx.pulse.activity.HomeActivity;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.session.SharedPrefUtility;

import java.util.Date;
import java.util.HashMap;

/**
 * TODO: Check if this class is required.
 */
public class RegistrationUtils {

    private static final String TAG = "GCMRelated";

    public static String getRegistrationId(Context context) {
        HashMap<String, String> gcmReg = SharedPrefUtility.getGcmData(context);
        String registrationId = gcmReg.get(ApplicationConstants.GCM_REGISTRATION_ID);
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        String registeredVersion = gcmReg.get(ApplicationConstants.GCM_APP_VERSION);
        String currentVersion = getAppVersion(context);
        if (!registeredVersion.equals(currentVersion)) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return "" + packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}