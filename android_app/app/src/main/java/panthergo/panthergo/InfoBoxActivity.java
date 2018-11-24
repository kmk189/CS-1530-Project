package panthergo.panthergo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class InfoBoxActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_box);
        // retrieve the UUID of the location to display information for
        String uuid = getIntent().getStringExtra("locationId");
        // retrieve the Location object
        Location loc = MapActivity.locationMap.get(uuid);
        // get more detailed information about this location and adjust the
        // contents of the display accordingly
        viewLocationInfo(loc);
    }

    /* Retrieve data for location and display its info on the screen */
    public void viewLocationInfo(final Location location) {
        // obtain a URL from which we can get detailed info about location
        String urlPath = getString(R.string.baseURL) + "locations/" + getPluralClassName(location) +
                "/" + location.id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlPath,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Location fullLocationInfo = getLocationDetailsFromJSON(location, response);
                            fillInfoBoxFields(fullLocationInfo);
                            // TODO: use fullLocationInfo to display window?
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if there's an error, display an alert stating something's gone wrong while
                        // getting locations
                        Utility.displayConnectionError(InfoBoxActivity.this);
                    }
                });
        // make the request
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    /* Fills the text views in the info box with fields appropriate for the subclass of location
     * that locationInfo belongs to. */
    public void fillInfoBoxFields(Location locationInfo) {
        ((TextView)findViewById(R.id.infoBoxLocationName)).setText(locationInfo.name);
        ((TextView)findViewById(R.id.infoBoxLocationDescription)).setText(locationInfo.description);

        TextView infoBoxField1 = (TextView)findViewById(R.id.infoBoxField1);
        TextView infoBoxField2 = (TextView)findViewById(R.id.infoBoxField2);
        TextView infoBoxField3 = (TextView)findViewById(R.id.infoBoxField3);
        TextView infoBoxLabel1 = (TextView)findViewById(R.id.infoBoxLabel1);
        TextView infoBoxLabel2 = (TextView)findViewById(R.id.infoBoxLabel2);
        TextView infoBoxLabel3 = (TextView)findViewById(R.id.infoBoxLabel3);
        if (locationInfo instanceof AcademicBuilding || locationInfo instanceof Museum ||
                locationInfo instanceof Restaurant) {
            infoBoxLabel1.setText("Hours: ");
        }
        if (locationInfo instanceof AcademicBuilding) {
            infoBoxField1.setText(((AcademicBuilding) locationInfo).hoursOperation);
        }
        else if (locationInfo instanceof Museum) {
            infoBoxField1.setText(((Museum) locationInfo).hoursOperation);
            infoBoxLabel2.setText("Price: ");
            infoBoxField2.setText(((Museum) locationInfo).price);
        }
        else if (locationInfo instanceof Restaurant) {
            infoBoxField1.setText(((Restaurant) locationInfo).hoursOperation);
            infoBoxLabel2.setText("Menu: ");
            infoBoxField2.setText(((Restaurant) locationInfo).menu);
        }
        else if (locationInfo instanceof SportsFacility) {
            infoBoxLabel1.setText("Sports: ");
            infoBoxLabel2.setText("Teams: ");
            infoBoxLabel3.setText("Schedule: ");
            infoBoxField1.setText(((SportsFacility) locationInfo).sports);
            infoBoxField2.setText(((SportsFacility) locationInfo).teams);
            infoBoxField3.setText(((SportsFacility) locationInfo).schedule);
        }
        else if (locationInfo instanceof OutdoorAttraction) {
            infoBoxLabel1.setText("Type: ");
            infoBoxField1.setText(((OutdoorAttraction) locationInfo).type);
        }
    }

    /* Returns a valid plural form of the location argument's class. Needed to determine
     * the URL for obtaining the detailed information of a location. */
    public String getPluralClassName(Location location) {
        if (location instanceof Restaurant || location instanceof AcademicBuilding ||
                location instanceof OutdoorAttraction || location instanceof Museum) {
            return location.getClass().getSimpleName() + "s";
        }
        //stupid English...
        else if (location instanceof SportsFacility) {
            return "SportsFacilities";
        }
        else {
            throw new IllegalArgumentException("Invalid location subclass detected.");
        }
    }

    /* Returns a Location with the same class as the location argument and data derived
     * from the locationInfo JSONObject. */
    public Location getLocationDetailsFromJSON(Location location, JSONObject locationInfo) throws JSONException {
        Location locationData = null;
        if (location instanceof Restaurant) {
            locationData = new Restaurant();
            ((Restaurant)locationData).hoursOperation = locationInfo.getString("hoursOperation");
            ((Restaurant)locationData).menu = locationInfo.getString("menu");
        }
        else if (location instanceof AcademicBuilding) {
            locationData = new AcademicBuilding();
            ((AcademicBuilding)locationData).hoursOperation = locationInfo.getString("hoursOperation");
        }
        else if (location instanceof Museum) {
            locationData = new Museum();
            ((Museum) locationData).hoursOperation = locationInfo.getString("hours");
            ((Museum) locationData).price = locationInfo.getString("price");
        }
        else if (location instanceof OutdoorAttraction) {
            locationData = new OutdoorAttraction();
            ((OutdoorAttraction) locationData).type = locationInfo.getString("type");
        }
        else if (location instanceof SportsFacility) {
            locationData = new SportsFacility();
            ((SportsFacility) locationData).sports = locationInfo.getString("sports");
            ((SportsFacility) locationData).teams = locationInfo.getString("teams");;
            ((SportsFacility) locationData).schedule = locationInfo.getString("schedule");;
        }
        locationData.setDescription(locationInfo.getString("description"));
        locationData.setName(locationInfo.getString("name"));
        locationData.setId(locationInfo.getInt("id"));
        locationData.setLatitude(locationInfo.getDouble("latitude"));
        locationData.setLongitude(locationInfo.getDouble("longitude"));
        return locationData;
    }

    /* The onClick method for the Close button. Closes the window and activity. */
    public void close(View view) {
        this.finish();
    }
}
