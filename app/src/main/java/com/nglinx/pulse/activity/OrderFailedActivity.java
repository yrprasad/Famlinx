package com.nglinx.pulse.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.DialogUtils;

import java.util.Date;

public class OrderFailedActivity extends AppCompatActivity {

    // Create the adapter to convert the array to views
    TextView tv_failed_count_item;
    TextView tv_order_number;
    TextView tv_order_date;
    TextView tv_address;
    TextView tv_order_value;

    DataSession ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_failed);
        initializeIcons();
        displayData();
        ds.clearDeviceTypes();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    protected void initializeIcons() {

        ds = DataSession.getInstance();

        tv_failed_count_item = (TextView) findViewById(R.id.tv_failed_count_item);
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        tv_order_date = (TextView) findViewById(R.id.tv_order_date);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_order_value = (TextView) findViewById(R.id.tv_order_value);
    }

    public void displayData() {
        int faileItemCount = getIntent().getExtras().getInt(ApplicationConstants.FAILED_ITEM_COUNT_STR);
        tv_failed_count_item.setText(String.valueOf(faileItemCount));
        tv_order_number.setText("TBD");
        tv_order_date.setText(new Date().toString());
        tv_address.setText(ApplicationUtils.getAddress(ds.getSelectedAddress()));

        int order_value = getIntent().getExtras().getInt(ApplicationConstants.TOTAL_ORDER_COST_STR);
        tv_order_value.setText(String.valueOf(order_value));
    }

    public void OnRetryOrderClickHandler(View v) {
        Intent intent4 = new Intent(this, BuyNowActivity.class);
        startActivity(intent4);
    }

    public void OnGoToHomeOrderClickHandler(View v) {
        Intent intent4 = new Intent(this, HomeActivity.class);
        startActivity(intent4);
    }
}