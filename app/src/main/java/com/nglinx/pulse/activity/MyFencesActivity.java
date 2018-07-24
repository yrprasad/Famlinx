package com.nglinx.pulse.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.adapter.MyFencesAdapter;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.FenceModel;
import com.nglinx.pulse.models.NotificationModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyFencesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView lv_fence;

    List<FenceModel> fenceList;
    MyFencesAdapter adapter;

//    FenceItemClickListener fenceItemClickListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    DataSession ds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_my_fence);

        ds = DataSession.getInstance();

        //Initialize all the icons on this screen and Navigation Menu screens
        initializeIcons();

        //Get the fences list
        getAndPopulateFences();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    protected void initializeIcons() {

//        fenceItemClickListener = new FenceItemClickListener();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        lv_fence = (ListView) findViewById(R.id.fence_lv);

        fenceList = new ArrayList<FenceModel>();
        adapter = new MyFencesAdapter(MyFencesActivity.this, (ArrayList<FenceModel>) fenceList);

        lv_fence.setAdapter(adapter);

//        lv_fence.setOnItemClickListener(fenceItemClickListener);
    }

    private List<FenceModel> getAndPopulateFences() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listFence(DataSession.getInstance().getUserModel().getId(), new RetroResponse<FenceModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    fenceList.clear();
                    fenceList.addAll(models);
                    Collections.sort((List<FenceModel>) fenceList);
                    adapter.notifyDataSetChanged();
                    ProgressbarUtil.stopProgressBar(mProgressDialog1);
                }
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(MyFencesActivity.this, errorMsg);
            }
        });

        return fenceList;
    }

    @Override
    public void onRefresh() {
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.listFence(DataSession.getInstance().getUserModel().getId(), new RetroResponse<FenceModel>() {
            @Override
            public void onSuccess() {
                if ((models != null) && (models.size() > 0)) {
                    fenceList.clear();
                    fenceList.addAll(models);
                    Collections.sort((List<FenceModel>) fenceList);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure() {
                DialogUtils.diaplayFailureDialog(MyFencesActivity.this, errorMsg);
            }
        });
    }

    public void editFenceClickHandler(View view) {
        final FenceModel fenceModel = (FenceModel) view.getTag();
        Toast.makeText(this, "TBD to edit " + fenceModel.getName(), Toast.LENGTH_SHORT).show();
    }

    public void applyFenceClickHandler(View view) {
        final FenceModel fenceModel = (FenceModel) view.getTag();

        new AlertDialog.Builder(MyFencesActivity.this)
                .setTitle("Add Fence")
                .setMessage("Do you want to apply fence " + fenceModel.getName() + " to users?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int whichButton) {
                        Intent intent7 = new Intent(getApplicationContext(), ApplyFenceActivity.class);
                        intent7.putExtra(ApplicationConstants.SELECTED_FENCE, fenceModel.getId());
                        startActivityForResult(intent7, ApplicationConstants.ACTIVITY_APPLY_FENCE);
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    public void deleteFenceClickHandler(View view) {
        final FenceModel iModel = (FenceModel) view.getTag();
        new AlertDialog.Builder(MyFencesActivity.this)
                .setTitle("Accept Invite")
                .setMessage("Do you want to delete this fence ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(MyFencesActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.deleteFence(DataSession.getInstance().getUserModel().getId(), iModel.getId(), new RetroResponse<FenceModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        MyFencesActivity.this);
                                builder.setTitle("Notification delete successfully");
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
                                DialogUtils.diaplayDialog(MyFencesActivity.this, "Failed to delete the Notification", errorMsg);
                            }
                        });


                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}