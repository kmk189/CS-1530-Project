package panthergo.panthergo;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.PendingIntent.getActivity;
import static com.google.android.gms.location.Geofence.NEVER_EXPIRE;

public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback, OnMarkerClickListener {

    private GoogleMap mMap;
    public static ArrayList<Location> locations = new ArrayList<Location>();
    public static HashMap<String, Location> locationMap = new HashMap<>(); //maps uuid to location obj
    public static ArrayList<Location> visitedLocations = new ArrayList<>();
    private List<Geofence> mGeofenceList = new ArrayList<Geofence>();
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GeofencingClient mGeofencingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // set the context in LocationVisitHandler so it can access visited locations on disk
        LocationVisitHandler.context = getApplicationContext();
        // load locations from the database into this.locations
        loadLocations();
        mGeofencingClient = LocationServices.getGeofencingClient(this);
    }


    /**
     * Manipulates the map once available
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //zoom in so we can properly see the campus
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        // Move camera to the Cathedral
        LatLng cathy = new LatLng(40.443704, -79.953886);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cathy));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {//Access Fine Location ==1
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the

                    // location task you need to do.
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    /* Retrieve basic information on all tour locations from the back end */
    public void loadLocations() {
        //if there's no internet connection, just display a message saying we can't connect
        if (!Utility.networkConnectionAvailable(this)) {
            Utility.displayConnectionError(this);
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                // construct a request to retrieve locations from database
                (Request.Method.GET, getString(R.string.baseURL) + "locations",
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // update the ArrayList and HashMap of locations
                            parseLocationResponse(response);
                            // now that we can be sure locationMap is filled with data, update visitedLocations
                            visitedLocations = LocationVisitHandler.getVisitedLocationsList(locationMap);
                            // set all the visited locations' visited status to true
                            for (Location loc: visitedLocations) {
                                loc.setVisited(true);
                            }
                            for (int i = 0; i < locations.size(); i++){
                                Location currLocation = locations.get(i);
                                addMarker(currLocation.id, currLocation.name, currLocation.latitude, currLocation.longitude);
                                addGeofenceToList(currLocation.id, currLocation.latitude, currLocation.longitude, 100);
                                // TODO: Use this.locations to load map markers?
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if there's an error, display an alert stating something's gone wrong while
                        // getting locations
                        Utility.displayConnectionError(MapActivity.this);
                    }
                });
        // make request
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    /* Fill the locations array with all locations in the response argument. */
    public void parseLocationResponse(JSONObject response) throws JSONException {
        String[] locationTypes = {"Restaurant", "AcademicBuilding", "Museum", "OutdoorAttraction",
        "SportsFacility"};
        //for every type of location
        for (String locationType: locationTypes) {
            // retrieve an array of this type of location (restaurants, academic buildings, etc.)
            JSONArray locations = response.getJSONArray(locationType);

            // fill the locations array with the current type of location:
            // For every location in the JSON array
            for (int i = 0; i < locations.length(); i++) {
                JSONObject jsonLocation = locations.getJSONObject(i);
                // parse the JSON object to a Location
                Location newLocation = getLocation(locationType, jsonLocation.getString("name"),
                        "", jsonLocation.getDouble("latitude"),
                        jsonLocation.getDouble("longitude"), jsonLocation.getInt("id"),
                        jsonLocation.getString("uuid"));
                // add the location to the locations array
                this.locations.add(newLocation);
                locationMap.put(newLocation.uuid, newLocation);
            }
        }
    }

    /* Returns a location with the class named by the locationClass argument. The
     * other arguments supply information for that location */
    public Location getLocation(String locationClass, String name, String description, double lat,
                             double lon, int id, String uuid) {
        // in future: retrieve visited from device memory
        boolean visited = false;
        // use Location (superclass) constructor to instantiate a location object with limited information
        if (locationClass.equals("Restaurant")) {
            return new Restaurant(name, description, lat, lon, visited, id, uuid);
        }
        else if (locationClass.equals("AcademicBuilding")) {
            return new AcademicBuilding(name, description, lat, lon, visited, id, uuid);
        }
        else if (locationClass.equals("Museum")) {
            return new Museum(name, description, lat, lon, visited, id, uuid);
        }
        else if (locationClass.equals("OutdoorAttraction")) {
            return new OutdoorAttraction(name, description, lat, lon, visited, id, uuid);
        }
        else if (locationClass.equals("SportsFacility")) {
            return new SportsFacility(name, description, lat, lon, visited, id, uuid);
        }
        return null;
    }

    public void addMarker(int id, String location_name, double latitude, double longitude){
        LatLng latlng = new LatLng(latitude, longitude);
        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(latlng)
                                .title(location_name)
        );
        marker.setTag(id);
    }

    @Override
    public boolean onMarkerClick(Marker marker){
        // true if default behavior should not happen (centering and opening the info)
        // false if default behavior should happen
        return false;
    }

    /* Creates a geofence and adds it to the List
    *  @param id = integer id number
    *  @param latitude = double latitude value
    *  @param longitude = double longitude value
    *  @param radius = float value for the radius (in meters)*/
    public void addGeofenceToList(int id, double latitude, double longitude, float radius){
        mGeofenceList.add(new Geofence.Builder()
                .setRequestId(Integer.toString(id))
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(NEVER_EXPIRE)
                .setLoiteringDelay(5000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        );
    }


    private GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    public void setupGeofences(){
        if(checkPermission()) {
            mGeofencingClient.addGeofences(getGeofencingRequest(), getGetGeofencePendingIntent())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    private final int GEOFENCE_REQ_CODE = 0;
    PendingIntent mGeofencePendingIntent = null;
    private PendingIntent getGetGeofencePendingIntent(){
        if(mGeofencePendingIntent != null){
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public void launchVisitedActivity(View view) {
        Intent intent = new Intent(this, VisitedActivity.class);
        //launch the map activity
        startActivity(intent);
    }
}
