package luongvo.com.everythingtraffic.BusData;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import luongvo.com.everythingtraffic.R;

/**
 * Created by luongvo on 21/06/2017.
 */

public class BusDataAdapter extends ArrayAdapter<routeInfo> {

    private Context context;
    private int resourceID;
    private ArrayList<routeInfo> routeInfos;
    private ArrayList<routeInfo> searchRouteInfos;

    public BusDataAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<routeInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceID = resource;
        this.routeInfos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
//            LayoutInflater layoutInflater = ((Activity) this.context).getLayoutInflater();
//            convertView = layoutInflater.inflate(resourceID, parent, false);
            convertView = LayoutInflater.from(this.context).inflate(resourceID, parent, false);
        }


        TextView busName = (TextView) convertView.findViewById(R.id.busName);
        TextView busRoute = (TextView) convertView.findViewById(R.id.busRoute);
        routeInfo routeInfoItem = routeInfos.get(position);

        busName.setText("Xe số : " + routeInfoItem.getRouteId());
        busRoute.setText(routeInfoItem.getRouteName());
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<routeInfo> filteredRouteInfos = new ArrayList<>();

                if (searchRouteInfos == null) {
                    searchRouteInfos = new ArrayList<>(routeInfos);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = searchRouteInfos.size();
                    results.values = searchRouteInfos;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < searchRouteInfos.size(); i++) {
                        String data = "Xe số " + searchRouteInfos.get(i).getRouteId();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            filteredRouteInfos.add(searchRouteInfos.get(i));
                        }
                    }
                    results.count = filteredRouteInfos.size();
                    results.values = filteredRouteInfos;
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                routeInfos = (ArrayList<routeInfo>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    @Override
    public int getCount() {
        return routeInfos.size();
    }

}
