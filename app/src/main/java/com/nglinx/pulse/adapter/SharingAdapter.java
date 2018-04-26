package com.nglinx.pulse.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nglinx.pulse.constants.ApplicationConstants;

/**
 * Created by Priyabrat on 21-08-2015.
 */
public class SharingAdapter extends FragmentPagerAdapter {

    public SharingAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new SharingPendingInvitesFragment();
        } else if (position == 1) {
            fragment = new SharingTrackingMeFragment();
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
            return ApplicationConstants.SHARING_PENDING_INVITES_HEADER;
        } else if (position == 1) {
            return ApplicationConstants.SHARING_TRACKING_ME_HEADER;
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
