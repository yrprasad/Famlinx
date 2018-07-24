package com.nglinx.pulse.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nglinx.pulse.R;

public class NotificationsDetailActivity extends Activity {

    TextView notif_detail_date, notif_detail_message;

    ImageButton backButton;
    Intent intent;

    public NotificationsDetailActivity() {
        intent = new Intent();
    }

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

        Toolbar inc_toolbar;

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

    @Override
    public void onBackPressed() {
    }
}