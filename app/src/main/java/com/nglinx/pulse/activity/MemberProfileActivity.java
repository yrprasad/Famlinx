package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.MemberProfileAdapter;
import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.models.DeviceModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemberProfileActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView lv_member_profiles;

    List<ChildUserModel> childProfilesList;
    List<DeviceModel> deviceList;
    MemberProfileAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    Dialog md_dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_member_profile);

        initializeParent();

        //Initialize all the icons on this screen and Navigation Menu screens
        initializeIcons();

        //Get the notifications list
        getAndPopulateMemberProfiles();

        //Get the notifications list
        getAndPopulateDevices();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    @Override
    protected void initializeIcons() {

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        lv_member_profiles = (ListView) findViewById(R.id.lv_member_profiles);

        childProfilesList = new ArrayList<ChildUserModel>();
        adapter = new MemberProfileAdapter(MemberProfileActivity.this, (ArrayList<ChildUserModel>) childProfilesList);

        lv_member_profiles.setAdapter(adapter);
    }


    private List<DeviceModel> getAndPopulateDevices() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listMyDevices(new RetroResponse<DeviceModel>() {
            @Override
            public void onSuccess() {
                deviceList.clear();
                deviceList.addAll(ApplicationUtils.getNonSensorDevices(models));
                Collections.sort(deviceList);
                ds.setDevicesList(deviceList);
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(MemberProfileActivity.this, errorMsg);
            }
        });

        return deviceList;
    }

    private List<ChildUserModel> getAndPopulateMemberProfiles() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllChildUser(DataSession.getInstance().getUserModel().getId(), new RetroResponse<ChildUserModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    childProfilesList.clear();
                    Collections.sort((List<ChildUserModel>) childProfilesList);
                    childProfilesList.addAll(models);
                    adapter.notifyDataSetChanged();
                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                }
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(MemberProfileActivity.this, errorMsg);
            }
        });

        return childProfilesList;
    }

    @Override
    public void onRefresh() {
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllChildUser(DataSession.getInstance().getUserModel().getId(), new RetroResponse<ChildUserModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    childProfilesList.clear();
                    Collections.sort((List<ChildUserModel>) childProfilesList);
                    childProfilesList.addAll(models);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure() {
                DialogUtils.diaplayFailureDialog(MemberProfileActivity.this, errorMsg);
            }
        });
    }

    public void deleteChildProfileClickHandler(View v) {
        final ChildUserModel iModel = (ChildUserModel) v.getTag();
        new AlertDialog.Builder(MemberProfileActivity.this)
                .setTitle("Delete Profile")
                .setMessage("Do you want to delete the profile ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MemberProfileActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.deleteChildUser(DataSession.getInstance().getUserModel().getId(), iModel.getId(), new RetroResponse<ChildUserModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        MemberProfileActivity.this);
                                builder.setTitle("Profile delete successfully");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.dismiss();
                                                Log.e("info", "OK");
                                                //TODO: Refresh
                                                swipeRefreshLayout.setRefreshing(true);
                                                // directly call onRefresh() method
                                                onRefresh();
                                            }
                                        });
                                builder.show();
                            }

                            @Override
                            public void onFailure() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                DialogUtils.diaplayDialog(MemberProfileActivity.this, "Failed to delete the Profile", errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void viewChildProfileClickHandler(View v) {
        Toast.makeText(this, "TBD", Toast.LENGTH_SHORT).show();
    }

    public void onItemClick(View view, int position, long id) {
        long viewId = view.getId();
        if (viewId == R.id.btn_profile_manage) {
            onProfileManage(position);
        } else if (viewId == R.id.btn_profile_edit) {
            viewChildProfileClickHandler(view);
        }
    }

    private ChildUserModel getItem(int position) {
        return ds.getChildProfilesList().get(position);
    }

    TextView tv_profile;
    Spinner spinner_devices;
    ArrayAdapter<DeviceModel> devicesAdapter;
    Button md_btn_attach, md_btn_detach, md_btn_cancel;
    ImageView btn_buy_device;
    AttachDeviceClickListener attachDeviceClickListener;
    DetachDeviceClickListener detachDeviceClickListener;
    ChildUserModel selectedProfile;

    public void onProfileManage(int position) {

        selectedProfile = getItem(position);

        md_dialog = new Dialog(MemberProfileActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        md_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        md_dialog.setCancelable(true);
        md_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        md_dialog.setContentView(R.layout.activity_manage_device);
        md_dialog.show();

        tv_profile = (TextView) md_dialog.findViewById(R.id.tv_profile);
        spinner_devices = (Spinner) md_dialog.findViewById(R.id.spinner_udid);

        if (null != selectedProfile.getName())
            tv_profile.setText(selectedProfile.getName());

        devicesAdapter = new ArrayAdapter<DeviceModel>(MemberProfileActivity.this, android.R.layout.simple_spinner_item, ds.getAvailableDevicesToAttach());
        devicesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_devices.setAdapter(devicesAdapter);

        md_btn_attach = (Button) md_dialog.findViewById(R.id.md_btn_attach);
        md_btn_detach = (Button) md_dialog.findViewById(R.id.md_btn_detach);
        md_btn_cancel = (Button) md_dialog.findViewById(R.id.md_btn_cancel);
        btn_buy_device = (ImageView) md_dialog.findViewById(R.id.btn_buy_device);

        if (null == selectedProfile.getUdid() || (selectedProfile.getUdid().length() == 0)) {
            //User is not attached to any user. Deactivate the detach button
            md_btn_detach.setVisibility(View.GONE);
            btn_buy_device.setVisibility(View.VISIBLE);
        } else {
            //User is attached to some user. Deactivate the Attach Button
            md_btn_attach.setVisibility(View.GONE);
            btn_buy_device.setVisibility(View.GONE);
            spinner_devices.setClickable(false);
        }

        md_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                md_dialog.dismiss();
            }
        });

        md_btn_attach.setOnClickListener(attachDeviceClickListener);
        md_btn_detach.setOnClickListener(detachDeviceClickListener);
    }

    class AttachDeviceClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            final DeviceModel selectedDevice = (DeviceModel) spinner_devices.getSelectedItem();

            new AlertDialog.Builder(MemberProfileActivity.this)
                    .setTitle("Attach Device")
                    .setMessage("Do you want to attach Device " + selectedDevice.getUdid() + " to " + selectedProfile.getUsername())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, int whichButton) {

                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MemberProfileActivity.this);

                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.attachDevice(selectedDevice.getId(), selectedProfile.getUsername(), selectedDevice, new RetroResponse<DeviceModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    getAndPopulateDevices();
                                    if (md_dialog != null)
                                        md_dialog.dismiss();
                                    DialogUtils.diaplaySuccessDialog(MemberProfileActivity.this, "Device attached successfully");
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(MemberProfileActivity.this, "Failed to attach Device", errorMsg);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    class DetachDeviceClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            final DeviceModel selectedDevice = (DeviceModel) spinner_devices.getSelectedItem();

            new AlertDialog.Builder(MemberProfileActivity.this)
                    .setTitle("Detach Device")
                    .setMessage("Do you want to detach Device ")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, int whichButton) {

                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MemberProfileActivity.this);

                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.detachDevice(selectedDevice.getId(), selectedDevice, new RetroResponse<DeviceModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    getAndPopulateDevices();
                                    if (md_dialog != null)
                                        md_dialog.dismiss();
                                    DialogUtils.diaplaySuccessDialog(MemberProfileActivity.this, "Device detached successfully");
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(MemberProfileActivity.this, "Failed to detach Device", errorMsg);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

}