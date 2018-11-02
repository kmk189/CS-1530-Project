package panthergo.panthergo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Location> locations = new ArrayList<Location>();
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //load locations from the database into this.locations
        loadLocations();
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

    /* Retrieve basic information on tour locations from the back end */
    public void loadLocations() {
        //if there's no internet connection, just display a message saying we can't connect
        if (!Utility.networkConnectionAvailable(this)) {
            displayConnectionError();
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                //make a request to retrieve locations from database
                (Request.Method.GET, getString(R.string.baseURL) + "locations",
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseLocationResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if there's an error, display an alert stating something's gone wrong while
                        // getting locations
                        displayConnectionError();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    /* Displays an error alert stating that we cannot retrieve location data from our
     * back end at this time. */
    public void displayConnectionError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.connectionErrorMsg)
                .setTitle("Connection Error")
                .setNeutralButton("OK", null)
                .show();
    }

    public void parseLocationResponse(JSONObject response) throws JSONException {
        String[] locationTypes = {"Restaurant", "AcademicBuilding", "Museum", "OutdoorAttraction",
        "SportsFacility"};
        //for every type of location
        for (String locationType: locationTypes) {
            // retrieve an array of this type of location (restaurants, academic buildings, etc.)
            JSONArray locs = response.getJSONArray(locationType);

            // fill the locations array with the current type of location:
            // For every location in the JSON array
            for (int i = 0; i < locs.length(); i++) {
                JSONObject jsonLocation = locs.getJSONObject(i);
                Location newLocation = getLocation(locationType, jsonLocation.getString("name"),
                        "", jsonLocation.getDouble("latitude"),
                        jsonLocation.getDouble("longitude"), jsonLocation.getInt("id"));
                this.locations.add(newLocation);
            }
        }
    }

    /* Returns a location with the class named by the locationClass argument. The
     * other arguments supply information for that location */
    public Location getLocation(String locationClass, String name, String description, double lat,
                             double lon, int id) {
        // in future: retrieve visited from device memory
        boolean visited = false;
        // use Location (superclass) constructor to instantiate a location object with limited information
        if (locationClass.equals("Restaurant")) {
            return new Restaurant(name, description, lat, lon, visited, id);
        }
        else if (locationClass.equals("AcademicBuilding")) {
            return new AcademicBuilding(name, description, lat, lon, visited, id);
        }
        else if (locationClass.equals("Museum")) {
            return new Museum(name, description, lat, lon, visited, id);
        }
        else if (locationClass.equals("OutdoorAttraction")) {
            return new OutdoorAttraction(name, description, lat, lon, visited, id);
        }
        else if (locationClass.equals("SportsFacility")) {
            return new SportsFacility(name, description, lat, lon, visited, id);
        }
        return null;
    }

}
