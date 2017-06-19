package luongvo.com.everythingtraffic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import luongvo.com.everythingtraffic.FavoritePlace.PlaceInfo;
import luongvo.com.everythingtraffic.Modules.MapWrapperLayout;
import luongvo.com.everythingtraffic.Modules.OnInterInfoWindowTouchListener;


public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks  {

    private static final LatLngBounds BOUNDS_HCMC = new LatLngBounds(new LatLng(10.500793, 106.379839),
            new LatLng(11.185964, 107.021166));
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String LOG_TAG = "Map2";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Button btnFind;
    private PlaceInfo placeInfo;
    private PlaceAutoCompleteAdapter mAdapter;
    private AutoCompleteTextView placeToGo;
    private String info;
    protected LatLng start = null;

    MapWrapperLayout mapWrapperLayout;
    private View contentView;
    private Button btnClick;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        initComponent();
    }

    private void initComponent() {
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
        setAutoSuggestion();

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!placeToGo.getText().toString().equals("")) {
                    moveToPosition();
                    placeToGo.setText("");
                }
                else Toast.makeText(MapsActivity2.this, "Please input a place", Toast.LENGTH_SHORT).show();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });


    }

    private void setAutoSuggestion() {
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

                        start = place.getLatLng();
                        info = place.getName().toString() + ", " + place.getAddress().toString();
                        if (!info.equals("")) {
                            placeInfo = new PlaceInfo(place.getName().toString(), place.getAddress().toString());
                        }
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
    }

    private void moveToPosition() {
        // add a marker
        LatLng wantToGoTo = start;
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(wantToGoTo).title(info)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.personstanding)));

        // create an animation for map while moving to this location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(wantToGoTo)
                .zoom(18)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void getCurPos () {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            start = new LatLng(location.getLatitude(), location.getLongitude());
                            if (info == null)
                                info = getAddress(MapsActivity2.this, location.getLatitude(), location.getLongitude());
                            moveToPosition();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MapsActivity2.this, "Check Your GPS or network", Toast.LENGTH_SHORT).show();
                    }
                });
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
        getCurPos();

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getCurPos();
                return true;
            }
        });

        getPopupInfo();
    }

    public void getPopupInfo() {
        contentView = LayoutInflater.from(this).inflate(R.layout.content, null);
        btnClick = (Button) contentView.findViewById(R.id.ClickToSeeMore);
        btnSave = (Button) contentView.findViewById(R.id.ClickToSave);

        final OnInterInfoWindowTouchListener isClick = new OnInterInfoWindowTouchListener(btnClick) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                String url2Load = "https://www.google.com/search?q=" + info.replace(" ", "+").replace(",", "%2C");
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(MapsActivity2.this, Uri.parse(url2Load));
            }
        };

        final OnInterInfoWindowTouchListener isClick2 = new OnInterInfoWindowTouchListener(btnClick) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity2.this).create();
                alertDialog.setTitle("Confirm Save Place");
                alertDialog.setMessage("Press OK will save place to favorite");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        savePlaceToFavorite();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        };

        btnClick.setOnTouchListener(isClick);
        btnSave.setOnTouchListener(isClick2);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                isClick.setMarker(marker);
                TextView name = (TextView) contentView.findViewById(R.id.name);
                name.setText(marker.getTitle());
                mapWrapperLayout.setMarkerWithInfoWindow(marker, contentView);
                return contentView;
            }

        });
    }

    private void savePlaceToFavorite() {
        if (placeInfo == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity2.this);
            builder.setTitle("Input Name For Place");
            final EditText input = new EditText(MapsActivity2.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    placeInfo = new PlaceInfo(input.getText().toString(), info);
                    Intent intent = new Intent(MapsActivity2.this, DisplayFavList.class);
                    intent.putExtra("placeInfoObj", placeInfo);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
        else {
            Intent intent = new Intent(this, DisplayFavList.class);
            intent.putExtra("placeInfoObj", placeInfo);
            startActivity(intent);
        }

    }

    public String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);
            add = add + "," + obj.getAdminArea();
            add = add + "," + obj.getCountryName();

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
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



}