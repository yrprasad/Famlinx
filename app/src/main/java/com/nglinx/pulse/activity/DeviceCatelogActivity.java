package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.DeviceCatalogAdapter;
import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.models.DeviceActivateModel;
import com.nglinx.pulse.models.DeviceModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.List;

public class DeviceCatelogActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {

    // Create the adapter to convert the array to views
    DeviceCatalogAdapter adapter;

    ListView lv_devices;

    List<DeviceModel> deviceList;

    private SwipeRefreshLayout swipeRefreshLayout;

    DeviceModel selectedDevice;

    //Add Device Dialog attributes
    Dialog ad_dialog;
    EditText ad_udid_value, ad_username_value;
    Spinner ad_devicetype_value;
    Button ad_btn_add, ad_btn_cancel;

    //Manage Device Dialog attributes
    Dialog md_dialog;
    TextView md_udid_value;
    Spinner md_username_value;
    Button md_btn_attach, md_btn_detach, md_btn_cancel;

    //Activate Device
    Dialog actd_dialog;
    EditText actd_code;
    Button actd_btn_ok, actd_btn_cancel;

    List<ChildUserModel> myChildProfiles = null;
    ArrayAdapter<ChildUserModel> childProfileAdapter;

//    AddDeviceClickListener addDeviceClickListener;

