package luongvo.com.everythingtraffic.FavoritePlace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import luongvo.com.everythingtraffic.R;

/**
 * Created by luongvo on 18/06/2017.
 */

public class PlaceFavAdapter extends RecyclerView.Adapter<PlaceFavAdapter.CustomViewHolder> {

    private List<PlaceInfo> placeInfos;
    private Context context;
    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView titleView, bodyView;
        protected ImageButton shareButton, deleteButton;

        public CustomViewHolder(View view) {
            super(view);
            this.titleView = (TextView) view.findViewById(R.id.info_title);
            this.bodyView = (TextView) view.findViewById(R.id.info_body);
            this.shareButton = (ImageButton) view.findViewById(R.id.share_button);
            this.deleteButton = (ImageButton) view.findViewById(R.id.delete_button);

        }
    }

    public PlaceFavAdapter(List<PlaceInfo> placeInfos, Context context) {
        this.placeInfos = placeInfos;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        PlaceInfo placeInfo = placeInfos.get(position);
        holder.titleView.setText(placeInfo.getTitle());
        holder.bodyView.setText(placeInfo.getBody());
    }

    @Override
    public int getItemCount() {
        return ( null!=placeInfos ? placeInfos.size() : 0);
    }
}
