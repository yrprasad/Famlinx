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
import com.nglinx.pulse.activity.NotificationActivity;
import com.nglinx.pulse.models.DeviceOrderModel;
import com.nglinx.pulse.models.DeviceType;
import com.nglinx.pulse.utils.ApplicationUtils;

import java.util.ArrayList;

public class MyOrdersAdapter extends ArrayAdapter<DeviceOrderModel> {

    private ViewHolder holder = null;

    private ArrayList<DeviceOrderModel> arr2;
    private LayoutInflater mInflater;
    private Context context;

    public MyOrdersAdapter(Context context, ArrayList<DeviceOrderModel> users) {
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
    public DeviceOrderModel getItem(int position) {
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
        final DeviceOrderModel modelHolder = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_my_account_order_row, null);

            holder = new ViewHolder();
            // Lookup view for data population
            holder.tv_order_id = (TextView) convertView.findViewById(R.id.tv_order_id);
            holder.tv_device_name = (TextView) convertView.findViewById(R.id.tv_device_name);
            holder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_order_status);
            holder.tv_order_date = (TextView) convertView.findViewById(R.id.tv_order_date);
            holder.img_device = (ImageView) convertView.findViewById(R.id.img_device);

            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_order_id.setText("Order # " + modelHolder.getId());
        holder.tv_device_name.setText(modelHolder.getDeviceType().toString());
        holder.tv_order_status.setText("Order Status : " + modelHolder.getStatus());
        holder.tv_order_date.setText("Order Date : " +
                ApplicationUtils.convertFormatByTimeZone(modelHolder.getCreatedDate()));


        if (modelHolder.getDeviceType().equals(DeviceType.A9)) {
            holder.img_device.setImageResource(R.drawable.a9);
        } else if (modelHolder.getDeviceType().equals(DeviceType.V7)) {
            holder.img_device.setImageResource(R.drawable.v7);
        } else if (modelHolder.getDeviceType().equals(DeviceType.V16)) {
            holder.img_device.setImageResource(R.drawable.v16);
        } else if (modelHolder.getDeviceType().equals(DeviceType.MQTT)) {
            holder.img_device.setImageResource(R.drawable.mqtt);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    public class ViewHolder {

        public TextView tv_order_id;
        public TextView tv_device_name;
        public TextView tv_order_status;
        public TextView tv_order_date;
        public ImageView img_device;
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