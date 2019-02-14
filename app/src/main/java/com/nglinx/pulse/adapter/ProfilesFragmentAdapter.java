package com.nglinx.pulse.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.activity.DeviceActivity;
import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.models.NotificationModel;
import com.nglinx.pulse.session.DataSession;

import java.util.List;

public class ProfilesFragmentAdapter extends ArrayAdapter<ChildUserModel> {

    private List<ChildUserModel> arr2;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private LayoutInflater mInflater;
    private Context context;

    public ProfilesFragmentAdapter(Context context, List<ChildUserModel> users) {
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
        return DataSession.getInstance().getChildProfilesList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ViewHolder holder = null;
        ChildUserModel modelHolder = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_member_profile_row, null);

            // Lookup view for data population
            holder.tv_profile_name = (TextView) convertView.findViewById(R.id.tv_profile_name);
            holder.tv_prodile_email = (TextView) convertView.findViewById(R.id.tv_prodile_email);
            holder.tv_profile_udid = (TextView) convertView.findViewById(R.id.tv_profile_udid);
            holder.tv_profile_status = (TextView) convertView.findViewById(R.id.tv_profile_status);
            holder.img_online_status = (ImageView) convertView.findViewById(R.id.img_online_status);

            holder.layout_profile_manage= (RelativeLayout) convertView.findViewById(R.id.layout_profile_manage);
            holder.btn_profile_manage = (ImageView) convertView.findViewById(R.id.btn_profile_manage);

            holder.layout_profile_edit= (RelativeLayout) convertView.findViewById(R.id.layout_profile_edit);
            holder.btn_profile_edit = (ImageView) convertView.findViewById(R.id.btn_profile_edit);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final ViewHolder viewHolder1 = holder;

        holder.btn_profile_manage.setTag(modelHolder);
        holder.btn_profile_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeviceActivity) context).onProfileItemClick(viewHolder1.btn_profile_manage, position, 0);
            }
        });
        holder.layout_profile_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeviceActivity) context).onProfileItemClick(viewHolder1.btn_profile_manage, position, 0);
            }
        });

        holder.btn_profile_edit.setTag(modelHolder);
        holder.btn_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeviceActivity) context).onProfileItemClick(viewHolder1.btn_profile_edit, position, 0);
            }
        });
        holder.layout_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeviceActivity) context).onProfileItemClick(viewHolder1.btn_profile_edit, position, 0);
            }
        });

        // Populate the data into the template view using the data object

        if ((modelHolder != null) && (modelHolder.getName() != null))
            holder.tv_profile_name.setText(modelHolder.getName());

        if ((modelHolder != null) && (modelHolder.getEmail() != null))
            holder.tv_prodile_email.setText(modelHolder.getEmail());

        if ((modelHolder != null) && (modelHolder.getUdid() != null)) {
            holder.tv_profile_udid.setText(modelHolder.getUdid());
        } else
        {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.inactive_row));
        }

        if ((modelHolder != null) && (modelHolder.getStatus() != null)) {
            if(modelHolder.getStatus().equals(1))
            {
                holder.tv_profile_status.setText("Active");
                holder.img_online_status.setBackgroundResource(R.drawable.circle_green);
            } else
            {
                holder.tv_profile_status.setText("InActive");
                holder.img_online_status.setBackgroundResource(R.drawable.circle_red);
            }

        }

        // Return the completed view to render on screen
        return convertView;
    }

    public class ViewHolder {

        public TextView tv_profile_name;
        public TextView tv_prodile_email;
        public TextView tv_profile_udid;
        public TextView tv_profile_status;

        public ImageView btn_profile_manage;
        public ImageView btn_profile_edit;
        public ImageView img_online_status;
        public RelativeLayout layout_profile_manage;
        public RelativeLayout layout_profile_edit;
    }
}
