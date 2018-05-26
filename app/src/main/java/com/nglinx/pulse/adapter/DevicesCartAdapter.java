package com.nglinx.pulse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.activity.CartActivity;
import com.nglinx.pulse.activity.NotificationActivity;
import com.nglinx.pulse.models.DeviceType;
import com.nglinx.pulse.models.DeviceTypesModel;

import java.util.ArrayList;

public class DevicesCartAdapter extends ArrayAdapter<DeviceTypesModel> {

    private ViewHolder holder = null;

    private ArrayList<DeviceTypesModel> arr2;

    private LayoutInflater mInflater;
    private Context context;

    public DevicesCartAdapter(Context context, ArrayList<DeviceTypesModel> users) {
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
    public DeviceTypesModel getItem(int position) {
        return arr2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        DeviceTypesModel deviceTypeModel = getItem(position);

        final ViewHolder holder = new ViewHolder();
        holder.modelHolder = deviceTypeModel;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_cart_row, null);
        }
        initializeIcons(holder, convertView);

        setValues(holder, deviceTypeModel);

        holder.tv_cart_minus.setTag(holder.modelHolder);
        holder.tv_cart_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CartActivity)context).onItemClick(holder.tv_cart_minus, holder.tv_cart_count, holder.tv_cart_cost, position, 0);
            }
        });

        holder.tv_cart_plus.setTag(holder.modelHolder);
        holder.tv_cart_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CartActivity)context).onItemClick(holder.tv_cart_plus, holder.tv_cart_count, holder.tv_cart_cost, position, 0);
            }
        });

        /*holder.tv_cart_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CartActivity)context).onProfileItemClick(holder.tv_cart_minus, position, 0);
            }
        });*/

        convertView.setTag(holder);
        // Return the completed view to render on screen
        return convertView;
    }

    private void setValues(ViewHolder holder, DeviceTypesModel model) {
        if (model.getType().equals(DeviceType.A9)) {
            holder.img_device_type.setImageResource(R.drawable.a9);
        } else if (model.getType().equals(DeviceType.V7)) {
            holder.img_device_type.setImageResource(R.drawable.v7);
        } else if (model.getType().equals(DeviceType.V16)) {
            holder.img_device_type.setImageResource(R.drawable.v16);
        } else if (model.getType().equals(DeviceType.MQTT)) {
            holder.img_device_type.setImageResource(R.drawable.accept);
        }

        holder.tv_device_type.setText(model.getType().toString());
        holder.tv_cart_device_desc.setText(model.getDescription().get(0));
        holder.tv_cart_count.setText(String.valueOf(model.getCount()));
        holder.tv_cart_cost.setText(String.valueOf(model.getCount() * model.getCost()));
    }

    private void initializeIcons(ViewHolder holder, View convertView) {
        holder.img_device_type = (ImageView) convertView.findViewById(R.id.img_device_type);
        holder.tv_device_type = (TextView) convertView.findViewById(R.id.tv_device_type);
        holder.tv_cart_device_desc = (TextView) convertView.findViewById(R.id.tv_cart_device_desc);
        holder.tv_cart_count = (EditText) convertView.findViewById(R.id.tv_cart_count);
        holder.tv_cart_minus = (TextView) convertView.findViewById(R.id.tv_cart_minus);
        holder.tv_cart_plus = (TextView) convertView.findViewById(R.id.tv_cart_plus);
        holder.tv_cart_cost = (TextView) convertView.findViewById(R.id.tv_cart_cost);
    }

    public static class ViewHolder {
        DeviceTypesModel modelHolder;

        public ImageView img_device_type;
        public TextView tv_device_type;
        public TextView tv_cart_device_desc;
        public TextView tv_cart_minus;
        public EditText tv_cart_count;
        public TextView tv_cart_plus;
        public TextView tv_cart_cost;
    }
}