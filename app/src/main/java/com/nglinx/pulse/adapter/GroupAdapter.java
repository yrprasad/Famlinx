package com.nglinx.pulse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nglinx.pulse.R;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.models.GroupModel;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends ArrayAdapter<GroupModel> {

    private ArrayList<GroupModel> arr1;
    private LayoutInflater mInflater;

    private Context context;

    public GroupAdapter(Context context, ArrayList<GroupModel> arr1) {
        super(context, 0, arr1);
        this.context = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arr1 = arr1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return arr1.size();
    }

    @Override
    public GroupModel getItem(int position) {

        if(position < 0)
            return null;

        if(position >= arr1.size())
            return  null;
        return arr1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = null;
        ViewHolder holder = null;
        final GroupModel model = arr1.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.nav_header_group_row, parent, false);
            holder = new ViewHolder();
            holder.tv_groupname = (TextView) convertView.findViewById(R.id.tv_groupname);
            holder.tv_groupmember_count = (TextView) convertView.findViewById(R.id.tv_groupmember_count);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_groupname.setText(model.getName());
        holder.tv_groupmember_count.setText(getMembercount(model.getMembers()) + " " + "member in this Circle");

        return convertView;
    }

    private int getMembercount(List<GroupMemberModel> members) {
        int count = members.size();
        for (GroupMemberModel member : members
                ) {
            if (member.getId().trim().equalsIgnoreCase(""))
                count--;
        }
        return count;
    }

    public class ViewHolder {
        public TextView tv_groupname, tv_groupmember_count;
    }

}

