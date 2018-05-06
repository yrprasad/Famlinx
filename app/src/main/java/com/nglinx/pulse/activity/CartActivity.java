package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.DevicesCartAdapter;
import com.nglinx.pulse.adapter.NotificationsAdapter;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.DeviceTypesModel;
import com.nglinx.pulse.models.NotificationModel;
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

public class CartActivity extends AppCompatActivity {

    private ListView lv_cart;

    ArrayList<DeviceTypesModel> deviceTypesModelsList;
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
        deviceTypesModelsList = new ArrayList<>();
        deviceTypesModelsList.addAll(ds.getDeviceTypesList());
        adapter = new DevicesCartAdapter(CartActivity.this, deviceTypesModelsList);
        lv_cart.setAdapter(adapter);

        tv_total_cost = (TextView) findViewById(R.id.tv_total_cost);
        tv_total_pay = (TextView) findViewById(R.id.tv_total_pay);

        tv_selectedAddress = (TextView) findViewById(R.id.tv_selectedAddress);
        if (null != ds.getSelectedAddress())
            tv_selectedAddress.setText(ApplicationUtils.getAddress(ds.getSelectedAddress()));
    }

    public void onMinusClickCartHandler(View v, View countView, View costView) {
        final DeviceTypesModel selectedNotifModel = (DeviceTypesModel) v.getTag();

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
        final DeviceTypesModel selectedNotifModel = (DeviceTypesModel) v.getTag();
        Toast.makeText(this, "Plus Button Clicked for " + selectedNotifModel.getType(), Toast.LENGTH_SHORT).show();
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
        for (DeviceTypesModel model :
                ds.getDeviceTypesList()) {
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
        Toast.makeText(this, "This is under TBD", Toast.LENGTH_SHORT).show();
    }


}