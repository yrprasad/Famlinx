package com.nglinx.pulse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.models.DeviceType;
import com.nglinx.pulse.models.DeviceTypesModel;
import com.nglinx.pulse.models.DeviceTypesModel;
import com.nglinx.pulse.utils.ApplicationUtils;

import java.util.ArrayList;

/**
 * Created by android on 23/12/15.
 */
public class DeviceTypesAdapter extends ArrayAdapter<DeviceTypesModel> {

    private ViewHolder holder = null;

    private ArrayList<DeviceTypesModel> arr2;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private LayoutInflater mInflater;
    private Context context;

    public DeviceTypesAdapter(Context context, ArrayList<DeviceTypesModel> arr2) {
        super(context, 0, arr2);
        this.context = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arr2 = arr2;
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

    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;
        convertView = null;

        DeviceTypesModel selectedMember = arr2.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_buy_now_row, null);
            holder = new ViewHolder();
            holder.img_device_type = (ImageView) convertView.findViewById(R.id.img_device_type);
            holder.tv_device_type = (TextView) convertView.findViewById(R.id.tv_device_type);
            holder.tv_order_type = (TextView) convertView.findViewById(R.id.tv_order_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

		/*if(groupMembers.get(position).isFlag){
            // show button here
		}*/
        String first_name = ApplicationUtils.getUserFistName(selectedMember.getName());

        if(selectedMember.getType().equals(DeviceType.A9))
        {
            holder.img_device_type.setImageResource(R.drawable.a9);
        } else if(selectedMember.getType().equals(DeviceType.V7))
        {
            holder.img_device_type.setImageResource(R.drawable.v7);
        }else if(selectedMember.getType().equals(DeviceType.V16))
        {
            holder.img_device_type.setImageResource(R.drawable.v16);
        }else if(selectedMember.getType().equals(DeviceType.MQTT))
        {
            holder.img_device_type.setImageResource(R.drawable.accept);
        }

        holder.tv_device_type.setText(selectedMember.getName().toString());

        holder.tv_order_type.setText(selectedMember.getOrderType().toString());

        return convertView;
    }


    public static class ViewHolder {
        public TextView tv_device_type, tv_order_type;
        public ImageView img_device_type;
    }

    public void setArr2(ArrayList<DeviceTypesModel> arr2) {
        this.arr2 = arr2;
    }
}
