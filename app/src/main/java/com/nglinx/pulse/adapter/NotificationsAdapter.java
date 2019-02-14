package com.nglinx.pulse.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.activity.MyFencesActivity;
import com.nglinx.pulse.activity.NotificationActivity;
import com.nglinx.pulse.models.NotificationModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.ApplicationUtils;

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
        ViewHolder holder = null;
        final NotificationModel modelHolder = getItem(position);;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.activity_notifications_row, null);
            // Lookup view for data population
            holder.notif_row_type = (TextView) convertView.findViewById(R.id.notif_row_type);
            holder.notif_row_date = (TextView) convertView.findViewById(R.id.notif_row_date);
            holder.notif_row_message = (TextView) convertView.findViewById(R.id.notif_row_message);
            holder.options = (ImageView) convertView.findViewById(R.id.optionsMenu);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();

        final ViewHolder viewHolder1=holder;

        holder.options.setTag(holder);
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setTag(modelHolder);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        showPopupMenu(view);
                    }
                });
            }
        });

        if(DataSession.getInstance().isHomePageOn())
        {
            holder.options.setVisibility(View.INVISIBLE);
        } else
        {
            holder.options.setVisibility(View.VISIBLE);
        }

        /*holder.iv_delete_notification = (ImageView) convertView.findViewById(R.id.iv_delete_notification);
        holder.iv_delete_notification.setTag(holder.modelHolder);
        holder.iv_delete_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NotificationActivity)context).onProfileItemClick(holder.iv_delete_notification, position, 0);
            }
        });

        holder.iv_view_notification = (ImageView) convertView.findViewById(R.id.iv_view_notification);
        holder.iv_view_notification.setTag(holder.modelHolder);
        holder.iv_view_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NotificationActivity)context).onProfileItemClick(holder.iv_view_notification, position, 0);
            }
        });*/

        // Populate the data into the template view using the data object

        if((modelHolder != null) && (modelHolder.getType() != null))
            holder.notif_row_type.setText(modelHolder.getType().toString());
        else
            holder.notif_row_type.setText(NotificationModel.Type.ALERT.toString());

        if((modelHolder != null) && (modelHolder.getCreatedDate() != null))
        {
            String localTimeZoneFormat = ApplicationUtils.convertFormatByTimeZone(modelHolder.getCreatedDate());
            holder.notif_row_date.setText(localTimeZoneFormat);
        }

        if((modelHolder.getMessage() != null) && ((modelHolder.getMessage().length() != 0)))
        {
            if (modelHolder.getMessage().length() > 50) {
                holder.notif_row_message.setText(modelHolder.getMessage().substring(0, 50) + "...");
            } else {
                holder.notif_row_message.setText(modelHolder.getMessage());
            }
        }

        convertView.setTag(holder);
        // Return the completed view to render on screen
        return convertView;
    }

    public class ViewHolder {

        public TextView notif_row_type;
        public TextView notif_row_date;
        public TextView notif_row_message;

        public ImageView iv_delete_notification;
        public ImageView iv_view_notification;

        public ImageView options;
    }


    private void showPopupMenu(final View view) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(R.menu.activity_menu_notifications, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete_notifications:
                        ((NotificationActivity) context).deleteNotificationClickHandler(view);
                        return true;
                }
                return false;
            }
        });
    }
}