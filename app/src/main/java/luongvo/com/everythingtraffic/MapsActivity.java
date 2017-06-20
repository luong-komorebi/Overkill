package luongvo.com.everythingtraffic;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.error.ApiError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import luongvo.com.everythingtraffic.Modules.DirectionFinder;
import luongvo.com.everythingtraffic.Modules.DirectionFinderListener;
import luongvo.com.everythingtraffic.Modules.Route;




public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks  {
    private static final String LOG_TAG = "MyActivity";
    private GoogleMap mMap;
    private ImageButton btnFindPath;
    private RelativeLayout searchBar;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarker = new ArrayList<>();
    private List<Polyline> polyLinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private PlaceAutoCompleteAdapter mAdapter;
    protected GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_HCMC = new LatLngBounds(new LatLng(10.500793, 106.379839),
            new LatLng(11.185964, 107.021166));

    private AutoCompleteTextView starting;
    private AutoCompleteTextView destination;
    private RideRequestButton requestButton;
    private RideRequestButton requestButton2;
    private RideRequestButton requestButton3;
    protected LatLng start;
    protected LatLng end;
    private Button buttonMaiLinh;
    private Button buttonVinaSun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initComponent();
        setUpAutoSuggestion();

    }


    private void setupUber() {
        if (start == null || end == null) {
            Toast.makeText(this, "Address not accessible. Please redo and choose from suggestion", Toast.LENGTH_SHORT).show();
            finish();
        }
        searchBar.setVisibility(View.GONE);
        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId(getResources().getString(R.string.uber_client_id))
                .setServerToken(getResources().getString(R.string.uber_server_token))
                .setRedirectUri("luongvo://overkill")
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();
        UberSdk.initialize(config);


        RideParameters rideParams = new RideParameters.Builder()
                .setProductId("8d7386df-d99c-48fd-a628-ea65f859b0ab")
                .setDropoffLocation(end.latitude, end.longitude, destination.getText().toString(), destination.getText().toString())
                .setPickupLocation(start.latitude, start.longitude, starting.getText().toString(), starting.getText().toString())
                .setDropoffLocation(end.latitude, end.longitude, destination.getText().toString(), destination.getText().toString())
                .build();

        RideParameters rideParams2 = new RideParameters.Builder()
                .setProductId("cafb57ec-5157-4462-8453-09a7fc01f5d1")
                .setDropoffLocation(end.latitude, end.longitude, destination.getText().toString(), destination.getText().toString())
                .setPickupLocation(start.latitude, start.longitude, starting.getText().toString(), starting.getText().toString())
                .setDropoffLocation(end.latitude, end.longitude, destination.getText().toString(), destination.getText().toString())
                .build();

        RideParameters rideParams3 = new RideParameters.Builder()
                .setProductId("0b6b2de2-a6f3-4fa7-8385-414312f042ce")
                .setDropoffLocation(end.latitude, end.longitude, destination.getText().toString(), destination.getText().toString())
                .setPickupLocation(start.latitude, start.longitude, starting.getText().toString(), starting.getText().toString())
                .setDropoffLocation(end.latitude, end.longitude, destination.getText().toString(), destination.getText().toString())
                .build();


        ServerTokenSession session = new ServerTokenSession(config);

        RideRequestButtonCallback callback = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {
                // react to the displayed estimates
                requestButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(ApiError apiError) {
                // API error details: /docs/riders/references/api#section-errors
                Toast.makeText(MapsActivity.this, apiError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable throwable) {
                // Unexpected error, very likely an IOException
            }
        };
        RideRequestButtonCallback callback2 = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {
                // react to the displayed estimates
                requestButton2.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(ApiError apiError) {
                // API error details: /docs/riders/references/api#section-errors
                Toast.makeText(MapsActivity.this, apiError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable throwable) {
                // Unexpected error, very likely an IOException
            }
        };
        RideRequestButtonCallback callback3 = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {
                // react to the displayed estimates
                requestButton3.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(ApiError apiError) {
                // API error details: /docs/riders/references/api#section-errors
                Toast.makeText(MapsActivity.this, apiError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable throwable) {
                // Unexpected error, very likely an IOException
            }
        };
        requestButton.setRideParameters(rideParams);
        requestButton2.setRideParameters(rideParams2);
        requestButton3.setRideParameters(rideParams3);
        requestButton.setSession(session);
        requestButton2.setSession(session);
        requestButton3.setSession(session);
        requestButton.setCallback(callback);
        requestButton2.setCallback(callback2);
        requestButton3.setCallback(callback3);
        requestButton.loadRideInformation();
        requestButton2.loadRideInformation();
        requestButton3.loadRideInformation();
        buttonMaiLinh.setVisibility(View.VISIBLE);
        buttonVinaSun.setVisibility(View.VISIBLE);


    }

    private void setUpAutoSuggestion() {
        starting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                        end=place.getLatLng();
                    }
                });

            }
        });

        /*
        These text watchers set the start and end points to null because once there's
        * a change after a value has been selected from the dropdown
        * then the value has to reselected from dropdown to get
        * the correct location.
        * */
        starting.addTextChangedListener(new TextWatcher() {
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

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(end!=null)
                {
                    end=null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initComponent() {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addOnConnectionFailedListener(this)
                .build();

        searchBar = (RelativeLayout) findViewById(R.id.searchBar);

        requestButton = (RideRequestButton) findViewById(R.id.uberRequest);
        requestButton2 = (RideRequestButton) findViewById(R.id.uberRequest2);
        requestButton3 = (RideRequestButton) findViewById(R.id.uberRequest3);
        buttonMaiLinh = (Button) findViewById(R.id.btnMaiLinh);
        buttonVinaSun = (Button) findViewById(R.id.btnVinasun);
        btnFindPath = (ImageButton) findViewById(R.id.buttonFindPath);

        starting = (AutoCompleteTextView) findViewById(R.id.start);
        destination = (AutoCompleteTextView) findViewById(R.id.destination);

        mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_HCMC, null);
        starting.setAdapter(mAdapter);
        destination.setAdapter(mAdapter);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(btnFindPath.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });

        buttonMaiLinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:02838383838"));
                startActivity(intent);
            }
        });

        buttonVinaSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:02838277178"));
                startActivity(intent);
            }
        });

        requestButton.setVisibility(View.GONE);
        requestButton2.setVisibility(View.GONE);
        requestButton3.setVisibility(View.GONE);
        buttonMaiLinh.setVisibility(View.GONE);
        buttonVinaSun.setVisibility(View.GONE);


        searchBar.setVisibility(View.VISIBLE);

    }


    private void sendRequest() {
        String originString = starting.getText().toString();
        String destinationString = destination.getText().toString();
        Bundle b = getIntent().getExtras();
        if (b != null)
            setupUber();
        if (originString.isEmpty()) {
            Toast.makeText(this, "Please enter origin!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destinationString.isEmpty()){
            Toast.makeText(this, "Please enter destination!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            new DirectionFinder(this, originString, destinationString).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
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
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);

        // move to the center of Vietnam
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(10.7771649,106.6953986))
                .zoom(18)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait", "Finding direction", true);
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }
        if (destinationMarker != null) {
            for (Marker marker : destinationMarker) {
                marker.remove();
            }
        }
        if (polyLinePaths != null) {
            for (Polyline polylinePath : polyLinePaths) {
                polylinePath.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {
        progressDialog.dismiss();
        polyLinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarker = new ArrayList<>();

        for (Route eachroute : route) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eachroute.startLocation, 16));

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_place))
                    .title(eachroute.startAddress)
                    .position(eachroute.startLocation)));

            destinationMarker.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_place))
                    .title(eachroute.endAddress)
                    .position(eachroute.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions()
                    .geodesic(true)
                    .color(Color.BLUE)
                    .width(10);

            for (int i = 0; i < eachroute.points.size(); i++) {
                polylineOptions.add(eachroute.points.get(i));
            }
            Toast.makeText(this, "Distance: "+ eachroute.distance.text +", ETA: " + eachroute.duration.text, Toast.LENGTH_LONG).show();
            starting.setText("");
            destination.setText("");
            polyLinePaths.add(mMap.addPolyline(polylineOptions));
        }
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
    protected void onStop() {
        super.onStop();
        // stop GoogleApiClient
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
