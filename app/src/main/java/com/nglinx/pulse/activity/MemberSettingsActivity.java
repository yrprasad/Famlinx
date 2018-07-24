package com.nglinx.pulse.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nglinx.pulse.R;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.FenceModel;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.models.NotificationModel;
import com.nglinx.pulse.models.SettingsModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.List;

public class MemberSettingsActivity extends AppCompatActivity {

    //    RadioButton rb_miles;
//    RadioButton rb_kms;
//    EditText et_speed_limit;
    Spinner spinner_fence;

    Button btn_ok, btn_cancel;

    ArrayAdapter<FenceModel> fenceAdapter;
    List<FenceModel> myFences;
    FenceModel emptyFence = null;

    DataSession ds;
    SettingsModel settingsModel = null;

    Intent intent = null;

    ToggleButton speed_toggle;
    SeekBar speed_seekbar;
    TextView speed_text;

    SeekBar.OnSeekBarChangeListener speedseekBarChangeListener = null;
    SpeedToggleChangeListener speedToggleChangeListener = null;

    CheckBox cb_phone_fence;
    CheckBox cb_phone_speed;
    CheckBox cb_phone_battery;

    CheckBox cb_email_fence;
    CheckBox cb_email_speed;
    CheckBox cb_email_battery;

    public MemberSettingsActivity() {
        intent = new Intent();
        emptyFence = new FenceModel();
        emptyFence.setName("");
        emptyFence.setId("0");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_setting);

        initializeIcons();

        getFenceList();

