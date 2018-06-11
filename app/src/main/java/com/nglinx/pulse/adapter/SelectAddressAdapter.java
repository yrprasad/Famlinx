package com.nglinx.pulse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.models.AddressModel;
import com.nglinx.pulse.utils.ApplicationUtils;

import java.util.ArrayList;

public class SelectAddressAdapter extends ArrayAdapter<AddressModel> {

    private ViewHolder holder = null;

    private ArrayList<AddressModel> arr2;
    private LayoutInflater mInflater;
    private Context context;

    public SelectAddressAdapter(Context context, ArrayList<AddressModel> users) {
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
    public AddressModel getItem(int position) {
        return arr2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        AddressModel addressModel = getItem(position);

        final ViewHolder holder = new ViewHolder();
        holder.modelHolder = addressModel;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_select_address_row, null);
        }

        holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
        holder.tv_address.setText(ApplicationUtils.getAddress(addressModel));

        convertView.setTag(holder);
        // Return the completed view to render on screen
        return convertView;
    }

    public class ViewHolder {
        AddressModel modelHolder;
        public TextView tv_address;
    }
}