package panthergo.panthergo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.reflect.Field;

public class Utility {
    /* Returns true if the device is connected to or connecting to the internet */
    public static boolean networkConnectionAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    /* For debugging: prints all the fields of an object and their values,
     * kind of like print(object.__dict__) in Python, but because Java's
      * so wonderful it requires 10 lines :DDD */
    public static void printObjectContents(Object object) {
        String out = "";
        for (Field field: object.getClass().getFields()) {
            String name = field.getName();
            Object value = new Object();
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            out += name + ": " + value + "\n";
        }
        System.out.println(out);
    }

    /* Displays an error alert stating that we cannot retrieve location data from our
     * back end at this time. context is the calling activity's context */
    public static void displayConnectionError(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.connectionErrorMsg)
                .setTitle("Connection Error")
                .setNeutralButton("OK", null)
                .show();
    }

    /* Displays a dialog asking the user if they want to learn more about a location. If they
     * confirm, an info box for that location is shown. Else the dialog is closed. context is
      * the calling activity's context */
    public static void displayLocationAlert(final Location location, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Would you like to learn about " + location.name + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewInfoBox(location.uuid, context);
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

    /* Displays an info box for the location with the provided UUID. Uses context to render
     * that information box; it should be the calling activity's context. */
    public static void viewInfoBox(String uuid, Context context) {
        Intent intent = new Intent(context, InfoBoxActivity.class);
        intent.putExtra("locationId", uuid);
        context.startActivity(intent);
    }

}
