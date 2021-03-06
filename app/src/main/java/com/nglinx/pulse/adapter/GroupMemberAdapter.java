package com.nglinx.pulse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.session.DataSession;
import com.nglinx.pulse.utils.ApplicationUtils;
import com.nglinx.pulse.viewholders.GroupMemberViewHolder;

import java.util.ArrayList;

/**
 * Created by android on 23/12/15.
 */
public class GroupMemberAdapter extends ArrayAdapter<GroupMemberModel> {

    private GroupMemberViewHolder holder = null;

    private ArrayList<GroupMemberModel> arr2;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private LayoutInflater mInflater;
    private Context context;

    public GroupMemberAdapter(Context context, ArrayList<GroupMemberModel> arr2) {
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
    public GroupMemberModel getItem(int position) {
        return arr2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;
        convertView = null;
        final GroupMemberModel memberModel = arr2.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_member_list_view, null);
            holder = new GroupMemberViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.notif_row_member_name);
            holder.member_image = (ImageView) convertView.findViewById(R.id.member_image);
            holder.member_type_image = (ImageView) convertView.findViewById(R.id.member_type_image);
            holder.img_online = (ImageView) convertView.findViewById(R.id.img_online);
            convertView.setTag(holder);
        } else {
            holder = (GroupMemberViewHolder) convertView.getTag();
        }

        if (ApplicationUtils.isDummyMember(memberModel)) {
            holder.member_image.setImageResource(R.drawable.add_icon);
            holder.name.setText("Add User");
        } else {
            String first_name = ApplicationUtils.getUserFistName(memberModel.getName());
            holder.name.setText(first_name);
            holder.member_image.setImageResource(R.drawable.img1);

            if (ApplicationUtils.isDeviceUser(memberModel))
                holder.member_type_image.setImageResource(R.drawable.watch_icon);
            else
                holder.member_type_image.setImageResource(R.drawable.mobile_icon);

            if (memberModel.getId().equalsIgnoreCase(DataSession.getInstance().getSelected_group_member_id())) {
                holder.img_online.setVisibility(View.VISIBLE);
            } else {
                holder.img_online.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public void setArr2(ArrayList<GroupMemberModel> arr2) {
        this.arr2 = arr2;
    }
}
