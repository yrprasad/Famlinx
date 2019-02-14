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
import com.nglinx.pulse.utils.ApplicationUtils;


/**
 * Created by anupamchugh on 05/11/16.
 */


public class SharingFragmentAdapter extends ArrayAdapter {

    private ArrayList<InviteModel> dataSet;
    Context mContext;
    private int listType;

    // View lookup cache
    private class ViewHolder {
        TextView invites_row1;
        TextView invites_row2;
        TextView invites_row3;

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


    private View getPendingInvitesView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        InviteModel inviteModel = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_sharing_pending_invites_row, parent, false);
            viewHolder.acceptMember = (ImageView) convertView.findViewById(R.id.accept_member);
            viewHolder.rejectMember = (ImageView) convertView.findViewById(R.id.reject_member);
            viewHolder.accept_member_layout_pendinginvites = (RelativeLayout) convertView.findViewById(R.id.accept_member_layout_pendinginvites);
            viewHolder.reject_member_layout_pendinginvites = (RelativeLayout) convertView.findViewById(R.id.reject_member_layout_pendinginvites);

            viewHolder.invites_row1 = (TextView) convertView.findViewById(R.id.invites_row1);
            viewHolder.invites_row2 = (TextView) convertView.findViewById(R.id.invites_row2);
            viewHolder.invites_row3 = (TextView) convertView.findViewById(R.id.invites_row3);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.acceptMember.setTag(viewHolder.modelHolder);
        viewHolder.rejectMember.setTag(viewHolder.modelHolder);
        viewHolder.accept_member_layout_pendinginvites.setTag(viewHolder.modelHolder);
        viewHolder.reject_member_layout_pendinginvites.setTag(viewHolder.modelHolder);

        viewHolder.modelHolder = inviteModel;
        viewHolder.invites_row1.setText("User: " + inviteModel.getName() + "(" + inviteModel.getEmail() + ")");
        viewHolder.invites_row2.setText("requests tracking of " + getItem(position).getToName());
        viewHolder.invites_row3.setText(ApplicationUtils.convertFormatByTimeZone(getItem(position).getDate()));

        // Return the completed view to render on screen
        return convertView;
    }

    private View getAcceptedInvitesView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder; // view lookup cache stored in tag
        InviteModel inviteModel = getItem(position);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.activity_sharing_accepted_invites_row, parent, false);

            viewHolder.suspendMember = (ImageView) convertView.findViewById(R.id.suspend_member);
            viewHolder.rejectMember = (ImageView) convertView.findViewById(R.id.reject_member);
            viewHolder.suspend_member_layout_trackingme = (RelativeLayout) convertView.findViewById(R.id.suspend_member_layout_trackingme);
            viewHolder.reject_member_layout_trackingme = (RelativeLayout) convertView.findViewById(R.id.reject_member_layout_trackingme);

            viewHolder.invites_row1 = (TextView) convertView.findViewById(R.id.invites_row1);
            viewHolder.invites_row2 = (TextView) convertView.findViewById(R.id.invites_row2);
            viewHolder.invites_row3 = (TextView) convertView.findViewById(R.id.invites_row3);

            viewHolder.modelHolder = inviteModel;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.invites_row1.setText("User: " + getItem(position).getName());
        viewHolder.invites_row2.setText("Tracking: " + getItem(position).getToName());
        viewHolder.invites_row3.setText(ApplicationUtils.convertFormatByTimeZone(getItem(position).getDate()));
        viewHolder.suspendMember.setTag(viewHolder.modelHolder);
        viewHolder.rejectMember.setTag(viewHolder.modelHolder);
        viewHolder.suspend_member_layout_trackingme.setTag(viewHolder.modelHolder);
        viewHolder.reject_member_layout_trackingme.setTag(viewHolder.modelHolder);

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (listType == ApplicationConstants.SHARING_PENDING_INVITES_INDEX) {
            return getPendingInvitesView(position, convertView, parent);

        } else if (listType == ApplicationConstants.SHARING_TRACKING_ME_INDEX) {
            return getAcceptedInvitesView(position, convertView, parent);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
