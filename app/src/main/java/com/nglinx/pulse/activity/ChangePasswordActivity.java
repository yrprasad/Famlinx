package com.nglinx.pulse.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.constants.ErrorMessages;
import com.nglinx.pulse.models.UserLoginModel;
import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

//TODO: Name in NAV is not getting Set.
//TODO: Update Password is not working because of the backend bug
public class ChangePasswordActivity extends AbstractActivity {

    TextView tv_oldPassword;
    TextView tv_newPassword;
    TextView tv_newPassword_confirm;

    Button btn_changePassword_confirm;
    Button btn_changePassword_cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_change_password);

        initializeParent();

        initializeIcons();
    }

    protected void initializeIcons() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        tv_oldPassword = (TextView) findViewById(R.id.tv_oldPassword);
        tv_newPassword = (TextView) findViewById(R.id.tv_newPassword);
        tv_newPassword_confirm = (TextView) findViewById(R.id.tv_newPassword_confirm);

        btn_changePassword_confirm = (Button) findViewById(R.id.btn_changePassword_confirm);
    }

    public void onChangePasswordConfirmClick(View v) {

        if ((tv_oldPassword.getText() == null) || (tv_oldPassword.getText().toString().isEmpty())) {
            DialogUtils.diaplayErrorDialog(ChangePasswordActivity.this, "All fields are mandatory");
        } else if ((tv_newPassword.getText() == null) || (tv_newPassword.getText().toString().isEmpty())) {
            DialogUtils.diaplayErrorDialog(ChangePasswordActivity.this, "All fields are mandatory");
        } else if ((tv_newPassword_confirm.getText() == null) || (tv_newPassword_confirm.getText().toString().isEmpty())) {
            DialogUtils.diaplayErrorDialog(ChangePasswordActivity.this, "All fields are mandatory");
        } else if (!ds.getPassword().equals(tv_oldPassword.getText().toString())) {
            DialogUtils.diaplayErrorDialog(ChangePasswordActivity.this, "Old Password is Invalid");
        } else if (!tv_newPassword.getText().toString().equals(tv_newPassword_confirm.getText().toString())) {
            DialogUtils.diaplayErrorDialog(ChangePasswordActivity.this, "New Passwords does not match");
        } else {
            UserLoginModel userLoginModel = new UserLoginModel();
            userLoginModel.setUsername(ds.getUsername());
            userLoginModel.setPassword(tv_newPassword.getText().toString());
            userLoginModel.setToken(ds.getUserModel().getToken());

            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
            apiEndpointInterface.updatePassword(userLoginModel, new RetroResponse<UserModel>() {
                @Override
                public void onSuccess() {
                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                    ds.setUserModel(model);
                    ds.setPassword(tv_newPassword.getText().toString());
                    DialogUtils.diaplayInfoDialog(ChangePasswordActivity.this, ErrorMessages.CHANGE_PASSWORD_SUCCESS);
                }

                @Override
                public void onFailure() {
                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                    DialogUtils.diaplayFailureDialog(ChangePasswordActivity.this, ErrorMessages.CHANGE_PASSWORD_FAILURE + errorMsg);
                }
            });
        }

    }
}