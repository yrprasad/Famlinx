package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

public class EditProfileActivity extends AppCompatActivity {

    ChildUserModel selectedUser;

    EditText et_ep_username;
    TextView et_ep_email;
    TextView et_associated_with;
    TextView et_member_type;

    Button btn_ep_save, btn_ep_cancel, btn_ep_delete;
    DataSession ds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_member_profile);

        //Initialize all the icons on this screen and Navigation Menu screens
        initializeIcons();

        displayData();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    protected void initializeIcons() {
        et_ep_username = (EditText) findViewById(R.id.et_ep_username);
        et_ep_email = (TextView) findViewById(R.id.et_ep_email);
        et_associated_with = (TextView) findViewById(R.id.et_associated_with);
        et_member_type = (TextView) findViewById(R.id.et_member_type);

        btn_ep_save = (Button) findViewById(R.id.btn_ep_save);
        btn_ep_cancel = (Button) findViewById(R.id.btn_ep_cancel);
        btn_ep_delete = (Button) findViewById(R.id.btn_ep_delete);
        btn_ep_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent7 = new Intent(EditProfileActivity.this, DeviceActivity.class);
                startActivity(intent7);
                finish();
            }
        });

        ds = DataSession.getInstance();
    }

    public void displayData() {

        Bundle mBundle = getIntent().getExtras();
        String selectedUserId = null;
        if (mBundle != null) {
            selectedUserId = mBundle.getString(ApplicationConstants.CHILD_PROFILE_SELECTED_USERID);
        }

        if (null == selectedUserId) {
            DialogUtils.diaplayErrorDialog(EditProfileActivity.this, "Failed to delete the Profile");
            return;
        }

        selectedUser = getSelectedChildProfile(selectedUserId);

        et_ep_username.setText(selectedUser.getUsername());
        et_ep_email.setText(selectedUser.getEmail());

        if (null != selectedUser.getUdid())
            et_associated_with.setText(selectedUser.getUdid());
        else
            et_associated_with.setText("NA");

        if (null != selectedUser.getType())
            et_member_type.setText(selectedUser.getType().toString());
    }

    private ChildUserModel getSelectedChildProfile(final String selectedUserId) {
        for (ChildUserModel childUserModel :
                ds.getChildProfilesList()) {
            if (childUserModel.getId().equalsIgnoreCase(selectedUserId))
                return childUserModel;
        }
        return null;
    }

    public void onSaveChildProfileClickHandler(View v) {

        String newUserName = null;
        if (null != et_ep_username.getText())
            newUserName = et_ep_username.getText().toString();

        if ((null == newUserName) || (newUserName.length() == 0)) {
            DialogUtils.diaplayErrorDialog(EditProfileActivity.this, "Enter the Username");
        } else {
            new AlertDialog.Builder(EditProfileActivity.this)
                    .setTitle("Edit Profile")
                    .setMessage("Do you want to change the profile name to " + et_ep_username.getText().toString() + " ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            selectedUser.setName(et_ep_username.getText().toString());
                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(EditProfileActivity.this);
                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.updateChildUser(DataSession.getInstance().getUserModel().getId(), selectedUser.getId(), selectedUser, new RetroResponse<ChildUserModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            EditProfileActivity.this);
                                    builder.setTitle("Profile updated successfully");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    builder.show();

                                    Intent intent7 = new Intent(EditProfileActivity.this, DeviceActivity.class);
                                    startActivity(intent7);
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(EditProfileActivity.this, "Failed to update the Profile", errorMsg);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }


    public void onDeleteChildProfileClickHandler(View v) {

        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle("Delete Profile")
                .setMessage("Do you want to delete the profile " + et_ep_username.getText() + " ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(EditProfileActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.deleteChildUser(DataSession.getInstance().getUserModel().getId(), selectedUser.getId(), new RetroResponse<ChildUserModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        EditProfileActivity.this);
                                builder.setTitle("Profile deleted successfully");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                builder.show();

                                Intent intent7 = new Intent(EditProfileActivity.this, DeviceActivity.class);
                                startActivity(intent7);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                DialogUtils.diaplayDialog(EditProfileActivity.this, "Failed to delete the Profile", errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}