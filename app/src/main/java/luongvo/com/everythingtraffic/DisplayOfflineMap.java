package luongvo.com.everythingtraffic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.glmapview.GLMapDownloadTask;
import com.glmapview.GLMapImage;
import com.glmapview.GLMapImageGroup;
import com.glmapview.GLMapInfo;
import com.glmapview.GLMapLocaleSettings;
import com.glmapview.GLMapManager;
import com.glmapview.GLMapMarkerLayer;
import com.glmapview.GLMapView;
import com.glmapview.MapPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import luongvo.com.everythingtraffic.OfflineMap.DownloadMap;

public class DisplayOfflineMap extends Activity implements GLMapView.ScreenCaptureCallback,  GLMapManager.StateListener{

    @Override
    public void onStartDownloading(GLMapInfo glMapInfo) {

    }

    @Override
    public void onDownloadProgress(GLMapInfo glMapInfo) {
        updateMapDownloadButtonText();
    }

    @Override
    public void onFinishDownloading(GLMapInfo glMapInfo) {
        mapView.reloadTiles();
    }

    @Override
    public void onStateChanged(GLMapInfo glMapInfo) {
        updateMapDownloadButtonText();
    }

//    class Pin
//    {
//        public MapPoint pos;
//        public int imageVariant;
//    }
//    private GLMapImage image=null;
//    private GLMapImageGroup imageGroup=null;
//    private List<Pin> pins = new ArrayList<>();
//    private GestureDetector gestureDetector;
    private GLMapView mapView;
    private GLMapInfo mapToDownload =null;
    private Button btnDownloadMap;

    GLMapMarkerLayer markerLayer;
    GLMapLocaleSettings localeSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        GLMapManager.initialize(this, this.getString(R.string.api_key), null);
        mapView = (GLMapView) this.findViewById(R.id.map_view);

        // Map list is updated, because download button depends on available map list and during first launch this list is empty
        GLMapManager.updateMapList(this, null);

        btnDownloadMap = (Button) this.findViewById(R.id.button_dl_map);
        btnDownloadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapToDownload != null) {
                    GLMapDownloadTask task = GLMapManager.getDownloadTask(mapToDownload);
                    if (task != null)
                    {
                        task.cancel();
                    } else {
                        GLMapManager.createDownloadTask(mapToDownload, DisplayOfflineMap.this).start();
                    }
                    updateMapDownloadButtonText();
                } else {
                    Intent i = new Intent(v.getContext(), DownloadMap.class);

                    MapPoint pt = mapView.getMapCenter();
                    i.putExtra("cx", pt.x);
                    i.putExtra("cy", pt.y);
                    v.getContext().startActivity(i);
                }
            }
        });

        GLMapManager.addStateListener(this);

        localeSettings = new GLMapLocaleSettings();
        mapView.setLocaleSettings(localeSettings);
        mapView.loadStyle(getAssets(), "DefaultStyle.bundle");
        mapView.setUserLocationImages(
                mapView.imageManager.open("DefaultStyle.bundle/circle-new.svgpb", 1, 0),
                mapView.imageManager.open("DefaultStyle.bundle/arrow-new.svgpb", 1, 0));

        mapView.setScaleRulerStyle(GLMapView.GLUnits.SI, GLMapView.GLMapPlacement.BottomCenter, new MapPoint(10, 10), 200);
        mapView.setAttributionPosition(GLMapView.GLMapPlacement.TopCenter);
        checkAndRequestLocationPermission();
        Bundle b = getIntent().getExtras();
        if (b!=null) {
            String c = b.getString("mapembed");
            if (!GLMapManager.AddMap(getAssets(), "49915.vm", null)) {
                //Failed to unpack to caches. Check free space.
            }
            zoomToPoint();
        }


        mapView.setCenterTileStateChangedCallback(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateMapDownloadButton();
                    }
                });
            }
        });



    }

    void zoomToPoint()
    {
        //New York
        //MapPoint pt = new MapPoint(-74.0059700 , 40.7142700	);

        //Belarus
        //MapPoint pt = new MapPoint(27.56, 53.9);
        //;

        // Move map to the Montenegro capital
        MapPoint pt = MapPoint.CreateFromGeoCoordinates(10.7771702,106.6932099);
        GLMapView mapView = (GLMapView) this.findViewById(R.id.map_view);
        mapView.setMapCenter(pt, false);
        mapView.setMapZoom(16, false);
    }

    public void checkAndRequestLocationPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        else
            mapView.setShowsUserLocation(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapView.setShowsUserLocation(true);
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


    @Override
    protected void onDestroy()
    {
        GLMapManager.removeStateListener(this);
        if(markerLayer!=null)
        {
            markerLayer.dispose();
            markerLayer = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MapPoint pt = new MapPoint(mapView.getWidth()/2, mapView.getHeight()/2);
        mapView.changeMapZoom(-1, pt, true);
        return false;
    }

    void updateMapDownloadButtonText()
    {
        if(btnDownloadMap.getVisibility()==View.VISIBLE)
        {
            MapPoint center = mapView.getMapCenter();

            mapToDownload = GLMapManager.MapAtPoint(center);

            if (mapToDownload != null)
            {
                String text;
                if(mapToDownload.getState() == GLMapInfo.State.IN_PROGRESS)
                {
                    text = String.format(Locale.getDefault(), "Downloading %s %d%%", mapToDownload.getLocalizedName(localeSettings), (int)(mapToDownload.getDownloadProgress()*100));
                }else
                {
                    text = String.format(Locale.getDefault(), "Download %s", mapToDownload.getLocalizedName(localeSettings));
                }
                btnDownloadMap.setText(text);
            } else {
                btnDownloadMap.setText("Download maps");
            }
        }
    }

    void updateMapDownloadButton()
    {
        switch (mapView.getCenterTileState())
        {
            case NoData:
            {
                if(btnDownloadMap.getVisibility()==View.INVISIBLE)
                {
                    btnDownloadMap.setVisibility(View.VISIBLE);
                    btnDownloadMap.getParent().requestLayout();
                    updateMapDownloadButtonText();
                }
                break;
            }

            case Loaded:
            {
                if(btnDownloadMap.getVisibility()==View.VISIBLE)
                {
                    btnDownloadMap.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case Unknown:
                break;
        }
    }

    @Override
    public void screenCaptured(Bitmap bitmap) {

    }
}
