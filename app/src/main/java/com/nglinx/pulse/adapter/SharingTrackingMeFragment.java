package com.nglinx.pulse.adapter;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.activity.SharingActivity;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.session.DataSession;

import java.util.ArrayList;

public class SharingTrackingMeFragment extends Fragment {

    ListView list;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_sharing);

        list = (ListView) view.findViewById(R.id.list);
        ArrayList stringList = (ArrayList) DataSession.getInstance().getTrackingMeInvites();

        SharingFragmentAdapter adapter = new SharingFragmentAdapter(stringList, getActivity(), ApplicationConstants.SHARING_TRACKING_ME_INDEX);
        list.setAdapter(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ((SharingActivity)getActivity()).refreshList();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        return view;
    }
}