package luongvo.com.everythingtraffic;



import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import luongvo.com.everythingtraffic.Modules.DirectionFinderListener;
import luongvo.com.everythingtraffic.Modules.MapWrapperLayout;
import luongvo.com.everythingtraffic.Modules.OnInterInfoWindowTouchListener;
import luongvo.com.everythingtraffic.Modules.Route;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback,DirectionFinderListener, LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks  {

    private static final LatLngBounds BOUNDS_HCMC = new LatLngBounds(new LatLng(10.500793, 106.379839),
            new LatLng(11.185964, 107.021166));


    LocationManager locationManager;
    String provider;

    private static final String LOG_TAG = "Map2";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Button btnFind;
    private PlaceAutoCompleteAdapter mAdapter;
    private AutoCompleteTextView placeToGo;
    protected LatLng latofPlace;
    protected LatLng start;
    View contentView;
    Button btnClick;

    MapWrapperLayout mapWrapperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addOnConnectionFailedListener(this)
                .build();

        btnFind = (Button) findViewById(R.id.findPlace);
        placeToGo = (AutoCompleteTextView) findViewById(R.id.inputPlace);
        mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1, mGoogleApiClient, BOUNDS_HCMC, null);
        placeToGo.setAdapter(mAdapter);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        placeToGo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);

                        start=place.getLatLng();
                    }
                });
            }
        });
        placeToGo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int startNum, int before, int count) {
                if (start != null) {
                    start = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_wrapper);


        mMap = googleMap;
        mapWrapperLayout.init(mMap, this);
        mMap.setMyLocationEnabled(true);




        // add a marker
        /*LatLng newWorldHotel = new LatLng(10.7719712,106.6954345);
        mMap.addMarker(new MarkerOptions().position(newWorldHotel).title("Location: New World Hotel")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.personstanding)));

        // create an animation for map while moving to this location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(newWorldHotel)
                .zoom(18)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        contentView = LayoutInflater.from(this).inflate(R.layout.content, null);
        btnClick = (Button) contentView.findViewById(R.id.Click);

        final OnInterInfoWindowTouchListener isClick = new OnInterInfoWindowTouchListener(btnClick) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Log.d("INFO WINDOW", "Clicked");
            }
        };

        btnClick.setOnTouchListener(isClick);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                isClick.setMarker(marker);
                TextView name = (TextView) contentView.findViewById(R.id.name);
                TextView location = (TextView) contentView.findViewById(R.id.location);
                name.setText(marker.getTitle());
                location.setText(marker.getPosition().toString());
                mapWrapperLayout.setMarkerWithInfoWindow(marker, contentView);
                return contentView;
            }

        });*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Your location"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            List<Address> listAddresses = geocoder.getFromLocation(lat, lng, 1);

            if (listAddresses != null && listAddresses.size() > 0) {

                Log.i("PlaceInfo", listAddresses.get(0).toString());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}