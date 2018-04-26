package com.nglinx.pulse.activity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.UserLoginModel;
import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.session.SharedPrefUtility;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

public class AuthenticateIntentService extends IntentService {

    private final String TAG = this.getClass().getCanonicalName();

    public AuthenticateIntentService() {
        super("AuthenticateIntentService");
        Log.v(TAG, "Constructor.");
    }

    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        final UserLoginModel userLoginModel = ApplicationUtils.getUserLoginModelForAuthenticate();

        receiver.send(ApplicationConstants.STATUS_RUNNING, Bundle.EMPTY);

        final Bundle bundle = new Bundle();

        DataSession.getInstance().clearSessionDetails();

        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.authenticate(userLoginModel, new RetroResponse<UserModel>(getApplicationContext()) {
            @Override
            public void onSuccess() {
                if (DataSession.getInstance().saveSessionDetails(response.getHeaders())) {
                    DataSession.getInstance().setUserModel(model);
                    SharedPrefUtility.saveUserLoggedInDetails(getApplicationContext(), userLoginModel.getUsername(), userLoginModel.getPassword());
                    DataSession.getInstance().setjSessionId(ApplicationUtils.getJSessionId(response.getHeaders()));
                    DataSession.getInstance().setRememberMeId(ApplicationUtils.getRememberMe(response.getHeaders()));
                    receiver.send(ApplicationConstants.STATUS_FINISHED, bundle);
                } else {
                    bundle.putString(ApplicationConstants.ERROR_MSG, errorMsg);
                    receiver.send(ApplicationConstants.STATUS_ERROR, Bundle.EMPTY);
                }
            }

            @Override
            public void onFailure() {
                bundle.putString(ApplicationConstants.ERROR_MSG, errorMsg);
                receiver.send(ApplicationConstants.STATUS_ERROR, bundle);
            }
        });
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }
}