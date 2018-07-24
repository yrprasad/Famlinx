package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.MyOrdersAdapter;
import com.nglinx.pulse.adapter.NotificationsAdapter;
import com.nglinx.pulse.models.DeviceOrderModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyOrdersActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView lv_my_orders;

    List<DeviceOrderModel> myOrdersList;
    MyOrdersAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_my_account_order);

        initializeParent();

        //Initialize all the icons on this screen and Navigation Menu screens
        initializeIcons();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    @Override
    protected void initializeIcons() {

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        lv_my_orders = (ListView) findViewById(R.id.lv_my_orders);

        myOrdersList = new ArrayList<DeviceOrderModel>();
        adapter = new MyOrdersAdapter(MyOrdersActivity.this, (ArrayList<DeviceOrderModel>) myOrdersList);

        lv_my_orders.setAdapter(adapter);

        getMyOrders();
    }

    private List<DeviceOrderModel> getMyOrders() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listMyDeviceOrder(new RetroResponse<DeviceOrderModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    myOrdersList.clear();
                    myOrdersList.addAll(models);
                    Collections.sort(myOrdersList);
                    adapter.notifyDataSetChanged();
                }
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(MyOrdersActivity.this, errorMsg);
            }
        });

        return myOrdersList;
    }

    @Override
    public void onRefresh() {
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listMyDeviceOrder(new RetroResponse<DeviceOrderModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    myOrdersList.clear();
                    myOrdersList.addAll(models);
                    Collections.sort(myOrdersList);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure() {
                DialogUtils.diaplayFailureDialog(MyOrdersActivity.this, errorMsg);
            }
        });
    }
}