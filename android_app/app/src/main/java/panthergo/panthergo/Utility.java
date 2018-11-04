package panthergo.panthergo;

import android.content.Context;
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
}
