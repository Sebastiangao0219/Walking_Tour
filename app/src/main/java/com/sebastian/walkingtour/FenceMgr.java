package com.sebastian.walkingtour;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.graphics.ColorUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FenceMgr {
    private static final String TAG = "FenceMgr";
    private final MapsActivity mapsActivity;
    private final GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private final ArrayList<Circle> circles = new ArrayList<>();
    private final List<PatternItem> pattern = Collections.singletonList(new Dot());
    private static final ArrayList<FenceData> fenceList = new ArrayList<>();

    FenceMgr(final MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
        geofencingClient = LocationServices.getGeofencingClient(mapsActivity);

        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(mapsActivity, aVoid -> Log.d(TAG, "onSuccess: removeGeofences"))
                .addOnFailureListener(mapsActivity, e -> {
                    Log.d(TAG, "onFailure: removeGeofences");
                });

        new Thread(new FenceDataDownloader(mapsActivity, this)).start();
    }

    static FenceData getFenceData(String id) {
        for (FenceData fd : fenceList) {
            if (fd.getId().equals(id))
                return fd;
        }
        return null;
    }

    void drawFences() {
        for (FenceData fd : fenceList) {
            drawFence(fd);
        }
    }

    void eraseFences() {
        for (Circle c : circles)
            c.remove();
        circles.clear();
    }

    private void drawFence(FenceData fd) {

        int color = Color.parseColor(fd.getFenceColor());
        int fill = ColorUtils.setAlphaComponent(color, 85);

        LatLng latLng = new LatLng(fd.getLatitude(), fd.getLongitude());
        Circle c = mapsActivity.getMap().addCircle(new CircleOptions()
                .center(latLng)
                .radius(fd.getRadius())
                .strokePattern(pattern)
                .strokeColor(color)
                .fillColor(fill));

        circles.add(c);
    }

    void addFences(ArrayList<FenceData> fences) {

        fenceList.clear();
        fenceList.addAll(fences);

        for (FenceData fd : fenceList) {
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(fd.getId())
                    .setCircularRegion(
                            fd.getLatitude(),
                            fd.getLongitude(),
                            fd.getRadius())
                    .setNotificationResponsiveness(1000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE) //Fence expires after N millis  -or- Geofence.NEVER_EXPIRE
                    .build();

            GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                    .addGeofence(geofence)
                    .build();

            geofencePendingIntent = getGeofencePendingIntent();

            if (ActivityCompat.checkSelfPermission(mapsActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            geofencingClient
                    .addGeofences(geofencingRequest, geofencePendingIntent)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: addGeofences"))
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Log.d(TAG, "onFailure: addGeofences");
                    });
        }
        mapsActivity.runOnUiThread(this::drawFences);
    }

    private PendingIntent getGeofencePendingIntent() {

        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Intent intent = new Intent(mapsActivity, GeofenceBroadcastReceiver.class);

        geofencePendingIntent = PendingIntent.getBroadcast(mapsActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
}
