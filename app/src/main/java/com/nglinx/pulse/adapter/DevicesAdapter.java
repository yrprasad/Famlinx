package com.nglinx.pulse.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nglinx.pulse.constants.ApplicationConstants;

public class DevicesAdapter extends FragmentPagerAdapter {

    public DevicesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new DevicesFragment();
        } else if (position == 1) {
            fragment = new ProfilesFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return ApplicationConstants.DEVICES_DEVICE_HEADER;
        } else if (position == 1) {
            return ApplicationConstants.DEVICES_MEMBER_PROFILES_HEADER;
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
