package com.nglinx.pulse.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.models.ResponseDto;
import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.session.SharedPrefUtility;
import com.nglinx.pulse.utils.ProgressbarUtil;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AbstractActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public String classTag;
    NavigationView navigationView;
    public DrawerLayout drawerLayout;
    DataSession ds;
    ApiEndpointInterface restEndpoint;

    TextView tv_username;
    TextView tv_email;
    Toolbar inc_toolbar;
    View headerLayout;

    public void initializeParent() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        ds = DataSession.getInstance();
        restEndpoint = RetroUtils.getApiEndPoint(getApplicationContext());
        headerLayout = navigationView.getHeaderView(0);

        inc_toolbar = (Toolbar) findViewById(R.id.inc_toolbar);

        TextView tv_username = headerLayout.findViewById(R.id.tv_username);
        tv_username.setText(ds.getUserModel().getName());
        TextView tv_email = headerLayout.findViewById(R.id.tv_email);
        tv_email.setText(ds.getUserModel().getEmail());

        ImageView iv = (ImageView)inc_toolbar.findViewById(R.id.menu_toolbar);
        iv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        RelativeLayout menu_toolbar_layout = (RelativeLayout)inc_toolbar.findViewById(R.id.menu_toolbar_layout);
        menu_toolbar_layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        //Closing drawer on item click
        drawerLayout.closeDrawers();

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                //IF the home option is explicitly selected, clear the selected group and selected member
                ds.clearSelectedGroupMember();
                ds.clearSelectedGroup();
                SharedPrefUtility.clearSelectedGroup(getApplicationContext());
                SharedPrefUtility.clearSelectedGroupMember(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.nav_device:
                Intent intent4 = new Intent(getApplicationContext(), DeviceActivity.class);
                startActivity(intent4);
                finish();
                return true;
            case R.id.nav_buynow:
                Intent intent6 = new Intent(getApplicationContext(), BuyNowActivity.class);
                startActivity(intent6);
                finish();
                return true;
            case R.id.nav_changepassword:
                Intent intent7 = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent7);
                finish();
                return true;
            case R.id.nav_fence:
                Intent intent3 = new Intent(getApplicationContext(), FenceActivity.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.nav_sharing:
                Intent intent2 = new Intent(getApplicationContext(), SharingActivity.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.nav_notification:
                Intent intent9 = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent9);
                finish();
                return true;
            case R.id.nav_myorders:
                Intent intent5 = new Intent(getApplicationContext(), MyOrdersActivity.class);
                startActivity(intent5);
                finish();
                return true;
            case R.id.nav_signout:
                SignOutApi();
                return true;
            default:
                return true;
        }
    }

    protected void startIntent(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }

    private void SignOutApi() {

        ds.clearSelectedGroupMember();
        ds.clearSelectedGroup();
//        DataSession.clearSessionData();
        SharedPrefUtility.clearSelectedGroup(getApplicationContext());
        SharedPrefUtility.clearSelectedGroupMember(getApplicationContext());

        final ProgressDialog mProgressDialog = ProgressbarUtil.startProgressBar(this, "Signing Out...");
        restEndpoint.logout(new Callback<ResponseDto<UserModel>>() {
            @Override
            public void success(ResponseDto<UserModel> userModelResponseDto, Response response) {
                ProgressbarUtil.stopProgressBar(mProgressDialog);
                ds.clearLoginRelatedInfo(getApplicationContext());
                SharedPrefUtility.clearprofile(getApplicationContext());
                startIntent(LoginActivity.class);
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                ProgressbarUtil.stopProgressBar(mProgressDialog);
                ds.clearLoginRelatedInfo(getApplicationContext());
                SharedPrefUtility.clearprofile(getApplicationContext());
                startIntent(LoginActivity.class);
                finish();
            }
        });
    }

    protected void initializeIcons(){}

    @Override
    public void onBackPressed() {
    }
}