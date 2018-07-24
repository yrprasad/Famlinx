/*
* Copyright (c) 2018 NGLinx Private Limited. All rights reserved.
*
* The copyright to the computer software herein is the property of
* Openwave Systems Inc. The software may be used and/or copied only
* with the written permission of NGLinx Private Limited. or in accordance
* with the terms and conditions stipulated in the agreement/contract
* under which the software has been supplied.
*
* $Id:$
*/
package com.nglinx.pulse.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.constants.ErrorMessages;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    private boolean FLAG = true;

    private AutoCompleteTextView et_username;
    private TextInputEditText et_password;
    private Button btnLogin;
    TextView bt_forgotpassword;
    TextView bt_signup;

    public String classTag;
    private DataSession ds;

    String regid = "dummy";

    void initializeIcons() {
        et_username = (AutoCompleteTextView) findViewById(R.id.spinner_udid);
        et_password = (TextInputEditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.bt_login);
        bt_forgotpassword = (TextView) findViewById(R.id.bt_forgotpassword);
        bt_signup = (TextView) findViewById(R.id.bt_signup);
    }

    void initializeMembers() {
        ds = DataSession.getInstance().getInstance();
        classTag = LoginActivity.class.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeIcons();
        initializeMembers();
    }

    public void onSignUpClick(View v) {
        Intent registerIntent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(registerIntent);
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    public void OnLoginButtonClick(View V) {

        Log.v(classTag, "SignIn Function Entered.");

        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if ((username == null) || (username.length() == 0) || (password == null) || (password.length() == 0)) {
            DialogUtils.diaplayErrorDialog(LoginActivity.this, ErrorMessages.INVALID_USERNAME_PASSWORF);
        } else {

            //Set the username and passwrd in the Data Session
            ds.setUsername(username);
            ds.setPassword(password);

            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(LoginActivity.this);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, AuthenticateIntentService.class);
            intent.putExtra("receiver", new ResultReceiver(new Handler()) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    switch (resultCode) {
                        case ApplicationConstants.STATUS_FINISHED:
                            ProgressbarUtil.stopProgressBar(mProgressDialog1);
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case ApplicationConstants.STATUS_ERROR:
                            ds.clearLoginRelatedInfo(getApplicationContext());
                            ProgressbarUtil.stopProgressBar(mProgressDialog1);
                            DialogUtils.diaplayErrorDialog(LoginActivity.this, (String) resultData.get(ApplicationConstants.ERROR_MSG));
                    }
                }
            });
            startService(intent);
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        et_username.getText().clear();
        et_password.getText().clear();
    }*/

    public void onForgotPasswordClick(View V) {
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_username.getText().clear();
        et_password.getText().clear();
    }

}