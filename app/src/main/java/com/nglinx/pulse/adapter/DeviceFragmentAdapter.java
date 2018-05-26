package com.nglinx.pulse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.activity.DeviceActivity;
import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.models.DeviceModel;

import java.util.List;

public class DeviceFragmentAdapter extends ArrayAdapter<DeviceModel> {

    Context context;
    List<ChildUserModel> childProfiles;

    public DeviceFragmentAdapter(Context context, List<DeviceModel> devices, List<ChildUserModel> childProfiles) {
        super(context, 0, devices);
        this.context = context;
        this.childProfiles = childProfiles;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        DeviceModel deviceModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_device_catalog_row, parent, false);
        }

        final View tmpView = convertView;

        // Lookup view for data population
//        TextView device_modified_date = (TextView) convertView.findViewById(R.id.device_modified_date);
        TextView tv_device_udid = (TextView) convertView.findViewById(R.id.tv_device_udid);
        TextView tv_date_modified = (TextView) convertView.findViewById(R.id.tv_date_modified);
        TextView tv_status_state = (TextView) convertView.findViewById(R.id.tv_status_state);
        // Populate the data into the template view using the data object

//        device_modified_date.setText(deviceModel.getModifiedDate());
        tv_device_udid.setText(deviceModel.getUdid());
        tv_date_modified.setText(deviceModel.getModifiedDate());

        String statusState = "";
        if (deviceModel.isActivated())
            statusState = statusState.concat("Active");
        else
            statusState = statusState.concat("InActive");

        final ChildUserModel userAttached = getAttachedProfileForDevice(deviceModel.getUdid());
        if (null == userAttached)
            statusState = statusState.concat(", " + "Not Attached.");
        else
            statusState = statusState.concat("Attached to " + userAttached.getUsername());

        tv_status_state.setText(statusState);

        final ImageView img_edit_device = (ImageView) convertView.findViewById(R.id.img_edit_device);
        img_edit_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeviceActivity) context).onDeviceItemClick(img_edit_device, position, 0);
            }
        });

        final ImageView img_manage_device = (ImageView) convertView.findViewById(R.id.img_manage_device);
        img_manage_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeviceActivity) context).onDeviceItemClick(img_manage_device, position, 1);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


    private ChildUserModel getAttachedProfileForDevice(final String udid) {
        for (ChildUserModel user :
                childProfiles) {
            if ((null != user.getUdid()) && (user.getUdid().equalsIgnoreCase(udid)))
                return user;
        }
        return null;
    }
}
