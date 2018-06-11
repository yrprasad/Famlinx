package com.nglinx.pulse.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.nglinx.pulse.models.UserModel;
import com.nglinx.pulse.utils.DialogUtils;
import com.nglinx.pulse.utils.retrofit.ApiEndpointInterface;
import com.nglinx.pulse.utils.retrofit.RetroResponse;
import com.nglinx.pulse.utils.retrofit.RetroUtils;

import java.util.ArrayList;
import java.util.List;

public class AddMemberAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<UserModel> members;
    Context context;

    public AddMemberAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        members = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public UserModel getItem(int position) {
        if (members != null)
            return members.get(position);
        else
            return null;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                final FilterResults filterResults = new FilterResults();
                final List<UserModel> userModels = new ArrayList<>();

                if (constraint != null) {
                    try {
                        //get data from the web
                        String term = constraint.toString();

                        ApiEndpointInterface apiEndpointInterface = RetroUtils.getHostAdapterForAuthenticate(context, RetroUtils.URL_HIT).create(ApiEndpointInterface.class);
                        apiEndpointInterface.getFilteredUsers(term + "*", new RetroResponse<UserModel>() {
                            @Override
                            public void onSuccess() {
                                members.clear();
                                members.addAll(models);
                                filterResults.values = userModels;
                                filterResults.count = userModels.size();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure() {
                                DialogUtils.diaplayFailureDialog(context, errorMsg);
                            }
                        });

                    } catch (Exception e) {
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return myFilter;
    }

}