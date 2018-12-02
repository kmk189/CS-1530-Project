package panthergo.panthergo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

/* The dialogs here must be in their own activity as they're being called from an IntentService.
 * If they're created in the intent service or down a call chain it started, nothing will happen. */
public class LocationAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retrieve the location passed in by the caller
        Location location = (Location)(getIntent().getSerializableExtra("location"));
        // display an alert dialog asking the user if they want to learn more about location
        displayAlertDialog(location);
    }

    /* Displays an info box for the location with the provided UUID */
    public void viewInfoBox(String uuid) {
        Intent intent = new Intent(this, InfoBoxActivity.class);
        intent.putExtra("locationId", uuid);
        startActivity(intent);
        this.finish(); //close this activity
    }

    public void displayAlertDialog(final Location location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to learn about " + location.name + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // record the visit to the location
                        LocationVisitHandler.recordLocationVisit(location, MapActivity.locationMap,
                                MapActivity.visitedLocations);
                        viewInfoBox(location.uuid);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        LocationAlertActivity.this.finish(); //close this activity
                    }
                });
        builder.show();
    }
}
