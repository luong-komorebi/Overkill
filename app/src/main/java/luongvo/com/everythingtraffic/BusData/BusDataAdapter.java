package luongvo.com.everythingtraffic.BusData;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        routeInfo routeInfoItem = routeInfos.get(position);
        TextView busName = (TextView) convertView.findViewById(R.id.busName);
        TextView busRoute = (TextView) convertView.findViewById(R.id.busRoute);

        busName.setText("Xe số : " + routeInfoItem.getRouteId());
        busRoute.setText(routeInfoItem.getRouteName());
        return convertView;





    }
}
