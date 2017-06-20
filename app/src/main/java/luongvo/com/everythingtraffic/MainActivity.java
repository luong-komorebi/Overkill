package luongvo.com.everythingtraffic;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final int MYPERMISSION = 4111;
    private static final int reqCode = 4112;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide  title bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        setContentView(R.layout.activity_main);
        checkInternetConnection();
        checkGooglePlayService();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkForPermission();
        initComponent();
    }

    private void checkFirstTimeRun() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });
        t.run();
    }

    private void checkInternetConnection() {
        ConnectivityManager  cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ( netInfo == null || !netInfo.isConnectedOrConnecting() ) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("This app has limited functions without the internet connection.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private void checkGooglePlayService() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
        }
    }

    private void checkForPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCode);
            return;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MYPERMISSION) {
            if (grantResults.length < 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Please!!");
                alertDialog.setMessage("ALLOW map permission.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }

    private void initComponent() {
        checkFirstTimeRun();
        TextView appName = (TextView) findViewById(R.id.appName);
        TextView appDescription = (TextView) findViewById(R.id.appDescription);

        Button findRoute = (Button) findViewById(R.id.findRoute);
        Button freeMapView = (Button) findViewById(R.id.freeview);
        Button viewFavList = (Button) findViewById(R.id.viewFavList);
        Button viewOfflineMap = (Button) findViewById(R.id.offlineMap);
        Button viewVietnamOffline = (Button) findViewById(R.id.viewVietnamOffline);
        Button requestUber = (Button) findViewById(R.id.requestUber);
        Button viewNearByPlaces = (Button) findViewById(R.id.viewNearbyPlaces);


        Typeface fontForName = Typeface.createFromAsset(getAssets(), "fonts/vnfintro.ttf");
        Typeface fontForDescription = Typeface.createFromAsset(getAssets(), "fonts/Cabin-Regular.ttf");
        Typeface fontForButton = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue-Medium.otf");



        appName.setTypeface(fontForName);
        appDescription.setTypeface(fontForDescription);

        findRoute.setTypeface(fontForButton);
        freeMapView.setTypeface(fontForButton);
        viewFavList.setTypeface(fontForButton);
        viewOfflineMap.setTypeface(fontForButton);
        viewVietnamOffline.setTypeface(fontForButton);
        requestUber.setTypeface(fontForButton);
        viewNearByPlaces.setTypeface(fontForButton);


        findRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });



        freeMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity2.class);
                startActivity(intent);
            }
        });



        viewFavList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayFavList.class);
                startActivity(intent);
            }
        });



        viewOfflineMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayOfflineMap.class);
                startActivity(intent);
            }
        });



        viewVietnamOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayOfflineMap.class);
                Bundle b = new Bundle();
                b.putString("mapembed", "mapembed");
                intent.putExtras(b);
                startActivity(intent);
            }
        });



        requestUber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("CallUber", "CallUber");
                startActivity(intent);
            }
        });



        viewNearByPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewNearbyPlaces.class);
                startActivity(intent);
            }
        });

    }
}
