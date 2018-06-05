package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nglinx.pulse.R;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.FenceModel;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.models.SettingsModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.List;

public class ApplyFenceActivity extends AppCompatActivity {

    EditText et_fencename;

    private Spinner spinner_groups;
    private Spinner spinner_groups_members;

    ArrayAdapter<GroupModel> groupAdapter;
    ArrayAdapter<GroupMemberModel> groupMemberAdapter;

    GroupModel selectedGroup;
    GroupMemberModel selectedGroupMember;

    List<GroupModel> groups = null;
    List<GroupMemberModel> groupMembers = null;

    Button btn_apply_fence, btn_cancel;

    DataSession ds;

    FenceModel selectedFence;

    SettingsModel settingsModel = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_apply_fence);

        ds = DataSession.getInstance();
        String selectedFenceId = getIntent().getExtras().getString(ApplicationConstants.SELECTED_FENCE);
        selectedFence = ds.getFenceById(selectedFenceId);

        initializeIcons();
    }


    protected void initializeIcons() {
        et_fencename = (EditText) findViewById(R.id.et_fencename);
        et_fencename.setText(selectedFence.getName());

        //Initialize the groups
        spinner_groups = (Spinner) findViewById(R.id.spinner_groups);

        btn_apply_fence = (Button) findViewById(R.id.btn_apply_fence);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        ds = DataSession.getInstance();
        groups = ds.getUserModel().getGroups();

        groupAdapter = new ArrayAdapter<GroupModel>(this, android.R.layout.simple_spinner_item, groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_groups.setAdapter(groupAdapter);

        //Initialize the dummy group members
        spinner_groups_members = (Spinner) findViewById(R.id.spinner_groups_members);

        groupMembers = new ArrayList<>();
        groupMemberAdapter = new ArrayAdapter<GroupMemberModel>(this, android.R.layout.simple_spinner_item, groupMembers);
        groupMemberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_groups_members.setAdapter(groupMemberAdapter);

        spinner_groups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGroup = (GroupModel) spinner_groups.getSelectedItem();
                selectedGroupMember = null;
                groupMembers = selectedGroup.getMembers();
                groupMemberAdapter.clear();
                groupMembers.add(ApplicationUtils.getEmptyGroupMemberAllMembers());
                groupMemberAdapter.addAll(groupMembers);
                groupMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_groups_members.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGroupMember = (GroupMemberModel) spinner_groups_members.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void callApplyFence(final FenceModel fenceModel, final GroupModel group, final GroupMemberModel member) {

        if(member.getId().equalsIgnoreCase("0"))
        {
            //This is a dummy group
            return;
        }

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(ApplyFenceActivity.this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getMemberSettings(ds.getUserModel().getId(), member.getId(), new RetroResponse<SettingsModel>() {
            @Override
            public void onSuccess() {
                settingsModel = model;

                //Update the with the new fence
                settingsModel.getFence().setEnabled(true);
                settingsModel.getFence().setId(fenceModel.getId());

                ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                apiEndpointInterface.updateMemberSettings(ds.getUserModel().getId(), member.getId(), settingsModel, new RetroResponse<SettingsModel>() {
                    @Override
                    public void onSuccess() {
                        ProgressbarUtil.stopProgressBar(mProgressDialog1);
                        DialogUtils.diaplaySuccessDialog(ApplyFenceActivity.this, "Successfully applied the fence");
                    }

                    @Override
                    public void onFailure() {
                        ProgressbarUtil.stopProgressBar(mProgressDialog1);
                        DialogUtils.diaplayDialog(ApplyFenceActivity.this, "Failed to apply the fence", errorMsg);
                    }
                });

                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayDialog(ApplyFenceActivity.this, "Failed to apply the fence", errorMsg);
            }
        });
    }

    public void applyFenceToMember(final FenceModel fence, final GroupModel group, final GroupMemberModel member) {

        new AlertDialog.Builder(ApplyFenceActivity.this)
                .setTitle("Add Fence")
                .setMessage("Do you want to apply fence to " + member.getName())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {
                        callApplyFence(fence, group, member);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void applyFenceToGroup(final FenceModel fence, final GroupModel group) {

        new AlertDialog.Builder(ApplyFenceActivity.this)
                .setTitle("Add Fence")
                .setMessage("Do you want to apply fence to members of group " + group.getName())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {
                        for (GroupMemberModel member :
                                group.getMembers()) {
                            callApplyFence(selectedFence, group, member);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void onApplyFenceClickHandler(View v) {

        selectedGroupMember = (GroupMemberModel) spinner_groups_members.getSelectedItem();

        selectedGroup = (GroupModel) spinner_groups.getSelectedItem();

        //if no group selected,  throw error Dialog
        if ((null == selectedGroup) && (null == selectedGroupMember)) {
            DialogUtils.diaplayErrorDialog(ApplyFenceActivity.this, "Select group and member");
            return;
        }

        //If both group and member selected, apply to that member
        if ((null != selectedGroup) && (null != selectedGroup) && (selectedGroupMember.getId().equalsIgnoreCase("0"))) {
            applyFenceToGroup(selectedFence, selectedGroup);
            return;
        }

        //If both group and member selected, apply to that member
        if ((null != selectedGroup) && (null != selectedGroupMember)) {
            applyFenceToMember(selectedFence, selectedGroup, selectedGroupMember);
            return;
        }

        //IF group selected and no member selected, apply fence to all members in group
        if ((null != selectedGroup) && (null == selectedGroupMember)) {
            applyFenceToGroup(selectedFence, selectedGroup);
            return;
        }

    }

    public void onCancelClickHandler(View v) {
        Intent intent7 = new Intent(this, FenceActivity.class);
        startActivity(intent7);
        finish();
    }
}