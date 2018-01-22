package com.example.piotr.pogoda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static String width;
    private static String height;
    private static String locationWidth;
    private static String locationHeight;


    private LocationManager locationManager;
    private LocationListener listener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setLocationWidth(Double.toString(location.getLatitude()));
                setLocationHeight(Double.toString(location.getLongitude()));
                setWidth(locationWidth);
                setHeight(locationHeight);

                Intent intent = new Intent(MainActivity.this, City.class);
                startActivity(intent);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        getLocation();
    }

    static String getWidth() {
        return width;
    }

    static void setWidth(String width) {
        MainActivity.width = width;
    }

    static String getHeight() {
        return height;
    }

    static void setHeight(String height) {
        MainActivity.height = height;
    }

    String getLocationWidth() {
        return locationWidth;
    }

    private void setLocationWidth(String locationWidth) {
        MainActivity.locationWidth = locationWidth;
    }

    String getLocationHeight() {
        return locationHeight;
    }

    private void setLocationHeight(String locationHeight) {
        MainActivity.locationHeight = locationHeight;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }*/


        locationManager.requestLocationUpdates("gps", 50000, 0, listener);
    }

}



