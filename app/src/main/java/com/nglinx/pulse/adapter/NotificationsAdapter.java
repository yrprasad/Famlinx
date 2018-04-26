package com.nglinx.pulse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.activity.NotificationActivity;
import com.nglinx.pulse.models.NotificationModel;

import java.util.ArrayList;

public class NotificationsAdapter extends ArrayAdapter<NotificationModel> {

    private ViewHolder holder = null;

    private ArrayList<NotificationModel> arr2;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private LayoutInflater mInflater;
    private Context context;

    public NotificationsAdapter(Context context, ArrayList<NotificationModel> users) {
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
    public NotificationModel getItem(int position) {
        return arr2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        NotificationModel notificationModel = getItem(position);

        final ViewHolder holder = new ViewHolder();
        holder.modelHolder = notificationModel;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_notifications_row, null);
        }

        // Lookup view for data population
        holder.notif_row_type = (TextView) convertView.findViewById(R.id.notif_row_type);
        holder.notif_row_date = (TextView) convertView.findViewById(R.id.notif_row_date);
        holder.notif_row_message = (TextView) convertView.findViewById(R.id.notif_row_message);

        holder.iv_delete_notification = (ImageView) convertView.findViewById(R.id.iv_delete_notification);
        holder.iv_delete_notification.setTag(holder.modelHolder);
        holder.iv_delete_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NotificationActivity)context).onItemClick(holder.iv_delete_notification, position, 0);
            }
        });

        holder.iv_view_notification = (ImageView) convertView.findViewById(R.id.iv_view_notification);
        holder.iv_view_notification.setTag(holder.modelHolder);
        holder.iv_view_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NotificationActivity)context).onItemClick(holder.iv_view_notification, position, 0);
            }
        });

        // Populate the data into the template view using the data object

        if((notificationModel != null) && (notificationModel.getType() != null))
            holder.notif_row_type.setText(notificationModel.getType().toString());

        if((notificationModel != null) && (notificationModel.getCreatedDate() != null))
            holder.notif_row_date.setText(notificationModel.getCreatedDate());

        if((notificationModel.getMessage() != null) && ((notificationModel.getMessage().length() != 0)))
        {
            if (notificationModel.getMessage().length() > 50) {
                holder.notif_row_message.setText(notificationModel.getMessage().substring(0, 50) + "...");
            } else {
                holder.notif_row_message.setText(notificationModel.getMessage());
            }
        }

        convertView.setTag(holder);
        // Return the completed view to render on screen
        return convertView;
    }

    public static class ViewHolder {
        NotificationModel modelHolder;

        public TextView notif_row_type;
        public TextView notif_row_date;
        public TextView notif_row_message;

        public ImageView iv_delete_notification;
        public ImageView iv_view_notification;
    }
}