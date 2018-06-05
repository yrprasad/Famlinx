package com.nglinx.pulse.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.nglinx.pulse.R;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.InviteModel;
import com.nglinx.pulse.models.NotificationModel;


/**
 * Created by anupamchugh on 05/11/16.
 */


public class SharingFragmentAdapter extends ArrayAdapter {

    private ArrayList<InviteModel> dataSet;
    Context mContext;
    private int listType;

    // View lookup cache
    private static class ViewHolder {
        TextView notif_row_member_name;
        TextView notif_row_member_detail;
        TextView invite_time;
        InviteModel modelHolder;
        ImageView suspendMember;
        ImageView acceptMember;
        ImageView rejectMember;
        RelativeLayout reject_member_layout_trackingme;
        RelativeLayout suspend_member_layout_trackingme;

        RelativeLayout reject_member_layout_pendinginvites;
        RelativeLayout accept_member_layout_pendinginvites;
    }

    public SharingFragmentAdapter(ArrayList data, Context context, int listType) {
        super(context, 0, data);
        this.dataSet = data;
        this.mContext = context;
        this.listType = listType;
    }

    @Nullable
    @Override
    public InviteModel getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder; // view lookup cache stored in tag
        InviteModel inviteModel = getItem(position);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (listType == ApplicationConstants.SHARING_PENDING_INVITES_INDEX) {
                convertView = inflater.inflate(R.layout.activity_sharing_pending_invites_row, parent, false);
                viewHolder.modelHolder = inviteModel;

                viewHolder.acceptMember = (ImageView) convertView.findViewById(R.id.accept_member);
                viewHolder.rejectMember = (ImageView) convertView.findViewById(R.id.reject_member);
                viewHolder.accept_member_layout_pendinginvites = (RelativeLayout) convertView.findViewById(R.id.accept_member_layout_pendinginvites);
                viewHolder.reject_member_layout_pendinginvites = (RelativeLayout) convertView.findViewById(R.id.reject_member_layout_pendinginvites);

                viewHolder.acceptMember.setTag(viewHolder.modelHolder);
                viewHolder.rejectMember.setTag(viewHolder.modelHolder);
                viewHolder.accept_member_layout_pendinginvites.setTag(viewHolder.modelHolder);
                viewHolder.reject_member_layout_pendinginvites.setTag(viewHolder.modelHolder);
            } else if (listType == ApplicationConstants.SHARING_TRACKING_ME_INDEX) {
                convertView = inflater.inflate(R.layout.activity_sharing_accepted_invites_row, parent, false);
                viewHolder.modelHolder = inviteModel;
                viewHolder.suspendMember = (ImageView) convertView.findViewById(R.id.suspend_member);
                viewHolder.rejectMember = (ImageView) convertView.findViewById(R.id.reject_member);
                viewHolder.suspend_member_layout_trackingme = (RelativeLayout) convertView.findViewById(R.id.suspend_member_layout_trackingme);
                viewHolder.reject_member_layout_trackingme = (RelativeLayout) convertView.findViewById(R.id.reject_member_layout_trackingme);
                viewHolder.suspendMember.setTag(viewHolder.modelHolder);
                viewHolder.rejectMember.setTag(viewHolder.modelHolder);
                viewHolder.suspend_member_layout_trackingme.setTag(viewHolder.modelHolder);
                viewHolder.reject_member_layout_trackingme.setTag(viewHolder.modelHolder);
            }

            viewHolder.notif_row_member_name = (TextView) convertView.findViewById(R.id.notif_row_member_name);
            viewHolder.notif_row_member_detail = (TextView) convertView.findViewById(R.id.notif_row_member_detail);
            viewHolder.invite_time = (TextView) convertView.findViewById(R.id.invite_time);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.notif_row_member_name.setText(getItem(position).getName());
        viewHolder.notif_row_member_detail.setText(getItem(position).getEmail());
        viewHolder.invite_time.setText(getItem(position).getDate());

        // Return the completed view to render on screen
        return convertView;
    }
}
