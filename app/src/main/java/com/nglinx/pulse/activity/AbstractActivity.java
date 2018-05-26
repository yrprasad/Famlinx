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
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_device:
                Intent intent4 = new Intent(this, DeviceActivity.class);
                startActivity(intent4);
                return true;
            case R.id.nav_buynow:
                Intent intent6 = new Intent(this, BuyNowActivity.class);
                startActivity(intent6);
                return true;
            case R.id.nav_changepassword:
                Intent intent7 = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent7);
                return true;
            /*case R.id.nav_profile:
                Intent intent5 = new Intent(this, MemberProfileActivity.class);
                startActivity(intent5);
                return true;*/
            case R.id.nav_fence:
                Intent intent3 = new Intent(this, FenceActivity.class);
                startActivity(intent3);
                return true;
            /*case R.id.nav_group:
                Intent intent1 = new Intent(this, CreateGroupActivty.class);
                startActivity(intent1);
                return true;*/
           /* */
            /*

            case R.id.nav_login:
                Intent intent8 = new Intent(this, LoginActivity.class);
                startActivity(intent8);
                return true;

            case R.id.nav_settings:
                Intent intent10 = new Intent(this, MemberSettingsActivity.class);
                startActivity(intent10);
                return true;
            case R.id.nav_signup:
                Intent intent11 = new Intent(this, SignupActivity.class);
                startActivity(intent11);
                return true;*/
            case R.id.nav_sharing:
                Intent intent2 = new Intent(this, SharingActivity.class);
                startActivity(intent2);
                return true;
            case R.id.nav_notification:
                Intent intent9 = new Intent(this, NotificationActivity.class);
                startActivity(intent9);
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
//        tv_username = (TextView) headerLayout.findViewById(R.id.tv_username);
//        tv_email = (TextView) headerLayout.findViewById(R.id.tv_email);
    }

    private void SignOutApi() {

        ds.clearSelectedGroupMember();
        ds.clearSelectedGroup();
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
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                ProgressbarUtil.stopProgressBar(mProgressDialog);
                ds.clearLoginRelatedInfo(getApplicationContext());
                SharedPrefUtility.clearprofile(getApplicationContext());
                startIntent(LoginActivity.class);
            }
        });
    }

    protected void initializeIcons(){}
}