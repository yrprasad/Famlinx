package com.nglinx.pulse.activity;


import android.os.Bundle;

import com.nglinx.pulse.R;

public class SignupActivity extends AbstractActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_signup);
        initializeParent();
    }
}