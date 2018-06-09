package com.nglinx.pulse.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.DeviceTypesAdapter;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.DeviceCartModel;
import com.nglinx.pulse.models.DeviceOrderType;
import com.nglinx.pulse.models.DeviceType;
import com.nglinx.pulse.models.DeviceTypesModel;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;
import com.nglinx.pulse.utils.view.HorizontalView;

import java.util.ArrayList;

public class BuyNowActivity extends AbstractActivity {

    HorizontalView hv_devices;
    DeviceTypesAdapter deviceTypesAdapter;
    ArrayList<DeviceTypesModel> deviceTypesList;
    ArrayList<DeviceCartModel> devicesCart;

    DeviceTypeClickListener deviceTypeClickListener;

    TextView tv_size_1;
    TextView tv_size_2;
    TextView tv_size_3;

    TextView tv_desc_1;
    TextView tv_desc_2;
    TextView tv_desc_3;

    TextView tv_care_1;
    TextView tv_care_2;
    TextView tv_care_3;

    ImageView img_mini_device_type;
    ImageView img_mini_device_type_1;
    ImageView img_mini_device_type_2;
    ImageView img_mini_device_type_3;
    ImageView img_mini_device_type_4;

    TextView tv_device_count_minus;
    NumberPicker tv_device_count_value;
    TextView tv_device_count_plus;

    NumberPicker et_rental_days;

