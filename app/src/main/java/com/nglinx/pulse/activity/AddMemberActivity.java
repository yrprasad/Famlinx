package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.AddMemberAdapter;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

public class AddMemberActivity extends AppCompatActivity {

    DataSession ds = null;
    Intent intent = null;
    AutoCompleteTextView membersAutoCompleteTextView;
    EditText nm_name_value, nm_username_value, nm_email_value, nm_phone_value, nm_usertype_value;
    Button nm_add_btn, nm_cancel_btn;
    UserModel selectedUserForAdd;
    AddMemberAdapter adapter;
    Toolbar inc_toolbar;

    public AddMemberActivity() {
        intent = new Intent();
        ds = DataSession.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        initializeIcons();

        membersAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUserForAdd = adapter.getItem(position);
                if (selectedUserForAdd != null) {
                    if (selectedUserForAdd.getName() != null)
                        nm_name_value.setText(selectedUserForAdd.getName());
                    if (selectedUserForAdd.getUsername() != null)
                        nm_username_value.setText(selectedUserForAdd.getUsername());
                    if (selectedUserForAdd.getEmail() != null)
                        nm_email_value.setText(selectedUserForAdd.getEmail());
                    if (selectedUserForAdd.getType() != null)
                        nm_usertype_value.setText(selectedUserForAdd.getType().toString());
                    if (selectedUserForAdd.getPhone() != null)
                        nm_phone_value.setText(selectedUserForAdd.getPhone());
                }
            }
        });

        inc_toolbar = (Toolbar) findViewById(R.id.inc_toolbar);

        Button btn_toolbar_cancel = (Button) inc_toolbar.findViewById(R.id.btn_toolbar_cancel);
        btn_toolbar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }


    //Initialize all the icons on this screen and Navigation Menu screens
    protected void initializeIcons() {
        membersAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.membersAutoCompleteTextView);
        adapter = new AddMemberAdapter(AddMemberActivity.this, android.R.layout.simple_dropdown_item_1line);

        membersAutoCompleteTextView.setThreshold(3);
        membersAutoCompleteTextView.setAdapter(adapter);

        nm_name_value = (EditText) findViewById(R.id.et_name);
        nm_username_value = (EditText) findViewById(R.id.et_parent_name);
        nm_email_value = (EditText) findViewById(R.id.et_email);
        nm_phone_value = (EditText) findViewById(R.id.et_phone);
        nm_usertype_value = (EditText) findViewById(R.id.et_user_type);
        nm_add_btn = (Button) findViewById(R.id.btn_add);
        nm_cancel_btn = (Button) findViewById(R.id.btn_cancel);
    }

    public void addMemberClickHandler(View v) {
        if (selectedUserForAdd == null) {
            Toast.makeText(AddMemberActivity.this, "User not selected", Toast.LENGTH_SHORT).show();
            return;
        }

        String groupId = null;
        String groupName = null;
        if ((null == ds.getSelected_group_id()) || (ds.getSelected_group_id().equalsIgnoreCase(""))) {
            groupId = ds.getDefaultGroupId();
            groupName = ds.getDefaultGroup().getName();
        } else {
            groupId = ds.getSelected_group_id();
            groupName = ds.getSelected_group_name();
        }

        final String fGroupId = groupId;
        final String fGroupName = groupName;

        new AlertDialog.Builder(AddMemberActivity.this)
                .setTitle("Add Member")
                .setMessage("Do you want to add member to group " + fGroupName)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        GroupMemberModel memberModel = new GroupMemberModel();
                        memberModel.setName(selectedUserForAdd.getName());
                        memberModel.setUsername(selectedUserForAdd.getUsername());

                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(AddMemberActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.inviteMembers(ds.getUserModel().getId(), fGroupId, memberModel, new RetroResponse<GroupMemberModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                if ((errorMsg == null) || (errorMsg.trim().isEmpty())) {
                                    errorMsg = "Server Error";
                                }
                                DialogUtils.diaplayDialog(AddMemberActivity.this, "Failure to add group member", errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void cancelClickHandler(View v) {
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
    }

}