    AttachDeviceClickListener attachDeviceClickListener;
    DetachDeviceClickListener detachDeviceClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_catelog);

        initializeParent();

        initializeIcons();

        getChildProfiles();

        attachDeviceClickListener = new AttachDeviceClickListener();
        detachDeviceClickListener = new DetachDeviceClickListener();

        getAndPopulateDevices();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    @Override
    protected void initializeIcons() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshDeviceCatalog);
        swipeRefreshLayout.setOnRefreshListener(this);

        myChildProfiles = new ArrayList<ChildUserModel>();
        deviceList = new ArrayList<DeviceModel>();

        lv_devices = (ListView) findViewById(R.id.lv_device_catalog);
        adapter = new DeviceCatalogAdapter(this, (ArrayList<DeviceModel>) deviceList, (ArrayList<ChildUserModel>) myChildProfiles);
        lv_devices.setAdapter(adapter);
    }

    private List<ChildUserModel> getChildProfiles() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllChildUser(DataSession.getInstance().getUserModel().getId(), new RetroResponse<ChildUserModel>() {
            @Override
            public void onSuccess() {
                myChildProfiles.clear();
                myChildProfiles.addAll(models);
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(DeviceCatelogActivity.this, errorMsg);
            }
        });

        return myChildProfiles;
    }

    private List<DeviceModel> getAndPopulateDevices() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listMyDevices(new RetroResponse<DeviceModel>() {
            @Override
            public void onSuccess() {
                deviceList.clear();
                deviceList.addAll(ApplicationUtils.getNonSensorDevices(models));
                adapter.notifyDataSetChanged();
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(DeviceCatelogActivity.this, errorMsg);
            }
        });

        return deviceList;
    }

    /*class AddDeviceClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            ad_dialog = new Dialog(DeviceCatelogActivity.this);
            ad_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            ad_dialog.setCancelable(true);

            // Include dialog.xml file
            ad_dialog.setContentView(R.layout.activity_adddevice);
            ad_dialog.show();

            //Add Fence Dialog related attributes
            ad_devicetype_value = (Spinner) ad_dialog.findViewById(R.id.ad_devicetype_value);
            String deviceTypeStr = String.valueOf(ad_devicetype_value.getSelectedItem());
            final DeviceType deviceType = DeviceType.valueOf(deviceTypeStr);

            ad_btn_add = (Button) ad_dialog.findViewById(R.id.ad_btn_add);
            ad_btn_cancel = (Button) ad_dialog.findViewById(R.id.ad_btn_cancel);

            ad_btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(DeviceCatelogActivity.this)
                            .setTitle("Add Device")
                            .setMessage("Do you want to add Device ")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(final DialogInterface dialog, int whichButton) {

                                    final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceCatelogActivity.this);

                                    ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                                    apiEndpointInterface.purchaseDevice(deviceType, new DeviceModel(), new RetroResponse<DeviceModel>() {
                                        @Override
                                        public void onSuccess() {
                                            ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                            getAndPopulateDevices();
                                            if (ad_dialog != null)
                                                ad_dialog.dismiss();
                                            DialogUtils.diaplaySuccessDialog(DeviceCatelogActivity.this, "Device added successfully");
                                        }

                                        @Override
                                        public void onFailure() {
                                            ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                            DialogUtils.diaplayDialog(DeviceCatelogActivity.this, "Failed to add Device", errorMsg);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });


            ad_btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ad_dialog.dismiss();
                }
            });
        }
    }*/

    class AttachDeviceClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            final ChildUserModel selectedChild = (ChildUserModel) md_username_value.getSelectedItem();

            new AlertDialog.Builder(DeviceCatelogActivity.this)
                    .setTitle("Attach Device")
                    .setMessage("Do you want to attach Device " + selectedDevice.getUdid() + " to " + selectedChild.getUsername())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, int whichButton) {

                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceCatelogActivity.this);

                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.attachDevice(selectedDevice.getId(), selectedChild.getUsername(), selectedDevice, new RetroResponse<DeviceModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    getAndPopulateDevices();
                                    if (ad_dialog != null)
                                        ad_dialog.dismiss();
                                    DialogUtils.diaplaySuccessDialog(DeviceCatelogActivity.this, "Device attached successfully");
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(DeviceCatelogActivity.this, "Failed to attach Device", errorMsg);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    class DetachDeviceClickListener implements View.OnClickListener {

        //TODO: This needs to be changes to appropriate API.
        @Override
        public void onClick(View view) {

            final ChildUserModel selectedChild = (ChildUserModel) md_username_value.getSelectedItem();

            new AlertDialog.Builder(DeviceCatelogActivity.this)
                    .setTitle("Detach Device")
                    .setMessage("Do you want to detach Device ")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, int whichButton) {

                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceCatelogActivity.this);

                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.detachDevice(selectedDevice.getId(), selectedDevice, new RetroResponse<DeviceModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    getAndPopulateDevices();
                                    if (md_dialog != null)
                                        md_dialog.dismiss();
                                    DialogUtils.diaplaySuccessDialog(DeviceCatelogActivity.this, "Device detached successfully");
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(DeviceCatelogActivity.this, "Failed to detach Device", errorMsg);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    private void deactivateDevice(final DeviceModel selectedDevice1) {
        new AlertDialog.Builder(DeviceCatelogActivity.this)
                .setTitle("Deactivate Device")
                .setMessage("Do you want to de-activate Device " + selectedDevice1.getUdid())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {

                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceCatelogActivity.this);

                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.deactivateDevice(selectedDevice1.getId(), selectedDevice1, new RetroResponse<DeviceModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                getAndPopulateDevices();
                                if (ad_dialog != null)
                                    ad_dialog.dismiss();
                                DialogUtils.diaplaySuccessDialog(DeviceCatelogActivity.this, "Device de-activated successfully");
                            }

                            @Override
                            public void onFailure() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                DialogUtils.diaplayDialog(DeviceCatelogActivity.this, "Failed to de-activate Device", errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void activateDevice(final DeviceModel selectedDevice1) {
        actd_dialog = new Dialog(DeviceCatelogActivity.this);
        actd_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        actd_dialog.setCancelable(true);

        actd_dialog.setContentView(R.layout.activity_activate_device);
        actd_dialog.show();

        actd_code = (EditText) actd_dialog.findViewById(R.id.et_code);
        String activationCode = actd_code.getText().toString();
        final DeviceActivateModel deviceActivateModel = new DeviceActivateModel();
        deviceActivateModel.setActivationCode(activationCode);

        actd_btn_ok = (Button) actd_dialog.findViewById(R.id.ep_btn_activate);
        actd_btn_cancel = (Button) actd_dialog.findViewById(R.id.actd_btn_cancel);

        actd_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(DeviceCatelogActivity.this)
                        .setTitle("Activate Device")
                        .setMessage("Do you want to Activate Device " + selectedDevice1.getUdid())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(final DialogInterface dialog, int whichButton) {

                                final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(DeviceCatelogActivity.this);

                                ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                                apiEndpointInterface.activateDevice(selectedDevice1.getId(), deviceActivateModel, new RetroResponse<DeviceModel>() {
                                    @Override
                                    public void onSuccess() {
                                        ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                        getAndPopulateDevices();
                                        if (actd_dialog != null)
                                            actd_dialog.dismiss();
                                        DialogUtils.diaplaySuccessDialog(DeviceCatelogActivity.this, "Device activated successfully");
                                    }

                                    @Override
                                    public void onFailure() {
                                        ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                        DialogUtils.diaplayDialog(DeviceCatelogActivity.this, "Failed to activate Device", errorMsg);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        actd_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actd_dialog.dismiss();
            }
        });
    }


    private DeviceModel getItem(int position) {
        return deviceList.get(position);
    }

    public void onDeviceEdit(int position) {

        DeviceModel selectedDevice1 = getItem(position);

        if (selectedDevice1.isActivated()) {
            deactivateDevice(selectedDevice1);
        } else {
            activateDevice(selectedDevice1);
        }
    }

    //TODO: Need to remove thid dummy
    public void onDeviceManage(int position) {

        selectedDevice = getItem(position);

        md_dialog = new Dialog(DeviceCatelogActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        md_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        md_dialog.setCancelable(true);
        md_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        md_dialog.setContentView(R.layout.activity_manage_device);
        md_dialog.show();

        md_udid_value = (TextView) md_dialog.findViewById(R.id.et_udid);

        if (null != selectedDevice.getUdid())
            md_udid_value.setText(selectedDevice.getUdid());

        md_username_value = (Spinner) md_dialog.findViewById(R.id.spinner_udid);

        childProfileAdapter = new ArrayAdapter<ChildUserModel>(DeviceCatelogActivity.this, android.R.layout.simple_spinner_item, myChildProfiles);
        childProfileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        md_username_value.setAdapter(childProfileAdapter);

        md_btn_attach = (Button) md_dialog.findViewById(R.id.md_btn_attach);
        md_btn_detach = (Button) md_dialog.findViewById(R.id.md_btn_detach);
        md_btn_cancel = (Button) md_dialog.findViewById(R.id.md_btn_cancel);

        md_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                md_dialog.dismiss();
            }
        });

        md_btn_attach.setOnClickListener(attachDeviceClickListener);
        md_btn_detach.setOnClickListener(detachDeviceClickListener);
    }

    public void onItemClick(View view, int position, long id) {
        long viewId = view.getId();
        DeviceModel model = getItem(position);
        if (viewId == R.id.img_activate_device) {
            activateDevice(model);
        }
        /*else if (viewId == R.id.img_edit_device) {
            onDeviceEdit(position);
        }*/
    }

    //TODO: Need to complete the function.
    @Override
    public void onRefresh() {
        getChildProfiles();
        swipeRefreshLayout.setRefreshing(false);
    }
}