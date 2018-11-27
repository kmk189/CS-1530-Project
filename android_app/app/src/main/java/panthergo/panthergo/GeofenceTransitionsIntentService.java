package panthergo.panthergo;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();
    public static final int GEOFENCE_NOTIFICATION_ID = 0;

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

            displayLocationAlert(triggeringGeofencesIdsList.get(0));
            // Send notification and log the transition details.
            // sendNotification(geofenceTransitionDetails);
        }
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

    /* Displays an alert when in range of a location to ask if they want to view it*/
    public void displayLocationAlert(String location_uuid){
        ArrayList<Location> locations  = MapActivity.locations;
        HashMap<String, Location> locationMap = MapActivity.locationMap;

        final Location location = locationMap.get(location_uuid);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to learn about " + location.name + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        displayLocationInformation(location);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void displayLocationInformation(Location location){

        // Display the info_box layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View yourView = inflater.inflate(R.layout.info_box, null, false);

        // yourView.bringToFront();

        location.setVisited(true);
    }

}
