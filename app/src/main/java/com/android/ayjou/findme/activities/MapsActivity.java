package com.android.ayjou.findme.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressWarnings({"MissingPermission"})
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

//    private final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 0;
    private final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private final long MIN_TIME_BW_UPDATES = 10000;
    private final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            prepareMap();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
//            case MY_PERMISSION_ACCESS_COARSE_LOCATION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    prepareMap();
//                } else {
//                    System.exit(1);
//                }
//                break;

            case MY_PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prepareMap();
                } else {
                    System.exit(1);
                }
                break;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near last position, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        LatLng position = new LatLng(48, 2);

        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
//        LatLng position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        if (lastLocation == null) {
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        myMarker = mMap.addMarker(new MarkerOptions().position(position).title("Last known position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    protected void prepareMap() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        setContentView(com.android.ayjou.findme.activities.R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(com.android.ayjou.findme.activities.R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onLocationChanged(Location var1) {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        System.out.println(location.getLatitude() + " - " + location.getLongitude());
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        myMarker.setPosition(latlng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    }

    public void onStatusChanged(String var1, int var2, Bundle var3) {}

    public void onProviderEnabled(String var1) {}

    public void onProviderDisabled(String var1) {}
}
