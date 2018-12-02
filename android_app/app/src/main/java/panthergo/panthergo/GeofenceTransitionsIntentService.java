package panthergo.panthergo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();
    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    public static Context mapContext; // we cannot access MapActivity.this in here, but we need it.
                                      // this variable is set in MapActivity

    public GeofenceTransitionsIntentService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }
        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
            for(Geofence geofence : triggeringGeofences){
                triggeringGeofencesIdsList.add(geofence.getRequestId());
            }
            // retrieve closest location that triggered a geofence event
            Location closestLocation = getClosestLocation(triggeringGeofencesIdsList);
            Utility.displayLocationAlert(closestLocation, mapContext);
        }
    }

    /* Of the locations with UUIDs in triggeringLocationIds, this method finds the one that's
     * closest to the user. Ties are handled by arbitrarily picking one. */
    private Location getClosestLocation(ArrayList<String> triggeringLocationIds) {
        // get the user's last-known location
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // if the geofences are going off, we can access fine location--no need to check for permission
        @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        double userLat = lastKnownLocation.getLatitude();
        double userLong = lastKnownLocation.getLongitude();
        double minDist = Double.MAX_VALUE; // minimum distance between user and a triggering location
        Location closestLocation = null;
        for (String uuid: triggeringLocationIds) {
            float[] results = new float[1];
            Location triggeringLocation = MapActivity.locationMap.get(uuid);
            // store the distance between the user's location and triggeringLocation in results[0]
            android.location.Location.distanceBetween(userLat, userLong,
                    triggeringLocation.latitude, triggeringLocation.longitude, results);
            double distanceBetween = results[0];
            if (distanceBetween < minDist) {
                minDist = distanceBetween;
                closestLocation = triggeringLocation;
            }
        }
        return closestLocation;
    }

    private String getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> triggeringGeofences){
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for(Geofence geofence : triggeringGeofences){
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }

        String status = null;
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            status = "Entering";
        }
        else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            status = "Exiting";
        }
        return status + TextUtils.join(", ", triggeringGeofencesIdsList);
    }

    private static String getErrorString(int errorCode){
        switch(errorCode){
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "Geofence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many Geofences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error";
        }
    }
}
