package com.protosaider.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener {

    private static final String TAG = "Logger";

    private GoogleMap mMap;
    public ArrayList<LatLng> latLngArrayList;
    Polyline polyline;

    Bitmap airship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latLngArrayList = new ArrayList<LatLng>(10);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.maps_menu_clean:
                polyline.remove();
                //for (LatLng latLng : latLngArrayList){}
                latLngArrayList.clear();
                mMap.clear();
                polyline = mMap.addPolyline(new PolylineOptions().geodesic(true));
                break;
            case R.id.maps_menu_anim:
                Animate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Animate() {

        if (latLngArrayList.size() < 2)
            return;

        airship = BitmapFactory.decodeResource(getResources(), R.drawable.airship60);

        Marker animMarker = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(airship))
                .position(latLngArrayList.get(0)));
        Log.d(TAG, "in animate");

        startAnimation(animMarker, latLngArrayList.get(0), latLngArrayList.get(1));
        Log.d(TAG, "in animate after start anim");
        /*
        for (int i = 0; i < latLngArrayList.size() - 1;) {
            startAnimation(animMarker, latLngArrayList.get(i), latLngArrayList.get(i + 1));
            Log.d(TAG, "in animate after start anim");
        }*/
    }


    public void startAnimation(final Marker animMarker, final LatLng startLatLng, final LatLng endLatLng) {

        Log.d(TAG, "in start anim");
        final Handler mHandler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new LinearInterpolator();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float)elapsed / duration);
                Log.d(TAG, "in runnable");
                double lng = t * endLatLng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * endLatLng.latitude + (1 - t) * startLatLng.latitude;
                animMarker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    mHandler.postDelayed(this, 16);
                    Log.d(TAG, "in t < 1.0 (postDelayed)");
                }
                else
                {
                    Log.d(TAG, "!!! t >= 1.0 (postDelayed)");
	                animMarker.remove();
                }
            }
        });
    }


    @Override
    public void onMapClick(LatLng point) {
        mMap.addMarker(new MarkerOptions().position(point).title("checking"));

        latLngArrayList.add(point);
        Toast.makeText(MapsActivity.this, "success" + point.toString(), Toast.LENGTH_LONG).show();
            polyline.setPoints(latLngArrayList);
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
        mMap.setOnMapClickListener(this);
        mMap.setPadding(0, 300, 300, 0);
        polyline = mMap.addPolyline(new PolylineOptions().geodesic(true));
    }
}
