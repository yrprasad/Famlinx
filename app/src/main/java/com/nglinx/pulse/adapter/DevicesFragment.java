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
import com.nglinx.pulse.activity.DeviceActivity;
import com.nglinx.pulse.session.DataSession;

public class DevicesFragment extends Fragment {

    ListView list;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_sharing);

        list = (ListView) view.findViewById(R.id.list);

        DeviceFragmentAdapter adapter = new DeviceFragmentAdapter(getActivity(), DataSession.getInstance().getDevicesList(), DataSession.getInstance().getChildProfilesList());
        list.setAdapter(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ((DeviceActivity) getActivity()).refreshList();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        return view;
    }
}