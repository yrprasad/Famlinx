package com.nglinx.pulse.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wallet.Cart;
import com.nglinx.pulse.R;
import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.models.DeviceModel;
import com.nglinx.pulse.models.InviteModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.List;

public class AddAddressActivity extends AppCompatActivity {

    // Create the adapter to convert the array to views
    AutoCompleteTextView tv_location;
    AutoCompleteTextView tv_street;
    AutoCompleteTextView tv_landmark;

    DataSession ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initializeIcons();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    protected void initializeIcons() {

        ds = DataSession.getInstance();

        tv_location = (AutoCompleteTextView) findViewById(R.id.tv_location);
        tv_street = (AutoCompleteTextView) findViewById(R.id.tv_street);
        tv_landmark = (AutoCompleteTextView) findViewById(R.id.tv_landmark);
    }

    public void onAddAddressContinueClickHandler(View v) {

        if ((tv_location.getText() == null) ||
                (tv_location.getText().toString().trim().isEmpty()) ||
                (tv_street.getText() == null) ||
                (tv_street.getText().toString().trim().isEmpty()) ||
                (tv_landmark.getText() == null) ||
                (tv_location.getText().toString().trim().isEmpty())) {
            DialogUtils.diaplayErrorDialog(AddAddressActivity.this, "All fields are mandatory");
        } else {
            Toast.makeText(this, "This is under TBD", Toast.LENGTH_SHORT).show();
            /*new AlertDialog.Builder(AddAddressActivity.this)
                    .setTitle("Add Address")
                    .setMessage("Do you want to a add this address")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(SharingActivity.this);
                            ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                            apiEndpointInterface.accepInvite(iModel.getToId(), iModel.getUuid(), new RetroResponse<InviteModel>() {
                                @Override
                                public void onSuccess() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            SharingActivity.this);
                                    builder.setTitle("Accepted invite from " + iModel.getEmail() + " successfully");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    dialog.dismiss();
                                                    Log.e("info", "OK");
                                                }
                                            });
                                    builder.show();
                                }

                                @Override
                                public void onFailure() {
                                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                    DialogUtils.diaplayDialog(SharingActivity.this, "Accepted invite from " + iModel.getEmail() + " Failed", errorMsg);
                                }
                            });


                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();*/
        }
    }

    public void onAddAddressCancelClickHandler(View v) {
        Intent intent4 = new Intent(this, CartActivity.class);
        startActivity(intent4);
        finish();
    }
}