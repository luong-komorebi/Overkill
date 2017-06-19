package luongvo.com.everythingtraffic;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int MYPERMISSION = 4111;
    private static final int reqCode = 4112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide  title bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        setContentView(R.layout.activity_main);
        checkForPermission();
        initComponent();
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
        TextView appName = (TextView) findViewById(R.id.appName);
        TextView appDescription = (TextView) findViewById(R.id.appDescription);


        Typeface fontForName = Typeface.createFromAsset(getAssets(), "fonts/vnfintro.ttf");
        appName.setTypeface(fontForName);

        Typeface fontForDescription = Typeface.createFromAsset(getAssets(), "fonts/Cabin-Regular.ttf");
        appDescription.setTypeface(fontForDescription);

        Typeface fontForButton = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue-Medium.otf");

        Button findRoute = (Button) findViewById(R.id.findRoute);
        findRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        findRoute.setTypeface(fontForButton);

        Button freeMapView = (Button) findViewById(R.id.freeview);
        freeMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity2.class);
                startActivity(intent);
            }
        });
        freeMapView.setTypeface(fontForButton);

        Button viewFavList = (Button) findViewById(R.id.viewFavList);
        viewFavList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayFavList.class);
                startActivity(intent);
            }
        });
        viewFavList.setTypeface(fontForButton);

        Button viewOfflineMap = (Button) findViewById(R.id.offlineMap);
        viewOfflineMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayOfflineMap.class);
                startActivity(intent);
            }
        });
        viewOfflineMap.setTypeface(fontForButton);

        Button viewVietnamOffline = (Button) findViewById(R.id.viewVietnamOffline);
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
        viewVietnamOffline.setTypeface(fontForButton);

        Button requestUber = (Button) findViewById(R.id.requestUber);
        requestUber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("CallUber", "CallUber");
                startActivity(intent);
            }
        });
        requestUber.setTypeface(fontForButton);
    }
}
