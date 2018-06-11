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
import com.nglinx.pulse.adapter.NotificationsAdapter;
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

public class NotificationActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView notifications_lv;

    List<NotificationModel> notificationList;
    NotificationsAdapter adapter;

    NotificationItemClickListener notificationItemClickListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_notification);

        initializeParent();

        //Initialize all the icons on this screen and Navigation Menu screens
        initializeIcons();

        //Get the notifications list
        getAndPopulateNotifications();
    }

    //Initialize all the icons on this screen and Navigation Menu screens
    @Override
    protected void initializeIcons() {

        notificationItemClickListener = new NotificationItemClickListener();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        notifications_lv = (ListView) findViewById(R.id.lv_notifications);

        notificationList = new ArrayList<NotificationModel>();
        adapter = new NotificationsAdapter(NotificationActivity.this, (ArrayList<NotificationModel>) notificationList);

        notifications_lv.setAdapter(adapter);

        notifications_lv.setOnItemClickListener(notificationItemClickListener);
    }

    private List<NotificationModel> getAndPopulateNotifications() {

        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(this);
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllNotifications(DataSession.getInstance().getUserModel().getId(), new RetroResponse<NotificationModel>() {
            @Override
            public void onSuccess() {
                notificationList.clear();
                notificationList.addAll(models);
                Collections.sort((List<NotificationModel>) notificationList);
                adapter.notifyDataSetChanged();
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                DialogUtils.diaplayFailureDialog(NotificationActivity.this, errorMsg);
            }
        });

        return notificationList;
    }

    @Override
    public void onRefresh() {
        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.getAllNotifications(DataSession.getInstance().getUserModel().getId(), new RetroResponse<NotificationModel>() {
            @Override
            public void onSuccess() {
                notificationList.clear();
                notificationList.addAll(models);
                Collections.sort((List<NotificationModel>) notificationList);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure() {
                DialogUtils.diaplayFailureDialog(NotificationActivity.this, errorMsg);
            }
        });
    }

    class NotificationItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getApplicationContext(), NotificationsDetailActivity.class);
            NotificationModel selectedNotifModel = (NotificationModel) notifications_lv.getItemAtPosition(i);
            intent.putExtra("NotifDateBundle", selectedNotifModel.getType().toString() + " : " + selectedNotifModel.getCreatedDate());
            intent.putExtra("NotifTextBundle", selectedNotifModel.getMessage());
            startActivity(intent);
        }
    }

    public void deleteNotificationClickHandler(View v) {
        final NotificationModel iModel = (NotificationModel) v.getTag();
        new AlertDialog.Builder(NotificationActivity.this)
                .setTitle("Accept Invite")
                .setMessage("Do you want to delete this Notification ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final ProgressDialog mProgressDialog1 = ProgressbarUtil.startProgressBar(NotificationActivity.this);
                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);

                        //TODO:Need to change the parameters order in the backend
                        apiEndpointInterface.deleteNotification(iModel.getId(), DataSession.getInstance().getUserModel().getId(), new RetroResponse<NotificationModel>() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        NotificationActivity.this);
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
                                                onRefresh();
                                            }
                                        });
                                builder.show();
                            }

                            @Override
                            public void onFailure() {
                                ProgressbarUtil.stopProgressBar(mProgressDialog1);
                                DialogUtils.diaplayDialog(NotificationActivity.this, "Failed to delete the Notification", errorMsg);
                            }
                        });


                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void viewNotificationClickHandler(View v) {
        Intent intent = new Intent(getApplicationContext(), NotificationsDetailActivity.class);
        final NotificationModel selectedNotifModel = (NotificationModel) v.getTag();
        intent.putExtra("NotifDateBundle", selectedNotifModel.getType().toString() + " : " + selectedNotifModel.getCreatedDate());
        intent.putExtra("NotifTextBundle", selectedNotifModel.getMessage());
        startActivity(intent);
    }

   /* public void onProfileItemClick(View view, int position, long id) {
        long viewId = view.getId();
        if (viewId == R.id.iv_delete_notification) {
            deleteNotificationClickHandler(view);
        } else if (viewId == R.id.iv_view_notification) {
            viewNotificationClickHandler(view);
        }
    }*/

}