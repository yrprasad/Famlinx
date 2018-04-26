package com.nglinx.pulse.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.InviteModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.adapter.SharingAdapter;

import com.nglinx.pulse.R;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.List;

public class SharingActivity extends AbstractActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    SharingAdapter sharingAdapter;

    List<InviteModel> pendingInvites;
    List<InviteModel> trackingMe;

    DataSession ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        ds = DataSession.getInstance();

        initializeIcons();

        getAllInvites();
    }

    private void getAllInvites() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);

        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllInvites(DataSession.getInstance().getUserModel().getId(), new RetroResponse<InviteModel>() {
            @Override
            public void onSuccess() {
                List<InviteModel> tmp = new ArrayList<InviteModel>();
                tmp.addAll(models);

                pendingInvites.clear();
                trackingMe.clear();
                pendingInvites.addAll(getInvitesByStatus(tmp, ApplicationConstants.SHARING_PENDING_INVITES_STATUS));
                trackingMe.addAll(getInvitesByStatus(tmp, ApplicationConstants.SHARING_TRACKING_ME_STATUS));
                ds.setPendingInvites(pendingInvites);
                ds.setTrackingMeInvites(trackingMe);
                sharingAdapter.notifyDataSetChanged();
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(SharingActivity.this, errorMsg);
            }
        });
    }

    protected void initializeIcons() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pendingInvites = new ArrayList<InviteModel>();
        trackingMe = new ArrayList<InviteModel>();
        ds.setPendingInvites(pendingInvites);
        ds.setTrackingMeInvites(trackingMe);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sharingAdapter = new SharingAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sharingAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private List<InviteModel> getInvitesByStatus(List<InviteModel> allInvites, int status) {
        List<InviteModel> filteredinvites = new ArrayList<>();

        for (InviteModel invite :
                allInvites) {
            if (invite.getStatus() == status) {
                filteredinvites.add(invite);
            }
        }

        return filteredinvites;
    }

    public void rejectInviteHandlerFromPendingInvites(View v) {
        rejectInvite(v);
    }

    public void acceptInviteHandler(View v) {
        final InviteModel iModel = (InviteModel) v.getTag();
        new AlertDialog.Builder(SharingActivity.this)
                .setTitle("Accept Invite")
                .setMessage("Do you want to accept invite from " + iModel.getEmail())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(SharingActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.accepInvite(iModel.getToId(), iModel.getUuid(), new RetroResponse<InviteModel>() {
                            @Override
                            public void onSuccess() {
                                getAllInvites();
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
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void rejectInvite(View v) {
        final InviteModel iModel = (InviteModel) v.getTag();
        new AlertDialog.Builder(SharingActivity.this)
                .setTitle("Reject Invite")
                .setMessage("Do you want to reject invite from " + iModel.getEmail())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(SharingActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.rejectInvite(iModel.getToId(), iModel.getUuid(), new RetroResponse<InviteModel>() {
                            @Override
                            public void onSuccess() {
                                getAllInvites();
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        SharingActivity.this);
                                builder.setTitle("Rejected invite from " + model.getEmail() + " successfully");
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
                                DialogUtils.diaplayDialog(SharingActivity.this, "Failed to reject invite from " + iModel.getEmail(), errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void rejectInviteHandlerFromAcceptedInvites(View v) {
        rejectInvite(v);
    }

    public void suspendInviteHandler(View v) {
        final InviteModel iModel = (InviteModel) v.getTag();
        new AlertDialog.Builder(SharingActivity.this)
                .setTitle("Suspend Tracking")
                .setMessage("Do you want to suspend tracking by " + iModel.getEmail())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(SharingActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(SharingActivity.this, RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.suspendInvite(iModel.getToId(), iModel.getUuid(), new RetroResponse<InviteModel>() {
                            @Override
                            public void onSuccess() {
                                getAllInvites();
                                mProgressDialog1.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        SharingActivity.this);
                                builder.setTitle("Suspended tracking by " + iModel.getEmail() + " successfully");
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
                                DialogUtils.diaplayDialog(SharingActivity.this, "Failed to suspend tracking by " + model.getEmail(), errorMsg);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void refreshList() {
        sharingAdapter.notifyDataSetChanged();
    }
}