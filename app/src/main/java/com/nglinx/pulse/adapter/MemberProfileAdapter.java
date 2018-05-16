package com.nglinx.pulse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.activity.MemberProfileActivity;
import com.nglinx.pulse.models.ChildUserModel;

import java.util.ArrayList;

public class MemberProfileAdapter extends ArrayAdapter<ChildUserModel> {

    private ViewHolder holder = null;

    private ArrayList<ChildUserModel> arr2;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private LayoutInflater mInflater;
    private Context context;

    public MemberProfileAdapter(Context context, ArrayList<ChildUserModel> users) {
        super(context, 0, users);
        this.context = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arr2 = users;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return arr2.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ChildUserModel getItem(int position) {
        return arr2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ChildUserModel model = getItem(position);

        final ViewHolder holder = new ViewHolder();
        holder.modelHolder = model;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_member_profile_row, null);
        }

        // Lookup view for data population
        holder.tv_profile_name = (TextView) convertView.findViewById(R.id.tv_profile_name);
        holder.tv_prodile_email = (TextView) convertView.findViewById(R.id.tv_prodile_email);
        holder.tv_profile_udid = (TextView) convertView.findViewById(R.id.tv_profile_udid);
        holder.tv_profile_status = (TextView) convertView.findViewById(R.id.tv_profile_status);

        holder.btn_profile_delete = (ImageView) convertView.findViewById(R.id.btn_profile_delete);
        holder.btn_profile_delete.setTag(holder.modelHolder);
        holder.btn_profile_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MemberProfileActivity) context).onItemClick(holder.btn_profile_delete, position, 0);
            }
        });

        holder.btn_profile_view = (ImageView) convertView.findViewById(R.id.btn_profile_view);
        holder.btn_profile_view.setTag(holder.modelHolder);
        holder.btn_profile_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MemberProfileActivity) context).onItemClick(holder.btn_profile_view, position, 0);
            }
        });

        // Populate the data into the template view using the data object

        if ((model != null) && (model.getName() != null))
            holder.tv_profile_name.setText(model.getName());

        if ((model != null) && (model.getEmail() != null))
            holder.tv_prodile_email.setText(model.getEmail());

        if ((model != null) && (model.getUdid() != null))
            holder.tv_profile_udid.setText(model.getUdid());

        if ((model != null) && (model.getStatus() != null))
            holder.tv_profile_status.setText(String.valueOf(model.getStatus()));

        convertView.setTag(holder);
        // Return the completed view to render on screen
        return convertView;
    }

    public static class ViewHolder {
        ChildUserModel modelHolder;

        public TextView tv_profile_name;
        public TextView tv_prodile_email;
        public TextView tv_profile_udid;
        public TextView tv_profile_status;

        public ImageView btn_profile_delete;
        public ImageView btn_profile_view;
    }
}