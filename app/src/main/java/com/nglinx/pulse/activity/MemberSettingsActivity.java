package com.nglinx.pulse.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nglinx.pulse.R;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.FenceModel;
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

    CheckBox  checkbox_battery1;

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

        checkbox_battery1 = (CheckBox) findViewById(R.id.checkbox_battery1);
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
                DialogUtils.diaplayDialog(MemberSettingsActivity.this, "Failed to get the Member Settings", errorMsg);
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

        if (settingsModel.isLowBattery() == true) {
            checkbox_battery1.setChecked(true);
        }
    }


    public void onSaveSettings(View v) {

        boolean saveSettings = true;
        System.out.println("Settings Activity Save Button Clicked");

        if (!speed_text.getText().equals("")) {
            settingsModel.getSpeed().setEnabled(true);
            settingsModel.getSpeed().setLimit(Integer.parseInt(speed_text.getText().toString()));
        } else {
            settingsModel.getSpeed().setEnabled(false);
            settingsModel.getSpeed().setLimit(0);
        }
//        settingsModel.setLoop(loop_toggle.isChecked());
//        settingsModel.setTamper(tamper_toggle.isChecked());
        settingsModel.setLowBattery(checkbox_battery1.isChecked());

        if ((spinner_fence.getSelectedItem() == getEmptyFence())) {
            settingsModel.getFence().setEnabled(false);
            settingsModel.getFence().setId(getEmptyFence().getId());
        } else if ((spinner_fence.getSelectedItem() != null)) {
            settingsModel.getFence().setEnabled(true);
            FenceModel fenceModel = (FenceModel) spinner_fence.getSelectedItem();
            settingsModel.getFence().setId(fenceModel.getId());
        }

        if (saveSettings == false)
            return;

        //Get the Member Details
        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MemberSettingsActivity.this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.updateMemberSettings(ds.getUserModel().getId(), ds.getSelected_group_member_id(), settingsModel, new RetroResponse<SettingsModel>() {
            @Override
            public void onSuccess() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplaySuccessDialog(MemberSettingsActivity.this, "Successfully saved the Member Settings");
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
}