package com.nglinx.pulse.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nglinx.pulse.R;
import com.nglinx.pulse.activity.DeviceActivity;
import com.nglinx.pulse.activity.MyFencesActivity;
import com.nglinx.pulse.models.FenceModel;
import com.nglinx.pulse.models.FenceModel;

import java.util.ArrayList;

public class MyFencesAdapter extends ArrayAdapter<FenceModel> {

    private ViewHolder holder = null;

    private ArrayList<FenceModel> arr2;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private LayoutInflater mInflater;
    private Context context;

    public MyFencesAdapter(Context context, ArrayList<FenceModel> users) {
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
    public FenceModel getItem(int position) {
        return arr2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        FenceModel fenceModel = getItem(position);

        final ViewHolder holder = new ViewHolder();
        holder.modelHolder = fenceModel;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_myfences_row, null);
        }

        // Lookup view for data population
        holder.tv_myfence_fencename = (TextView) convertView.findViewById(R.id.tv_myfence_fencename);
        holder.tv_myfence_createddate = (TextView) convertView.findViewById(R.id.tv_myfence_createddate);

        holder.options = (ImageView) convertView.findViewById(R.id.optionsMenu);
        holder.options.setTag(holder);
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setTag(holder.modelHolder);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        showPopupMenu(view);
                    }
                });
            }
        });

        // Populate the data into the template view using the data object

        if ((fenceModel != null))
            holder.tv_myfence_fencename.setText(fenceModel.getName());

        if ((fenceModel != null) && (fenceModel.getCreationDate() != null))
            holder.tv_myfence_createddate.setText(fenceModel.getCreationDate().toString());

        convertView.setTag(holder.modelHolder);
        // Return the completed view to render on screen
        return convertView;
    }

    public class ViewHolder {
        FenceModel modelHolder;

        public TextView tv_myfence_fencename;
        public TextView tv_myfence_createddate;
        public ImageView options;
    }

    private void showPopupMenu(final View view) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(R.menu.activity_menu_myfences, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete_fence:
                        ((MyFencesActivity) context).deleteFenceClickHandler(view);
                        return true;
                    case R.id.menu_apply_fence:
                        ((MyFencesActivity) context).applyFenceClickHandler(view);
                        return true;
                    case R.id.menu_edit_fence:
                        ((MyFencesActivity) context).editFenceClickHandler(view);
                        return true;
                }
                return false;
            }
        });
    }
}