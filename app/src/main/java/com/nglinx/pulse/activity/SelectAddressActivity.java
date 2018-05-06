package com.nglinx.pulse.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.SelectAddressAdapter;
import com.nglinx.pulse.models.AddressModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.TempUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectAddressActivity extends AppCompatActivity {

    // Create the adapter to convert the array to views
    private ListView lv_addresses;
    Button btn_selectAddress;

    DataSession ds;

    ArrayList<AddressModel> addresses;

    SelectAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        initializeIcons();
        getUserAddresses();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    protected void initializeIcons() {

        ds = DataSession.getInstance();

        lv_addresses = (ListView) findViewById(R.id.lv_addresses);
        btn_selectAddress = (Button) findViewById(R.id.btn_selectAddress);
        addresses = new ArrayList<>();
        adapter = new SelectAddressAdapter(getApplicationContext(), addresses);
        lv_addresses.setAdapter(adapter);

        lv_addresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AddressModel addressModel = addresses.get(position);
                ds.setSelectedAddress(addressModel);
            }
        });
    }


    private List<AddressModel> getUserAddresses() {

        addresses.clear();
        addresses.addAll(TempUtils.getUserDummyAddressList());
        adapter.notifyDataSetChanged();
        /*final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllNotifications(DataSession.getInstance().getUserModel().getId(), new RetroResponse<NotificationModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    notificationList.clear();
                    notificationList.addAll(models);
                    Collections.sort((List<NotificationModel>) notificationList);
                    adapter.notifyDataSetChanged();
                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                }
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(NotificationActivity.this, errorMsg);
            }
        });*/

        return addresses;
    }

    public void onSASelectClickHandler(View v) {

        if (null == ds.getSelectedAddress()) {
            Toast.makeText(this, "Select any address", Toast.LENGTH_SHORT).show();
        } else {
            AddressModel addressModel = (AddressModel) lv_addresses.getSelectedItem();
            ds.setSelectedAddress(addressModel);
            Intent intent4 = new Intent(this, CartActivity.class);
            startActivity(intent4);
            finish();
        }

    }

    public void onSACancelClickHandler(View v) {
        Intent intent4 = new Intent(this, CartActivity.class);
        startActivity(intent4);
        finish();
    }

}