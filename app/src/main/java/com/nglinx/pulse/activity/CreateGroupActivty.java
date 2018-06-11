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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.NotificationsAdapter;
import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.models.NotificationModel;
import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateGroupActivty extends AppCompatActivity {

    private ListView notifications_lv;

    EditText et_group_name;
    Button btn_create_group, btn_cancel;
    DataSession ds;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_create_group);

        //Initialize all the icons on this screen and Navigation Menu screens
        initializeIcons();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    protected void initializeIcons() {
        et_group_name = (EditText) findViewById(R.id.et_group_name);
        btn_create_group = (Button) findViewById(R.id.btn_create_group);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        ds = DataSession.getInstance();
    }

    public void createGroupClickHandler(View v) {

        String newGroupName = et_group_name.getText().toString().trim();

        if (newGroupName.isEmpty() || newGroupName.length() == 0 || newGroupName.equals("") || newGroupName == null) {
            DialogUtils.diaplayDialog(CreateGroupActivty.this, "Error", "Enter Group Name");
        } else {
            CreateGroupApi(newGroupName);
        }
    }

    private void CreateGroupApi(final String groupName) {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);

        GroupModel groupModel = new GroupModel();
        groupModel.setName(groupName);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.createGroup(ds.getUserModel().getId(), groupModel, new RetroResponse<GroupModel>() {
            @Override
            public void onSuccess() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayDialog(CreateGroupActivty.this, "Success", "Created the group Successfully");
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);

                if ((errorMsg == null) || (errorMsg.trim().isEmpty())) {
                    errorMsg = "Server Error";
                }
                DialogUtils.diaplayDialog(CreateGroupActivty.this, "Error", "Failure to create a group." + errorMsg);
            }
        });
    }

    public void cancelGroupClickHandler(View v) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}