package luongvo.com.everythingtraffic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import luongvo.com.everythingtraffic.FavoritePlace.PlaceFavAdapter;
import luongvo.com.everythingtraffic.FavoritePlace.PlaceInfo;

import static android.R.attr.key;

public class DisplayFavList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<PlaceInfo> placeInfos;
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

        retrieveArrayList();
        if (intentExtra != null) {
            placeInfo = (PlaceInfo) intent.getSerializableExtra("placeInfoObj");
            if (placeInfos == null)
                placeInfos = new ArrayList<>();
            placeInfos.add(placeInfo);
            saveArrayList();
        }


        if (placeInfos != null) {
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



    public void saveArrayList() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(placeInfos);
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("MyObject", jsonString);
        prefsEditor.apply();
    }

    private void retrieveArrayList() {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String jsonPlaces = appSharedPrefs.getString("MyObject", "");
        Type type = new TypeToken<List<PlaceInfo>>(){}.getType();
        placeInfos = gson.fromJson(jsonPlaces, type);
    }
}
