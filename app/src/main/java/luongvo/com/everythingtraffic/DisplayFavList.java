package luongvo.com.everythingtraffic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import luongvo.com.everythingtraffic.FavoritePlace.PlaceFavAdapter;
import luongvo.com.everythingtraffic.FavoritePlace.PlaceInfo;

public class DisplayFavList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<PlaceInfo> placeInfos = new ArrayList<>();
    private PlaceFavAdapter mAdapter;
    private PlaceInfo placeInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_fav_list);

        initComponent();
    }

    private void initComponent() {
        mRecyclerView = (RecyclerView) findViewById(R.id.favListRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView notice = (TextView) findViewById(R.id.emptyNotice);

        Intent intent = getIntent();
        Bundle intentExtra = intent.getExtras();
        if (intentExtra != null) {
            placeInfo = (PlaceInfo) intent.getSerializableExtra("placeInfoObj");
            placeInfos.add(placeInfo);
        }

        if (placeInfos.size() != 0) {
            notice.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new PlaceFavAdapter(placeInfos, this);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mRecyclerView.setVisibility(View.GONE);
            notice.setVisibility(View.VISIBLE);
            notice.setText("No item has been added here");
        }


    }
}
