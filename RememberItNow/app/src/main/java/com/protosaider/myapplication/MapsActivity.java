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
    //Marker addedMarker;
    Polyline polyline;

    Bitmap airship;
    int imageHeight;
    int imageWidth;
    String imageType;
    //Marker animMarker;
    //ImageView mTargetImageView;

    Handler mHandler;
    final int START_ANIM = 0;
    final int END_ANIM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latLngArrayList = new ArrayList<LatLng>(10);

       /* mHandler = new Handler() {
            public void handleMessage(Message msg){
                switch (msg.what)
                {
                    case START_ANIM:
                        Toast.makeText(MapsActivity.this, "start animation" + msg.what, Toast.LENGTH_SHORT).show();
                        Anim(airship, latLngArrayList.get(0));
                        break;
                    case END_ANIM:
                        Toast.makeText(MapsActivity.this, "end animation" + msg.what + "agr1 = " + msg.arg1 + "agr2 = " + msg.arg2, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }; */

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
                //startAnimation(latLngArrayList.get(0), latLngArrayList.get(1));
                Animate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Animate() {

        if (latLngArrayList.isEmpty())
            return;

        airship = BitmapFactory.decodeResource(getResources(), R.drawable.airship60);

        Marker animMarker = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(airship))
                .position(latLngArrayList.get(0)));
        Log.d(TAG, "in animate");

        //startAnimation(animMarker, latLngArrayList.get(0), latLngArrayList.get(1));

        for (int i = 0; i < latLngArrayList.size() - 1;) {
            startAnimation(animMarker, latLngArrayList.get(i), latLngArrayList.get(i + 1));
            Log.d(TAG, "in animate after start anim");
        }
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
                }
            }
        });
    }

   /* public void startAnimation(LatLng startLatLng, LatLng toPosition) {

        Thread thread = new Thread(new Runnable() {
            Message msg;

            public void run() {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true; //avoids memory allocation, returning null for the bitmap object but setting outWidth, outHeight and outMimeType.
                    BitmapFactory.decodeResource(getResources(), R.drawable.airship60, options);
                    imageHeight = options.outHeight;
                    imageWidth = options.outWidth;
                    imageType = options.outMimeType;

                    options.inJustDecodeBounds = false;
                    airship = BitmapFactory.decodeResource(getResources(), R.drawable.airship60, options);

                    mHandler.sendEmptyMessage(START_ANIM);

                    final long start = SystemClock.uptimeMillis();
                    final long duration = 1500;

                    final Interpolator interpolator = new LinearInterpolator();

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);

                    double lng = t * toPosition.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * toPosition.latitude + (1 - t)
                            * startLatLng.latitude;

                    markerAirship.setPosition(new LatLng(lat, lng));

                    if (t < 1.0) {
                        // Post again 16ms later.
                        mHandler.sendEmptyMessage(START_ANIM);
                        mHandler.postDelayed(this, 16);
                    }

                    TimeUnit.SECONDS.sleep(3);


                    msg = mHandler.obtainMessage(END_ANIM, 10, 0);
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public Marker Anim(Bitmap bmp, LatLng latLng) {
        return mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                .anchor(0.5f, 0.5f));

    }
*/

    @Override
    public void onMapClick(LatLng point) {
        //addedMarker = new MarkerOptions().position(point).title("checking");
        mMap.addMarker(new MarkerOptions().position(point).title("checking"));
        //markerArrayList.add(mMap.addMarker(new MarkerOptions().position(point).title("checking")));
        //if (!markerArrayList.isEmpty())

        latLngArrayList.add(point);
        Toast.makeText(MapsActivity.this, "success" + point.toString(), Toast.LENGTH_LONG).show();
            polyline.setPoints(latLngArrayList);
            //polyline = mMap.addPolyline(new PolylineOptions().geodesic(true));
            //if (polyline == null)

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
        //!!!
        //setMapType
        // Add a marker in Sydney and move the camera
        mMap.setOnMapClickListener(this);
        mMap.setPadding(0, 300, 300, 0);
        LatLng sydney = new LatLng(-34, 151); //широта\долгота
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        polyline = mMap.addPolyline(new PolylineOptions().geodesic(true));
    }
}
