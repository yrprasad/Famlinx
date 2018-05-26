package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.MemberProfileAdapter;
import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemberProfileActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView lv_member_profiles;

    List<ChildUserModel> childProfilesList;
    MemberProfileAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_member_profile);

        initializeParent();

        //Initialize all the icons on this screen and Navigation Menu screens
        initializeIcons();

        //Get the notifications list
        getAndPopulateMemberProfiles();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    @Override
    protected void initializeIcons() {

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        lv_member_profiles = (ListView) findViewById(R.id.lv_member_profiles);

        childProfilesList = new ArrayList<ChildUserModel>();
        adapter = new MemberProfileAdapter(MemberProfileActivity.this, (ArrayList<ChildUserModel>) childProfilesList);

        lv_member_profiles.setAdapter(adapter);
    }

    private List<ChildUserModel> getAndPopulateMemberProfiles() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllChildUser(DataSession.getInstance().getUserModel().getId(), new RetroResponse<ChildUserModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    childProfilesList.clear();
                    Collections.sort((List<ChildUserModel>) childProfilesList);
                    childProfilesList.addAll(models);
                    adapter.notifyDataSetChanged();
                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                }
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(MemberProfileActivity.this, errorMsg);
            }
        });

        return childProfilesList;
    }

    @Override
    public void onRefresh() {
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllChildUser(DataSession.getInstance().getUserModel().getId(), new RetroResponse<ChildUserModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    childProfilesList.clear();
                    Collections.sort((List<ChildUserModel>) childProfilesList);
                    childProfilesList.addAll(models);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure() {
                DialogUtils.diaplayFailureDialog(MemberProfileActivity.this, errorMsg);
            }
        });
    }

    public void deleteChildProfileClickHandler(View v) {
        final ChildUserModel iModel = (ChildUserModel) v.getTag();
        new AlertDialog.Builder(MemberProfileActivity.this)
                .setTitle("Delete Profile")
                .setMessage("Do you want to delete the profile ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MemberProfileActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.deleteChildUser(DataSession.getInstance().getUserModel().getId(), iModel.getId(), new RetroResponse<ChildUserModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        MemberProfileActivity.this);
                                builder.setTitle("Profile delete successfully");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.dismiss();
                                                Log.e("info", "OK");
                                                //TODO: Refresh
                                                swipeRefreshLayout.setRefreshing(true);
                                                // directly call onRefresh() method
                                                onRefresh();
                                            }
                                        });
                                builder.show();
                            }

                            @Override
                            public void onFailure() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                DialogUtils.diaplayDialog(MemberProfileActivity.this, "Failed to delete the Profile", errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void viewChildProfileClickHandler(View v) {
        Toast.makeText(this, "TBD", Toast.LENGTH_SHORT).show();
    }

    public void onItemClick(View view, int position, long id) {
        long viewId = view.getId();
        if (viewId == R.id.btn_profile_delete) {
            deleteChildProfileClickHandler(view);
        } else if (viewId == R.id.btn_profile_edit) {
            viewChildProfileClickHandler(view);
        }
    }

}