        displayUserSettings();
    }

    protected void initializeIcons() {

        ds = DataSession.getInstance();

//        rb_miles = (RadioButton) findViewById(R.id.rb_miles);
//        rb_kms = (RadioButton) findViewById(R.id.rb_kms);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

//        rb_miles.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked == true)
//                    rb_kms.setChecked(false);
//            }
//        });
//
//        rb_kms.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked == true)
//                    rb_miles.setChecked(false);
//            }
//        });

//        et_speed_limit = (EditText) findViewById(R.id.et_speed_limit);
        spinner_fence = (Spinner) findViewById(R.id.spinner_fence);

        myFences = new ArrayList<FenceModel>();
        fenceAdapter = new ArrayAdapter<FenceModel>(this, android.R.layout.simple_spinner_item, myFences);
        fenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fence.setAdapter(fenceAdapter);

        speedseekBarChangeListener = new SettingsSeekBarChangeListener();
        speedToggleChangeListener = new SpeedToggleChangeListener();

        speed_toggle = (ToggleButton) findViewById(R.id.speed_toggle);
        speed_seekbar = (SeekBar) findViewById(R.id.speed_seekbar);
        speed_text = (TextView) findViewById(R.id.speed_text);

        speed_seekbar.setOnSeekBarChangeListener(speedseekBarChangeListener);
        speed_toggle.setOnCheckedChangeListener(speedToggleChangeListener);

        cb_phone_fence = (CheckBox) findViewById(R.id.cb_phone_fence);
        cb_phone_speed = (CheckBox) findViewById(R.id.cb_phone_speed);
        cb_phone_battery = (CheckBox) findViewById(R.id.cb_phone_battery);

        cb_email_fence = (CheckBox) findViewById(R.id.cb_email_fence);
        cb_email_speed = (CheckBox) findViewById(R.id.cb_email_speed);
        cb_email_battery = (CheckBox) findViewById(R.id.cb_email_battery);

        Toolbar inc_toolbar;

        inc_toolbar = (Toolbar) findViewById(R.id.inc_toolbar);

        Button btn_toolbar_cancel = (Button) inc_toolbar.findViewById(R.id.btn_toolbar_cancel);
        btn_toolbar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }


    void displayUserSettings() {
        //Get the Member Details
        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MemberSettingsActivity.this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getMemberSettings(ds.getUserModel().getId(), ds.getSelected_group_member_id(), new RetroResponse<SettingsModel>() {
            @Override
            public void onSuccess() {
                settingsModel = model;
                setValuesInView();
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayErrorDialog(MemberSettingsActivity.this, "User Settings not found. User not yet accepted your invite");
            }
        });
    }

    private FenceModel getEmptyFence() {
        return emptyFence;
    }

    private void clearFences() {
        myFences.clear();
        myFences.add(getEmptyFence());
    }

    public void getFenceList() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MemberSettingsActivity.this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listFence(DataSession.getInstance().getUserModel().getId(), new RetroResponse<FenceModel>() {

            @Override
            public void onSuccess() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                clearFences();
                myFences.addAll(models);
                fenceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayDialog(MemberSettingsActivity.this, "Failed to get the fence list", errorMsg);
            }
        });
    }


    void setValuesInView() {

        //TODO: Set the member name
//        member_name.setText(ds.getSelected_group_member_name() + " Settings");

        if ((settingsModel.getSpeed() != null) && (settingsModel.getSpeed().isEnabled())) {
            speed_toggle.setChecked(true);
            speed_text.setVisibility(View.VISIBLE);
            speed_text.setText(settingsModel.getSpeed().getLimit().toString());
            speed_seekbar.setVisibility(View.VISIBLE);
            speed_seekbar.setProgress(settingsModel.getSpeed().getLimit());
        } else {
            speed_seekbar.setVisibility(View.INVISIBLE);
        }

        if ((settingsModel.getFence() != null) && (settingsModel.getFence().isEnabled())) {
            spinner_fence.setVisibility(View.VISIBLE);
            FenceModel fenceModel = ApplicationUtils.getFenceModeById(myFences, settingsModel.getFence().getId());
            spinner_fence.setSelection(fenceAdapter.getPosition(fenceModel));
        } else {
            spinner_fence.setSelection(fenceAdapter.getPosition(emptyFence));
        }

        if (settingsModel.getEmailNotifications() != null) {
            cb_email_fence.setChecked(settingsModel.getEmailNotifications().isFenceNotification());
        } else {
            cb_email_fence.setChecked(true);
        }

        if (settingsModel.getEmailNotifications() != null) {
            cb_email_speed.setChecked(settingsModel.getEmailNotifications().isSpeedNotification());
        } else {
            cb_email_speed.setChecked(true);
        }

        if (settingsModel.getEmailNotifications() != null) {
            cb_email_battery.setChecked(settingsModel.getEmailNotifications().isBatteryNotification());
        } else {
            cb_email_battery.setChecked(true);
        }


        if (settingsModel.getPhoneNotifications() != null) {
            cb_phone_fence.setChecked(settingsModel.getPhoneNotifications().isFenceNotification());
        } else {
            cb_phone_fence.setChecked(true);
        }

        if (settingsModel.getPhoneNotifications() != null) {
            cb_phone_speed.setChecked(settingsModel.getPhoneNotifications().isSpeedNotification());
        } else {
            cb_phone_speed.setChecked(true);
        }

        if (settingsModel.getPhoneNotifications() != null) {
            cb_phone_battery.setChecked(settingsModel.getPhoneNotifications().isBatteryNotification());
        } else {
            cb_phone_battery.setChecked(true);
        }
    }


    public void onDeleteMember(View v) {

        String groupId = ds.getSelected_group_id();
        if ((null == groupId) || (groupId.equalsIgnoreCase(""))) {
            GroupModel group = ApplicationUtils.getGroupOfSelectedMember(ds.getSelected_group_member_id(), ds.getUserModel().getGroups());
            groupId = group.getId();
        }

        final String fGroupId = groupId;

        new AlertDialog.Builder(MemberSettingsActivity.this)
                .setTitle("Delete Member")
                .setMessage("Do you want to delete member " + ds.getSelected_group_member_name())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MemberSettingsActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.deleteMemerFromGroup(ds.getUserModel().getId(), fGroupId, ds.getSelected_group_member_id(), new RetroResponse<GroupMemberModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                DialogUtils.diaplaySuccessDialog(MemberSettingsActivity.this, "Successfully deleted the member");
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                DialogUtils.diaplayDialog(MemberSettingsActivity.this, "Failed to delete the member", errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void onSaveSettings(View v) {

        System.out.println("Settings Activity Save Button Clicked");

        if(settingsModel == null)
        {
            DialogUtils.diaplayErrorDialog(getApplicationContext(), "Cannot save settings. User not yet accepted your invite");
            return;
        }

        if (!speed_text.getText().equals("")) {
            settingsModel.getSpeed().setEnabled(true);
            settingsModel.getSpeed().setLimit(Integer.parseInt(speed_text.getText().toString()));
        } else {
            settingsModel.getSpeed().setEnabled(false);
            settingsModel.getSpeed().setLimit(0);
        }
        if ((spinner_fence.getSelectedItem() == getEmptyFence())) {
            settingsModel.getFence().setEnabled(false);
            settingsModel.getFence().setId(getEmptyFence().getId());
        } else if ((spinner_fence.getSelectedItem() != null)) {
            settingsModel.getFence().setEnabled(true);
            FenceModel fenceModel = (FenceModel) spinner_fence.getSelectedItem();
            settingsModel.getFence().setId(fenceModel.getId());
        }

        if (cb_email_fence.isChecked())
            settingsModel.getEmailNotifications().setFenceNotification(true);
        else
            settingsModel.getEmailNotifications().setFenceNotification(false);

        if (cb_email_speed.isChecked())
            settingsModel.getEmailNotifications().setSpeedNotification(true);
        else
            settingsModel.getEmailNotifications().setSpeedNotification(false);

        if (cb_email_battery.isChecked())
            settingsModel.getEmailNotifications().setBatteryNotification(true);
        else
            settingsModel.getEmailNotifications().setBatteryNotification(false);


        if (cb_phone_fence.isChecked())
            settingsModel.getPhoneNotifications().setFenceNotification(true);
        else
            settingsModel.getPhoneNotifications().setFenceNotification(false);

        if (cb_phone_speed.isChecked())
            settingsModel.getPhoneNotifications().setSpeedNotification(true);
        else
            settingsModel.getPhoneNotifications().setSpeedNotification(false);

        if (cb_phone_battery.isChecked())
            settingsModel.getPhoneNotifications().setBatteryNotification(true);
        else
            settingsModel.getPhoneNotifications().setBatteryNotification(false);

        //Get the Member Details
        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MemberSettingsActivity.this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.updateMemberSettings(ds.getUserModel().getId(), ds.getSelected_group_member_id(), settingsModel, new RetroResponse<SettingsModel>() {
            @Override
            public void onSuccess() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
//                DialogUtils.diaplaySuccessDialog(MemberSettingsActivity.this, "Successfully saved the Member Settings");
                setResult(ApplicationConstants.SETTINGS_NORMAL_FINISH, intent);
                finish();
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayDialog(MemberSettingsActivity.this, "Failed to save the Member Settings", errorMsg);
            }
        });
    }

    public void onCancelSettings(View v) {
        ds.clearSelectedGroupMember();
        setResult(ApplicationConstants.SETTINGS_NORMAL_FINISH, intent);
        this.finish();
    }

    private class SettingsSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            speed_text.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    private class SpeedToggleChangeListener implements ToggleButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                speed_text.setVisibility(View.VISIBLE);
                if (settingsModel.getSpeed().getLimit() == null) {
                    settingsModel.getSpeed().setLimit(0);
                }
                speed_text.setText(settingsModel.getSpeed().getLimit().toString());
                speed_seekbar.setVisibility(View.VISIBLE);
                speed_seekbar.setProgress(settingsModel.getSpeed().getLimit());
            } else {
                speed_text.setVisibility(View.INVISIBLE);
                speed_seekbar.setVisibility(View.INVISIBLE);
                speed_text.setText("");
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}