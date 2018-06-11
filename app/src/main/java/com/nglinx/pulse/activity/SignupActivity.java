package com.nglinx.pulse.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.models.RegisterModel;
import com.nglinx.pulse.models.UserType;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.EmailValidator;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

public class SignupActivity extends AppCompatActivity {

    AutoCompleteTextView tv_email;
    AutoCompleteTextView tv_username;
    AutoCompleteTextView tv_phone;
    AutoCompleteTextView tv_name;
    AutoCompleteTextView tv_password;
    AutoCompleteTextView tv_retypePassword;
    CheckBox checkbox_terms;

    private String str_username, str_password, str_confirmpassword, str_name, str_email, str_phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_signup);

        initializeIcons();
    }

    protected void initializeIcons() {
        tv_email = (AutoCompleteTextView) findViewById(R.id.tv_email);
        tv_username = (AutoCompleteTextView) findViewById(R.id.tv_username);
        tv_phone = (AutoCompleteTextView) findViewById(R.id.tv_phone);
        tv_name = (AutoCompleteTextView) findViewById(R.id.tv_name);
        tv_password = (AutoCompleteTextView) findViewById(R.id.tv_password);
        tv_retypePassword = (AutoCompleteTextView) findViewById(R.id.tv_retypePassword);
        checkbox_terms  = (CheckBox) findViewById(R.id.checkbox_terms);
    }


    public void onCancelClick(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignUpClick(View v) {

        if(!checkbox_terms.isChecked())
        {
            DialogUtils.diaplayInfoDialog(SignupActivity.this, "Please accept the Terms and Conditions to Proceed");
            return;
        }

        str_username = tv_username.getText().toString().trim();
        str_password = tv_password.getText().toString().trim();
        str_confirmpassword = tv_retypePassword.getText().toString().trim();
        str_name = tv_name.getText().toString().trim();
        str_email = tv_email.getText().toString().trim();
        str_phone = tv_phone.getText().toString().trim();

        if ((str_username == null) || (str_username.isEmpty())) {
            DialogUtils.diaplayErrorDialog(SignupActivity.this, "All fields are mandatory");
            return;
        } else if ((str_password == null) || (str_password.isEmpty())) {
            DialogUtils.diaplayErrorDialog(SignupActivity.this, "All fields are mandatory");
            return;
        } else if ((str_confirmpassword == null) || (str_confirmpassword.isEmpty())) {
            DialogUtils.diaplayErrorDialog(SignupActivity.this, "All fields are mandatory");
            return;
        } else if ((str_name == null) || (str_name.isEmpty())) {
            DialogUtils.diaplayErrorDialog(SignupActivity.this, "All fields are mandatory");
            return;
        } else if ((str_email == null) || (str_email.isEmpty())) {
            DialogUtils.diaplayErrorDialog(SignupActivity.this, "All fields are mandatory");
            return;
        } else if ((str_phone == null) || (str_phone.isEmpty())) {
            DialogUtils.diaplayErrorDialog(SignupActivity.this, "All fields are mandatory");
            return;
        } else if (!str_password.equals(str_confirmpassword.toString())) {
            DialogUtils.diaplayErrorDialog(SignupActivity.this, "New Passwords does not match");
            return;
        }

        RegisterModel registerModel = new RegisterModel();
        registerModel.setUsername(str_username);
        registerModel.setPassword(str_password);
        registerModel.setName(str_name);
        registerModel.setEmail(str_email);
        registerModel.setPhone(str_phone);
        registerModel.setType(UserType.android);

        final ProgressDialog mProgressDialog = ProgressbarUtil.startProgressBar(this);

        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(getApplicationContext(), RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
        apiEndpointInterface.register(registerModel, new RetroResponse<RegisterModel>() {
            @Override
            public void onSuccess() {
                ProgressbarUtil.stopProgressBar(mProgressDialog);
                Toast.makeText(getApplicationContext(), "Registration Success. Check Email", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure() {
                ProgressbarUtil.stopProgressBar(mProgressDialog);
                Toast.makeText(getApplicationContext(), "Registration Failed." + errorMsg, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void onTermsAndConditionClick(View v) {
        Intent intent = new Intent(getApplicationContext(), TermsAndConditionsActivity.class);
        startActivity(intent);
        finish();
    }
}