package com.nglinx.pulse.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nglinx.pulse.R;
import com.nglinx.pulse.constants.ErrorMessages;
import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

public class ForgotPasswordActivity extends AbstractActivity {

    EditText et_username;
    Button btn_reset_password, cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeIcons();
    }

    protected void initializeIcons() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        et_username = (EditText) findViewById(R.id.et_username);
        btn_reset_password = (Button) findViewById(R.id.btn_reset_password);
        cancel = (Button) findViewById(R.id.cancel);
    }

    public void onCancelClick(View v) {
        finish();
    }

    public void onResetPasswordClick(View v) {

        if ((et_username.getText() == null) || (et_username.getText().toString().isEmpty())) {
            DialogUtils.diaplayErrorDialog(ForgotPasswordActivity.this, "Please Enter Username");
        } else {
            final UserModel userLoginModel = new UserModel();

            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
            apiEndpointInterface.forgotPassword(et_username.getText().toString().trim(), userLoginModel, new RetroResponse<UserModel>() {
                @Override
                public void onSuccess() {
                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                    new AlertDialog.Builder(ForgotPasswordActivity.this)
                            .setTitle("Info")
                            .setMessage(ErrorMessages.FORGOT_PASSWORD_INFO)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int whichButton) {
                                    finish();
                                }
                            }).show();
                }

                @Override
                public void onFailure() {
                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                    DialogUtils.diaplayFailureDialog(ForgotPasswordActivity.this, ErrorMessages.FORGOT_PASSWORD_FAILURE + errorMsg);
                }
            });
        }

    }
}