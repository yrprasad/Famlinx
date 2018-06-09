package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.DevicesAdapter;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.models.DeviceActivateModel;
import com.nglinx.pulse.models.DeviceModel;
import com.nglinx.pulse.models.UserModel;
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

public class DeviceActivity extends AbstractActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    List<DeviceModel> devicesList;
    List<ChildUserModel> childProfilesList;

    DevicesAdapter devicesAdapter;

    DataSession ds;

    DeviceModel selectedDevice;
    ChildUserModel selectedProfile;

    //Manage Device Dialog attributes
    Dialog md_dialog;
    Dialog ap_dialog;
    TextView md_udid_value;
    Spinner md_username_value;

    //Activate Device
    Dialog actd_dialog;
    EditText actd_code;
    EditText et_udid;
    Button actd_btn_ok, actd_btn_cancel;

    //Edit Profile
    TextView ep_name;
    EditText ep_code;
    Button ep_btn_activate, ep_btn_cancel;

    //Add Device Dialog attributes
    Dialog ad_dialog;
    EditText ad_udid_value, ad_username_value;
    Spinner ad_devicetype_value;
    Button ad_btn_add, ad_btn_cancel;

    //Add Profile Dialog attributes
    EditText et_profile_name;
    Button ad_profile_ok, ad_profile_cancel;

    ArrayAdapter<ChildUserModel> childProfileAdapter;

    Toolbar inc_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        ds = DataSession.getInstance();

        attachDeviceClickListener = new AttachDeviceClickListener();
        detachDeviceClickListener = new DetachDeviceClickListener();

        //Intialize all the Parent Abstract activities
        initializeParent();

        initializeIcons();

        refreshList();
    }

    protected void initializeIcons() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        devicesList = new ArrayList<DeviceModel>();
        childProfilesList = new ArrayList<ChildUserModel>();

        ds.setDevicesList(devicesList);
        ds.setChildProfilesList(childProfilesList);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.devices_viewPager);
        devicesAdapter = new DevicesAdapter(getSupportFragmentManager());
        viewPager.setAdapter(devicesAdapter);
        tabLayout.setupWithViewPager(viewPager);

        inc_toolbar = (Toolbar) findViewById(R.id.inc_toolbar);

        ImageView btn_add_profile = (ImageView) inc_toolbar.findViewById(R.id.btn_add_profile);
        btn_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProfileClickHandler();
            }
        });

        RelativeLayout layout_add_profile = (RelativeLayout) inc_toolbar.findViewById(R.id.layout_add_profile);
        layout_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProfileClickHandler();
            }
        });
    }

    private List<ChildUserModel> getChildProfiles() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllChildUser(DataSession.getInstance().getUserModel().getId(), new RetroResponse<ChildUserModel>() {
            @Override
            public void onSuccess() {
                childProfilesList.clear();
                childProfilesList.addAll(models);
                Collections.sort(childProfilesList);
                ds.setChildProfilesList(childProfilesList);
                synchronized (this) {
                    devicesAdapter.notifyDataSetChanged();
                }

                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(DeviceActivity.this, errorMsg);
            }
        });

        return childProfilesList;
    }

    private List<DeviceModel> getDevices() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listMyDevices(new RetroResponse<DeviceModel>() {
            @Override
            public void onSuccess() {
                devicesList.clear();
                devicesList.addAll(ApplicationUtils.getNonSensorDevices(models));
                Collections.sort(devicesList);
                ds.setDevicesList(devicesList);
                synchronized (this) {
                    devicesAdapter.notifyDataSetChanged();
                }
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(DeviceActivity.this, errorMsg);
            }
        });

        return devicesList;
    }

    public void refreshList() {
        getChildProfiles();
        getDevices();
    }

    public void onDeviceEdit(int position) {

        DeviceModel selectedDevice1 = getItem(position);

        if (selectedDevice1.isActivated()) {
            deactivateDevice(selectedDevice1);
        } else {
            activateDevice(selectedDevice1);
        }
    }

    private DeviceModel getItem(int position) {
        return ds.getDevicesList().get(position);
    }

    private ChildUserModel getChildProfileItem(int position) {
        return ds.getChildProfilesList().get(position);
    }


    public void buyDeviceClickHandler(View view) {
        Intent intent4 = new Intent(this, BuyNowActivity.class);
        startActivity(intent4);
        finish();
    }

    public void checkForAttach() {
        new AlertDialog.Builder(DeviceActivity.this)
                .setTitle("Activate Device")
                .setMessage("Do you want to attach Profile: " + selectedProfile.getName() + " to device ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {
                        attachDevice();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    public void addProfileClickHandler() {
        //Open the new Dialog to add a new Profile
        ap_dialog = new Dialog(DeviceActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        ap_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ap_dialog.setCancelable(true);
        ap_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ap_dialog.setContentView(R.layout.activity_add_member_profile);
        ap_dialog.show();

        et_profile_name = (EditText) ap_dialog.findViewById(R.id.et_profile_name);
        ad_profile_ok = (Button) ap_dialog.findViewById(R.id.ad_profile_ok);
        ad_profile_cancel = (Button) ap_dialog.findViewById(R.id.ad_profile_cancel);
        ad_profile_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ap_dialog.dismiss();
            }
        });

        ad_profile_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String profileName = null;

                if (et_profile_name.getText() != null)
                    profileName = et_profile_name.getText().toString();

                if ((null == profileName) || (profileName.length() == 0)) {
                    DialogUtils.diaplayErrorDialog(DeviceActivity.this, "Enter the Profile Name");
                } else {
                    final ChildUserModel childUserModel = new ChildUserModel();
                    childUserModel.setUsername(profileName);

                    new AlertDialog.Builder(DeviceActivity.this)
                            .setTitle("Activate Device")
                            .setMessage("Do you want to create Profile: " + profileName)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(final DialogInterface dialog, int whichButton) {

                                    final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceActivity.this);

                                    ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                                    apiEndpointInterface.createChildUser(ds.getUserModel().getId(), childUserModel, new RetroResponse<ChildUserModel>() {
                                        @Override
                                        public void onSuccess() {
                                            selectedProfile = model;
                                            ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                            getChildProfiles();
                                            DialogUtils.diaplaySuccessDialog(DeviceActivity.this, "Member Profile created successfully. Check activation mail");
                                            if (ap_dialog != null)
                                                ap_dialog.dismiss();
                                            checkForAttach();
                                        }

                                        @Override
                                        public void onFailure() {
                                            ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                            DialogUtils.diaplayDialog(DeviceActivity.this, "Failed to create Member Profile", errorMsg);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });
    }

    public void onDeviceActivateClick(View view, int position, long id) {
        /*long viewId = view.getId();
        if (viewId == R.id.img_manage_device) {
            onProfileManage(position);
        } else if (viewId == R.id.img_edit_device) {
            onDeviceEdit(position);
        }
        Toast.makeText(this, "Clicked on item", Toast.LENGTH_SHORT).show();*/
        DeviceModel model = getItem(position);
        activateDevice(model);
    }

    private void deactivateDevice(final DeviceModel selectedDevice1) {
        new AlertDialog.Builder(DeviceActivity.this)
                .setTitle("Deactivate Device")
                .setMessage("Do you want to de-activate Device " + selectedDevice1.getUdid())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {

                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceActivity.this);

                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.deactivateDevice(selectedDevice1.getId(), selectedDevice1, new RetroResponse<DeviceModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                getAndPopulateDevices();
                                if (ad_dialog != null)
                                    ad_dialog.dismiss();
                                DialogUtils.diaplaySuccessDialog(DeviceActivity.this, "Device de-activated successfully");
                            }

                            @Override
                            public void onFailure() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                DialogUtils.diaplayDialog(DeviceActivity.this, "Failed to de-activate Device", errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void activateDevice(final DeviceModel selectedDevice1) {
        actd_dialog = new Dialog(DeviceActivity.this);
        actd_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        actd_dialog.setCancelable(true);

        actd_dialog.setContentView(R.layout.activity_activate_device);
        actd_dialog.show();

        et_udid = (EditText) actd_dialog.findViewById(R.id.et_udid);
        et_udid.setText(selectedDevice1.getUdid());

        actd_code = (EditText) actd_dialog.findViewById(R.id.et_code);
        String activationCode = actd_code.getText().toString();

        actd_btn_ok = (Button) actd_dialog.findViewById(R.id.ep_btn_activate);
        actd_btn_cancel = (Button) actd_dialog.findViewById(R.id.actd_btn_cancel);

        actd_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String activationCode = null;

                if (actd_code.getText() != null)
                    activationCode = actd_code.getText().toString();

                if ((null == activationCode) || (activationCode.length() == 0)) {
                    DialogUtils.diaplayErrorDialog(DeviceActivity.this, "Enter the activation Code");
                } else {
                    final DeviceActivateModel deviceActivateModel = new DeviceActivateModel();
                    deviceActivateModel.setActivationCode(activationCode);

                    new AlertDialog.Builder(DeviceActivity.this)
                            .setTitle("Activate Device")
                            .setMessage("Do you want to Activate Device " + selectedDevice1.getUdid())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(final DialogInterface dialog, int whichButton) {

                                    final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceActivity.this);

                                    ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                                    apiEndpointInterface.activateDevice(selectedDevice1.getId(), deviceActivateModel, new RetroResponse<DeviceModel>() {
                                        @Override
                                        public void onSuccess() {
                                            ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                            getAndPopulateDevices();
                                            if (actd_dialog != null)
                                                actd_dialog.dismiss();
                                            DialogUtils.diaplaySuccessDialog(DeviceActivity.this, "Device activated successfully");
                                        }

                                        @Override
                                        public void onFailure() {
                                            ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                            DialogUtils.diaplayDialog(DeviceActivity.this, "Failed to activate Device", errorMsg);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

        actd_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actd_dialog.dismiss();
            }
        });
    }


    class AttachDeviceClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            selectedDevice = (DeviceModel) spinner_devices.getSelectedItem();

            new AlertDialog.Builder(DeviceActivity.this)
                    .setTitle("Attach Device")
                    .setMessage("Do you want to attach Device " + selectedDevice.getUdid() + " to " + selectedProfile.getUsername())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, int whichButton) {

                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceActivity.this);

                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.attachDevice(selectedDevice.getId(), selectedProfile.getUsername(), selectedDevice, new RetroResponse<DeviceModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    getAndPopulateDevices();
                                    if (md_dialog != null)
                                        md_dialog.dismiss();
                                    DialogUtils.diaplaySuccessDialog(DeviceActivity.this, "Device attached successfully");
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(DeviceActivity.this, "Failed to attach Device", errorMsg);
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

            String attachedDeviceUdid = selectedProfile.getUdid();

            final DeviceModel deviceModel = ApplicationUtils.getDeviceByUdid(attachedDeviceUdid, ds.getDevicesList());

            new AlertDialog.Builder(DeviceActivity.this)
                    .setTitle("Detach Device")
                    .setMessage("Do you want to detach Device ")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, int whichButton) {

                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceActivity.this);

                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.detachDevice(deviceModel.getId(), new DeviceModel(), new RetroResponse<DeviceModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    getAndPopulateDevices();
                                    if (md_dialog != null)
                                        md_dialog.dismiss();
                                    DialogUtils.diaplaySuccessDialog(DeviceActivity.this, "Device detached successfully");
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(DeviceActivity.this, "Failed to detach Device", errorMsg);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }


    public void activateProfile(final String activationCode, final ChildUserModel selectedChild) {

        final DeviceActivateModel deviceActivateModel = new DeviceActivateModel();
        deviceActivateModel.setActivationCode(activationCode);

        new AlertDialog.Builder(DeviceActivity.this)
                .setTitle("Activate Profile")
                .setMessage("Do you want to activate Profile ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {

                        UserModel userModel = new UserModel();
                        userModel.setToken(activationCode);
                        userModel.setEmail(selectedChild.getEmail());
                        userModel.setUsername(selectedChild.getUsername());

                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceActivity.this);

                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.activateChildUser(userModel, new RetroResponse<UserModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                getAndPopulateDevices();
                                if (md_dialog != null)
                                    md_dialog.dismiss();
                                DialogUtils.diaplaySuccessDialog(DeviceActivity.this, "Profile activated successfully");
                            }

                            @Override
                            public void onFailure() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                DialogUtils.diaplayDialog(DeviceActivity.this, "Failed to activate Profile", errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    private List<DeviceModel> getAndPopulateDevices() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listMyDevices(new RetroResponse<DeviceModel>() {
            @Override
            public void onSuccess() {
                devicesList.clear();
                devicesList.addAll(ApplicationUtils.getNonSensorDevices(models));
                Collections.sort(devicesList);
                ds.setDevicesList(devicesList);
                synchronized (this) {
                    devicesAdapter.notifyDataSetChanged();
                }
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(DeviceActivity.this, errorMsg);
            }
        });

        return devicesList;
    }

    TextView tv_profile;
    Spinner spinner_devices;
    Button md_btn_attach, md_btn_detach, md_btn_cancel;
    ImageView btn_buy_device;
    AttachDeviceClickListener attachDeviceClickListener;
    DetachDeviceClickListener detachDeviceClickListener;

    ArrayAdapter<DeviceModel> devicesAdapterForManagerProfile;

    private ChildUserModel getChildProfile(int position) {
        return ds.getChildProfilesList().get(position);
    }

    public void attachDevice() {
        md_dialog = new Dialog(DeviceActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        md_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        md_dialog.setCancelable(true);
        md_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        md_dialog.setContentView(R.layout.activity_manage_attach_device);
        md_dialog.show();

        tv_profile = (TextView) md_dialog.findViewById(R.id.tv_profile);
        spinner_devices = (Spinner) md_dialog.findViewById(R.id.spinner_udid);

        if (null != selectedProfile.getName())
            tv_profile.setText(selectedProfile.getName());

        List<DeviceModel> availableDevicesToAttach = ds.getAvailableDevicesToAttach();

        devicesAdapterForManagerProfile = new ArrayAdapter<DeviceModel>(DeviceActivity.this, android.R.layout.simple_spinner_item, availableDevicesToAttach);
        devicesAdapterForManagerProfile.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_devices.setAdapter(devicesAdapterForManagerProfile);

        md_btn_attach = (Button) md_dialog.findViewById(R.id.md_btn_attach);
        md_btn_cancel = (Button) md_dialog.findViewById(R.id.md_btn_cancel);
        btn_buy_device = (ImageView) md_dialog.findViewById(R.id.btn_buy_device);

        md_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                md_dialog.dismiss();
            }
        });

        md_btn_attach.setOnClickListener(attachDeviceClickListener);
    }

    public void detachDevice() {
        md_dialog = new Dialog(DeviceActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        md_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        md_dialog.setCancelable(true);
        md_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        md_dialog.setContentView(R.layout.activity_manage_detach_device);
        md_dialog.show();

        tv_profile = (TextView) md_dialog.findViewById(R.id.tv_profile);

        TextView tv_udid = (TextView) md_dialog.findViewById(R.id.tv_udid);

        if (null != selectedProfile.getName())
            tv_profile.setText(selectedProfile.getName());

        tv_udid.setText(selectedProfile.getUdid());

        md_btn_detach = (Button) md_dialog.findViewById(R.id.md_btn_detach);
        md_btn_cancel = (Button) md_dialog.findViewById(R.id.md_btn_cancel);

        md_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                md_dialog.dismiss();
            }
        });

        md_btn_detach.setOnClickListener(detachDeviceClickListener);
    }


    public void onProfileManage(int position) {

        selectedProfile = getChildProfile(position);
        if (null == selectedProfile.getUdid() || (selectedProfile.getUdid().length() == 0)) {
            //Device is not attached.  Show pop up to attach the device
            attachDevice();
        } else {
            //Device is attached. Shw pop to detach the device.
            detachDevice();
        }

        /*md_dialog = new Dialog(DeviceActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        md_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        md_dialog.setCancelable(true);
        md_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        md_dialog.setContentView(R.layout.activity_manage_device);
        md_dialog.show();

        tv_profile = (TextView) md_dialog.findViewById(R.id.tv_profile);
        spinner_devices = (Spinner) md_dialog.findViewById(R.id.spinner_udid);

        if (null != selectedProfile.getName())
            tv_profile.setText(selectedProfile.getName());

        List<DeviceModel> availableDevicesToAttach = ds.getAvailableDevicesToAttach();

        devicesAdapterForManagerProfile = new ArrayAdapter<DeviceModel>(DeviceActivity.this, android.R.layout.simple_spinner_item, availableDevicesToAttach);
        devicesAdapterForManagerProfile.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_devices.setAdapter(devicesAdapterForManagerProfile);

        md_btn_attach = (Button) md_dialog.findViewById(R.id.md_btn_attach);
        md_btn_detach = (Button) md_dialog.findViewById(R.id.md_btn_detach);
        md_btn_cancel = (Button) md_dialog.findViewById(R.id.md_btn_cancel);
        btn_buy_device = (ImageView) md_dialog.findViewById(R.id.btn_buy_device);

        if (null == selectedProfile.getUdid() || (selectedProfile.getUdid().length() == 0)) {
            //User is not attached to any user. Deactivate the detach button
            md_btn_detach.setVisibility(View.GONE);

            if ((availableDevicesToAttach == null) || (availableDevicesToAttach.size() == 0)) {
                md_btn_attach.setClickable(false);
                md_btn_attach.setEnabled(false);
            }
        } else {
            //User is attached to some user. Deactivate the Attach Button
            md_btn_attach.setVisibility(View.GONE);
            spinner_devices.setClickable(false);
        }

        md_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                md_dialog.dismiss();
            }
        });

        md_btn_attach.setOnClickListener(attachDeviceClickListener);
        md_btn_detach.setOnClickListener(detachDeviceClickListener);*/
    }

    public void onProfileItemClick(View view, int position, long id) {
        long viewId = view.getId();
        if (viewId == R.id.btn_profile_manage) {
            onProfileManage(position);
        } else if (viewId == R.id.btn_profile_edit) {
            editChildProfileClickHandler(view);
        }
    }

    public void activateChildProfileClickHandler(View v, int position) {
        final ChildUserModel selectedChild = getChildProfileItem(position);

        md_dialog = new Dialog(DeviceActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        md_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        md_dialog.setCancelable(true);
        md_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        md_dialog.setContentView(R.layout.activity_profile_activate);
        md_dialog.show();

        ep_name = (TextView) md_dialog.findViewById(R.id.ep_username);
        ep_code = (EditText) md_dialog.findViewById(R.id.ep_code);
        ep_btn_activate = (Button) md_dialog.findViewById(R.id.ep_btn_activate);
        ep_btn_cancel = (Button) md_dialog.findViewById(R.id.ep_btn_cancel);
        ep_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                md_dialog.dismiss();
            }
        });

        ep_name.setText(selectedChild.getName());

        ep_btn_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activationCode = null;
                if ((null != ep_code.getText()) && (ep_code.getText().toString().length() != 0))
                    activationCode = ep_code.getText().toString();
                if ((null == activationCode) || (activationCode.length() == 0)) {
                    DialogUtils.diaplayErrorDialog(DeviceActivity.this, "Enter the activation Code");
                }
                activateProfile(activationCode, selectedChild);
            }
        });

    }

    public void editChildProfileClickHandler(View v) {
        final ChildUserModel iModel = (ChildUserModel) v.getTag();

        Intent intent7 = new Intent(this, EditProfileActivity.class);
        intent7.putExtra(ApplicationConstants.CHILD_PROFILE_SELECTED_USERID, iModel.getId());
        startActivity(intent7);
        finish();
    }
}