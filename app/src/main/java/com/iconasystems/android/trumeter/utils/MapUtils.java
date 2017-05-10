package com.iconasystems.android.trumeter.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by MUSHABE on 6/4/2016.
 */
public class MapUtils {
    private static final String TAG_INSTRUCTION = "instruction";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_DISTANCE = "distance";

    public static Location getLocation(Context context, LocationListener locationListener, AppCompatActivity activity) {
        LocationManager locationManager;
        boolean isGPSEnabled;
        boolean isNetworkEnabled;
        boolean canGetLocation;
        Location location = null;
        double latitude;
        double longitude;
        boolean isFineGranted = false;
        boolean isCoarseGranted = false;
        try {
            PermissionUtils.requestPermission(activity, 12135, Manifest.permission.ACCESS_FINE_LOCATION, false);
            PermissionUtils.requestPermission(activity, 12134,Manifest.permission.ACCESS_COARSE_LOCATION, false);


            if (true){

                locationManager = (LocationManager) context
                        .getSystemService(Context.LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    canGetLocation = true;

                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                0,
                                0, locationListener);
                        Log.d("Network", "Network Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    0,
                                    0, locationListener);
                            Log.d("GPS", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


}