    DeviceTypesModel selectedMember;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_buy_now);

        initializeParent();

        initializeIcons();

        getDeviceTypes();
    }

    protected void initializeIcons() {
        deviceTypesList = new ArrayList<>();
        deviceTypesAdapter = new DeviceTypesAdapter(getApplicationContext(), deviceTypesList);
        hv_devices = (HorizontalView) findViewById(R.id.hv_devices);
        hv_devices.setAdapter(deviceTypesAdapter);
        deviceTypeClickListener = new DeviceTypeClickListener();
        hv_devices.setOnItemClickListener(deviceTypeClickListener);

        tv_size_1 = (TextView) findViewById(R.id.tv_size_1);
        tv_size_2 = (TextView) findViewById(R.id.tv_size_2);
        tv_size_3 = (TextView) findViewById(R.id.tv_size_3);

        tv_desc_1 = (TextView) findViewById(R.id.tv_desc_1);
        tv_desc_2 = (TextView) findViewById(R.id.tv_desc_2);
        tv_desc_3 = (TextView) findViewById(R.id.tv_desc_3);

        tv_care_1 = (TextView) findViewById(R.id.tv_care_1);
        tv_care_2 = (TextView) findViewById(R.id.tv_care_2);
        tv_care_3 = (TextView) findViewById(R.id.tv_care_3);

        img_mini_device_type = (ImageView) findViewById(R.id.img_mini_device_type);
        img_mini_device_type_1 = (ImageView) findViewById(R.id.img_mini_device_type_1);
        img_mini_device_type_2 = (ImageView) findViewById(R.id.img_mini_device_type_2);
        img_mini_device_type_3 = (ImageView) findViewById(R.id.img_mini_device_type_3);
        img_mini_device_type_4 = (ImageView) findViewById(R.id.img_mini_device_type_4);

//        tv_device_count_minus = (TextView) findViewById(R.id.tv_device_count_minus);
        tv_device_count_value = (NumberPicker) findViewById(R.id.tv_device_count_value);
        tv_device_count_value.setMinValue(0);
        tv_device_count_value.setMaxValue(20);
        tv_device_count_value.setWrapSelectorWheel(false);

//        tv_device_count_plus = (TextView) findViewById(R.id.tv_device_count_plus);

        et_rental_days = (NumberPicker) findViewById(R.id.et_rental_days);
        et_rental_days.setMinValue(0);
        et_rental_days.setMaxValue(20);
        et_rental_days.setWrapSelectorWheel(false);
//        et_rental_days.setText("0");

        devicesCart = new ArrayList<DeviceCartModel>();
    }

    private void getDeviceTypes() {

        //No need to query the server as the device types does not change very frequently.
        //If loaded the selected counts may get changes.
        if (ds.getDeviceTypesList() != null) {
            deviceTypesList.clear();
            deviceTypesList.addAll(ds.getDeviceTypesList());
            deviceTypesAdapter.notifyDataSetChanged();
            return;
        }

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getDeviceTypes(new RetroResponse<DeviceTypesModel>() {
            @Override
            public void onSuccess() {
                deviceTypesList.clear();
                ApplicationUtils.updateDefaultValueIfDeviceDescIsNull(models);
                deviceTypesList.addAll(ApplicationUtils.getNonSensorDeviceTypesForBuy(models));
                deviceTypesList.addAll(ApplicationUtils.getNonSensorDeviceTypesForRent(models));
                deviceTypesAdapter.notifyDataSetChanged();

                //Initialize the Cart
                ds.setDeviceTypesList(deviceTypesList);
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(BuyNowActivity.this, errorMsg);
            }
        });
    }

    class DeviceTypeClickListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            selectedMember = deviceTypesList.get(position);

            //Send track request to the member.
            displaySelectedDeviceDetails(selectedMember);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            return true;
        }
    }

    private void displaySelectedDeviceDetails(DeviceTypesModel selectedMember) {

        //Deselect the existing selected member

        //Select the new member
        tv_size_1.setText(ApplicationConstants.BUY_NOW_DESC_BULLET_PREFIX + String.valueOf(selectedMember.getSizes().get(0)));
        tv_size_2.setText(ApplicationConstants.BUY_NOW_DESC_BULLET_PREFIX + String.valueOf(selectedMember.getSizes().get(1)));
        tv_size_3.setText(ApplicationConstants.BUY_NOW_DESC_BULLET_PREFIX + String.valueOf(selectedMember.getSizes().get(2)));

        tv_desc_1.setText(ApplicationConstants.BUY_NOW_DESC_BULLET_PREFIX + selectedMember.getDescription().get(0));
        tv_desc_2.setText(ApplicationConstants.BUY_NOW_DESC_BULLET_PREFIX + selectedMember.getDescription().get(1));
        tv_desc_3.setText(ApplicationConstants.BUY_NOW_DESC_BULLET_PREFIX + selectedMember.getDescription().get(2));

        tv_care_1.setText(ApplicationConstants.BUY_NOW_DESC_BULLET_PREFIX + selectedMember.getCare().get(0));
        tv_care_2.setText(ApplicationConstants.BUY_NOW_DESC_BULLET_PREFIX + selectedMember.getCare().get(1));
        tv_care_3.setText(ApplicationConstants.BUY_NOW_DESC_BULLET_PREFIX + selectedMember.getCare().get(2));

        if (selectedMember.getType().equals(DeviceType.A9)) {
            img_mini_device_type.setImageResource(R.drawable.a9);
            img_mini_device_type_1.setImageResource(R.drawable.a9);
            img_mini_device_type_2.setImageResource(R.drawable.a9);
            img_mini_device_type_3.setImageResource(R.drawable.a9);
            img_mini_device_type_4.setImageResource(R.drawable.a9);
        } else if (selectedMember.getType().equals(DeviceType.V7)) {
            img_mini_device_type.setImageResource(R.drawable.v7);
            img_mini_device_type_1.setImageResource(R.drawable.v7);
            img_mini_device_type_2.setImageResource(R.drawable.v7);
            img_mini_device_type_3.setImageResource(R.drawable.v7);
            img_mini_device_type_4.setImageResource(R.drawable.v7);
        } else if (selectedMember.getType().equals(DeviceType.V16)) {
            img_mini_device_type.setImageResource(R.drawable.v16);
            img_mini_device_type_1.setImageResource(R.drawable.v16);
            img_mini_device_type_2.setImageResource(R.drawable.v16);
            img_mini_device_type_3.setImageResource(R.drawable.v16);
            img_mini_device_type_3.setImageResource(R.drawable.v16);
        } else if (selectedMember.getType().equals(DeviceType.MQTT)) {
            img_mini_device_type.setImageResource(R.drawable.accept);
            img_mini_device_type_1.setImageResource(R.drawable.accept);
            img_mini_device_type_2.setImageResource(R.drawable.accept);
            img_mini_device_type_3.setImageResource(R.drawable.accept);
            img_mini_device_type_4.setImageResource(R.drawable.accept);
        }

        tv_device_count_value.setValue(0);

        if (selectedMember.getOrderType() == DeviceOrderType.BUY) {
            et_rental_days.setEnabled(false);
        } else {
            et_rental_days.setValue(0);
            et_rental_days.setEnabled(true);
        }
    }

    public void onDeviceCountToCartButtonClick(View v) {

        if (selectedMember == null) {
            Toast.makeText(this, "Select the device", Toast.LENGTH_SHORT).show();
            return;
        }

        int deviceCount = tv_device_count_value.getValue();
        if (deviceCount <= 0) {
            Toast.makeText(this, "Select the count of device", Toast.LENGTH_SHORT).show();
            return;
        }

        int rentalDays = et_rental_days.getValue();

        if(selectedMember.getOrderType() == DeviceOrderType.RENT) {
            if (rentalDays <= 0) {
                Toast.makeText(this, "Rental days cannot be zero", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        deviceCount = tv_device_count_value.getValue();

        DeviceCartModel deviceCartModel = new DeviceCartModel();
        deviceCartModel.setInfo(selectedMember);
        deviceCartModel.setCount(deviceCount);
        deviceCartModel.setRentalDays(rentalDays);

        devicesCart.add(deviceCartModel);
        ds.getDevicesCart().add(deviceCartModel);

        Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
    }

    public void onCartContinueButtonClick(View v) {
        ds.setDeviceTypesList(deviceTypesList);
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }
}