package com.protosaider.myapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener {

    private GoogleMap mMap;
    public ArrayList<LatLng> latLngArrayList;
    //Marker addedMarker;
    Polyline polyline;

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
                //???
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
