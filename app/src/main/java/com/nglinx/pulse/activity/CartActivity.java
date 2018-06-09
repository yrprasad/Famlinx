package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.DevicesCartAdapter;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.DeviceOrderModel;
import com.nglinx.pulse.models.DeviceOrderType;
import com.nglinx.pulse.models.DeviceType;
import com.nglinx.pulse.models.DeviceCartModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private ListView lv_cart;

    ArrayList<DeviceCartModel> deviceCartModelsList;
    DevicesCartAdapter adapter;

    DataSession ds;

    TextView tv_total_cost, tv_total_pay;

    TextView tv_selectedAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_cart);

        //Initialize all the icons on this screen and Navigation Menu screens
        initializeIcons();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    protected void initializeIcons() {
        ds = DataSession.getInstance();
        lv_cart = (ListView) findViewById(R.id.lv_cart);
        deviceCartModelsList = new ArrayList<>();
        deviceCartModelsList.addAll(ds.getDevicesCart());
        adapter = new DevicesCartAdapter(CartActivity.this, deviceCartModelsList);
        lv_cart.setAdapter(adapter);

        tv_total_cost = (TextView) findViewById(R.id.tv_total_cost);
        tv_total_pay = (TextView) findViewById(R.id.tv_total_pay);

        tv_selectedAddress = (TextView) findViewById(R.id.tv_selectedAddress);
        if (null != ds.getSelectedAddress())
            tv_selectedAddress.setText(ApplicationUtils.getAddress(ds.getSelectedAddress()));

        int totalCost = getCartTotalCost();
        int totalPayable = getCartTotalPayable(totalCost);

        tv_total_cost.setText(String.valueOf(totalCost));
        tv_total_pay.setText(String.valueOf(totalPayable));

//        getAvailableDeviceCount();
    }

    public void onMinusClickCartHandler(View v, View countView, View costView) {
        final DeviceCartModel selectedNotifModel = (DeviceCartModel) v.getTag();

        if (selectedNotifModel.getCount() > 0) {
            selectedNotifModel.setCount(selectedNotifModel.getCount() - 1);
            ((EditText) countView).setText(String.valueOf(selectedNotifModel.getCount()));
            ((TextView) costView).setText(String.valueOf(selectedNotifModel.getCount() + selectedNotifModel.getCost()));

            int totalCost = getCartTotalCost();
            int totalPayable = getCartTotalPayable(totalCost);
            tv_total_cost.setText(String.valueOf(totalCost));
            tv_total_pay.setText(String.valueOf(totalPayable));
        }

    }

    public void onPlusClickCartHandler(View v, View countView, View costView) {
        final DeviceCartModel selectedNotifModel = (DeviceCartModel) v.getTag();
        selectedNotifModel.setCount(selectedNotifModel.getCount() + 1);
        ((EditText) countView).setText(String.valueOf(selectedNotifModel.getCount()));
        ((TextView) costView).setText(String.valueOf(selectedNotifModel.getCount() + selectedNotifModel.getCost()));

        int totalCost = getCartTotalCost();
        int totalPayabale = getCartTotalPayable(totalCost);
        tv_total_cost.setText(String.valueOf(totalCost));
        tv_total_pay.setText(String.valueOf(totalPayabale));
    }

    public void onItemClick(View view, View countView, View costView, int position, long id) {
        long viewId = view.getId();
        if (viewId == R.id.tv_cart_minus) {
            onMinusClickCartHandler(view, countView, costView);
        } else if (viewId == R.id.tv_cart_plus) {
            onPlusClickCartHandler(view, countView, costView);
        }
    }

    private int getCartTotalCost() {
        int totalCost = 0;
        for (DeviceCartModel model :
                ds.getDevicesCart()) {
            totalCost += model.getCost() * model.getCount();
        }
        return totalCost;
    }

    private int getCartTotalPayable(int totalCost) {
        return totalCost + ApplicationConstants.SHIPPING_COST;
    }

    public void onAddAddressCancelClickHandler(View v) {
        Intent intent4 = new Intent(this, AddAddressActivity.class);
        startActivity(intent4);
        finish();
    }

    public void onCartSelectAddressButtonClick(View v) {
        Intent intent4 = new Intent(this, SelectAddressActivity.class);
        startActivity(intent4);
        finish();
    }


    public void onCartAddAddressButtonClick(View v) {
        Intent intent4 = new Intent(this, AddAddressActivity.class);
        startActivity(intent4);
        finish();
    }

    public void onCartOrderButtonClick(View v) {

        if ((ds.getSelectedAddress() == null)) {
            //Check if address selected
            DialogUtils.diaplayInfoDialog(CartActivity.this, "Please select the address");
        } else {
            //Check if the required devices are present
//            String orderErrorString = checkIfRequiredDevicesAvailable();
//            if ((orderErrorString == null) || (orderErrorString.isEmpty())) {
            final int deviceOrderCount = ApplicationUtils.getTotalOrderCount(deviceCartModelsList);
            if (deviceOrderCount > 0) {
                placeOrder(deviceOrderCount);
            } else {
                DialogUtils.diaplayInfoDialog(CartActivity.this, "Select device to order");
            }
            /*} else {
                orderErrorString += "Please modify the order accordingly";
                DialogUtils.diaplayInfoDialog(CartActivity.this, orderErrorString);
            }*/
        }
    }


    //TODO: Need a API for future to order multiple devices at one shot.
    private void placeOrder(final int deviceOrderCount) {


        final List<Integer> successDeviceOrderSummary = new ArrayList<>();
        successDeviceOrderSummary.add(0, 0);

        //Use only the first index as we will be unable to update the non final Integer inside the api call.
        final List<Integer> failureDeviceOrderCount = new ArrayList<>();
        failureDeviceOrderCount.add(0, 0);

        final ProgressDialog mProgressDialog = ProgressbarUtil.getProgressBar(CartActivity.this, "Placing Order");

        new AlertDialog.Builder(CartActivity.this)
                .setTitle("ORDER")
                .setMessage("Confirm Order ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        ProgressbarUtil.startProgressBar(mProgressDialog);

                        for (DeviceCartModel orderedDevice :
                                deviceCartModelsList) {

                            for (int i = 0; i < orderedDevice.getCount(); i++) {
                                ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);

                                if (orderedDevice.getOrderType() == DeviceOrderType.BUY) {
                                    apiEndpointInterface.createDeviceOrder(orderedDevice.getType(), new DeviceOrderModel(), new RetroResponse<DeviceOrderModel>() {
                                        @Override
                                        public void onSuccess() {

                                            //Increment the success counter
                                            successDeviceOrderSummary.set(0, successDeviceOrderSummary.get(0) + 1);

                                            synchronized (this) {
                                                int totalOrderReponses = 0;
                                                totalOrderReponses += successDeviceOrderSummary.get(0).intValue();
                                                totalOrderReponses += failureDeviceOrderCount.get(0).intValue();
                                                if ((deviceOrderCount <= totalOrderReponses)) {
                                                    ProgressbarUtil.stopProgressBar(mProgressDialog);
                                                    //All responses are received and last response is Success
                                                    if (failureDeviceOrderCount.get(0) > 0) {
                                                        //Failure Case
                                                        Intent intent4 = new Intent(getApplicationContext(), OrderFailedActivity.class);
                                                        intent4.putExtra(ApplicationConstants.FAILED_ITEM_COUNT_STR, (deviceOrderCount - totalOrderReponses));
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ORDER_COST_STR, Integer.parseInt(tv_total_pay.getText().toString()));
                                                        startActivity(intent4);
                                                        finish();
                                                    } else {
                                                        //Success Case
                                                        Intent intent4 = new Intent(getApplicationContext(), OrderSuccessActivity.class);
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ITEM_COUNT_STR, totalOrderReponses);
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ORDER_COST_STR, Integer.parseInt(tv_total_pay.getText().toString()));
                                                        startActivity(intent4);
                                                        finish();
                                                    }

                                                }
                                            }

                                        }

                                        @Override
                                        public void onFailure() {

                                            //Increment the Failure counter.

                                            failureDeviceOrderCount.set(0, failureDeviceOrderCount.get(0) + 1);
                                            synchronized (this) {

                                                int totalOrderReponses = 0;
                                                totalOrderReponses += successDeviceOrderSummary.get(0).intValue();
                                                totalOrderReponses += failureDeviceOrderCount.get(0).intValue();
                                                if ((deviceOrderCount <= totalOrderReponses)) {
                                                    ProgressbarUtil.stopProgressBar(mProgressDialog);

                                                    //All responses are received and last response is Failure
                                                    if (failureDeviceOrderCount.get(0) > 0) {
                                                        //Failure Case
                                                        Intent intent4 = new Intent(getApplicationContext(), OrderFailedActivity.class);
                                                        intent4.putExtra(ApplicationConstants.FAILED_ITEM_COUNT_STR, (deviceOrderCount - totalOrderReponses));
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ORDER_COST_STR, Integer.parseInt(tv_total_pay.getText().toString()));
                                                        startActivity(intent4);
                                                        finish();
                                                    } else {
                                                        //Success Case
                                                        Intent intent4 = new Intent(getApplicationContext(), OrderSuccessActivity.class);
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ITEM_COUNT_STR, totalOrderReponses);
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ORDER_COST_STR, Integer.parseInt(tv_total_pay.getText().toString()));
                                                        startActivity(intent4);
                                                        finish();
                                                    }

                                                }
                                            }
                                        }
                                    });
                                } else {
                                    apiEndpointInterface.rentDeviceOrder(orderedDevice.getType(), orderedDevice.getRentalDays(), new DeviceOrderModel(), new RetroResponse<DeviceOrderModel>() {
                                        @Override
                                        public void onSuccess() {

                                            //Increment the success counter
                                            successDeviceOrderSummary.set(0, successDeviceOrderSummary.get(0) + 1);

                                            synchronized (this) {
                                                int totalOrderReponses = 0;
                                                totalOrderReponses += successDeviceOrderSummary.get(0).intValue();
                                                totalOrderReponses += failureDeviceOrderCount.get(0).intValue();
                                                if ((deviceOrderCount <= totalOrderReponses)) {
                                                    ProgressbarUtil.stopProgressBar(mProgressDialog);
                                                    //All responses are received and last response is Success
                                                    if (failureDeviceOrderCount.get(0) > 0) {
                                                        //Failure Case
                                                        Intent intent4 = new Intent(getApplicationContext(), OrderFailedActivity.class);
                                                        intent4.putExtra(ApplicationConstants.FAILED_ITEM_COUNT_STR, (deviceOrderCount - totalOrderReponses));
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ORDER_COST_STR, Integer.parseInt(tv_total_pay.getText().toString()));
                                                        startActivity(intent4);
                                                        finish();
                                                    } else {
                                                        //Success Case
                                                        Intent intent4 = new Intent(getApplicationContext(), OrderSuccessActivity.class);
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ITEM_COUNT_STR, totalOrderReponses);
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ORDER_COST_STR, Integer.parseInt(tv_total_pay.getText().toString()));
                                                        startActivity(intent4);
                                                        finish();
                                                    }

                                                }
                                            }

                                        }

                                        @Override
                                        public void onFailure() {
                                            //Increment the Failure counter.
                                            failureDeviceOrderCount.set(0, failureDeviceOrderCount.get(0) + 1);
                                            synchronized (this) {

                                                int totalOrderReponses = 0;
                                                totalOrderReponses += successDeviceOrderSummary.get(0).intValue();
                                                totalOrderReponses += failureDeviceOrderCount.get(0).intValue();
                                                if ((deviceOrderCount <= totalOrderReponses)) {
                                                    ProgressbarUtil.stopProgressBar(mProgressDialog);

                                                    //All responses are received and last response is Failure
                                                    if (failureDeviceOrderCount.get(0) > 0) {
                                                        //Failure Case
                                                        Intent intent4 = new Intent(getApplicationContext(), OrderFailedActivity.class);
                                                        intent4.putExtra(ApplicationConstants.FAILED_ITEM_COUNT_STR, (deviceOrderCount - totalOrderReponses));
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ORDER_COST_STR, Integer.parseInt(tv_total_pay.getText().toString()));
                                                        startActivity(intent4);
                                                        finish();
                                                    } else {
                                                        //Success Case
                                                        Intent intent4 = new Intent(getApplicationContext(), OrderSuccessActivity.class);
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ITEM_COUNT_STR, totalOrderReponses);
                                                        intent4.putExtra(ApplicationConstants.TOTAL_ORDER_COST_STR, Integer.parseInt(tv_total_pay.getText().toString()));
                                                        startActivity(intent4);
                                                        finish();
                                                    }

                                                }
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    /*private String checkIfRequiredDevicesAvailable() {

        String orderErrorString = "";

        //Available devices list structure
        Map<DeviceType, Integer> availableDevices = ds.getAvailableDevices();

        //Order count list Structure
        for (DeviceCartModel orderedDevice :
                deviceCartModelsList) {
            if (orderedDevice.getCount() > availableDevices.get(orderedDevice.getType()).intValue()) {
                //This device is not available in store. Decrease the count.
                orderErrorString += "Device type " + orderedDevice.getType() +
                        " count is " + availableDevices.get(orderedDevice.getType()).intValue() + " in inventory." + "\n";
            }
        }

        return orderErrorString;
    }*/

    /*//TODO : Future. Need a new api to get the count of available devices to manage the order.
    //TODO: This may not work for the non admins.
    public void getAvailableDeviceCount() {
        final ArrayList<DeviceModel> devicesList = new ArrayList<>();

        //Get all the devices and calculate the available devices count
        final ProgressDialog mProgressDialog = ProgressbarUtil.startProgressBar(CartActivity.this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listAllDevices(new RetroResponse<DeviceModel>() {
            @Override
            public void onSuccess() {
                ProgressbarUtil.stopProgressBar(mProgressDialog);
                devicesList.clear();
                devicesList.addAll(models);
                //Get the available devices.
                ds.setAvailableDevices(ApplicationUtils.getAvailableDeviceCount(devicesList));
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog);
                DialogUtils.diaplayErrorDialog(CartActivity.this, errorMsg);
            }
        });
    }*/

}