package com.nglinx.pulse.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nglinx.pulse.R;

public class NotificationsDetailActivity extends Activity {

    TextView notif_detail_date, notif_detail_message;

    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_detail);

        notif_detail_date = (TextView) findViewById(R.id.notif_detail_date);
        notif_detail_message = (TextView) findViewById(R.id.notif_detail_message);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            notif_detail_date.setText(mBundle.getString("NotifDateBundle"));
            notif_detail_message.setText(mBundle.getString("NotifTextBundle"));
        }
    }
}