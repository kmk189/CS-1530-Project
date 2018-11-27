package panthergo.panthergo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LocationVisitHandler {

    public static Context context;
    private static final String visitedLocationsSetName = "visitedLocationsSet"; // name of set of
                                                            // visited locations in device storage

    /* Calls several helper functions to properly record in the storage and application context
     * that a location has been visited.
      * location is an object of class Location, locationMap is a hash map mapping all locations
      * to their UUIDs, and visitedLocations is an ArrayList containing locations the user has
      * visited. The context must be a context from the MapActivity. */
    public static void recordLocationVisit(Location location, HashMap<String, Location> locationMap,
                                          ArrayList<Location> visitedLocations) {
            markLocationVisited(location.uuid, locationMap);
            addLocationToVisitedList(location, visitedLocations);
            saveVisitedStatusToDevice(location.uuid);
    }

    /* Retrieves an ArrayList of visited locations from device storage. */
    public static ArrayList<Location> getVisitedLocationsList(HashMap<String, Location> locationMap) {
        ArrayList<Location> locations = new ArrayList<>();
        // retrieve UUIDs of stored locations from storage
        Set<String> storedUUIDs = getVisitedLocationUUIDs();
        for (String uuid: storedUUIDs) {
            // if the UUID corresponds to one of our loaded locations, add that location to the list
            // (if it doesn't, ignore it because it was probably just deleted from the database)
            if (locationMap.containsKey(uuid)) {
                locations.add(locationMap.get(uuid));
            }
        }
        return locations;
    }

    /* Sets the visited attribute to true for the location in locationMap with uuid as its UUID. */
    private static void markLocationVisited(String uuid, HashMap<String, Location> locationMap) {
        if (locationMap.containsKey(uuid)) {
            locationMap.get(uuid).setVisited(true);
        }
        else {
            throw new UnsupportedOperationException("Location UUID was not found in location hashmap.");
        }
    }

    private static void addLocationToVisitedList(Location loc, ArrayList<Location> visitedLocs) {
        if (!visitedLocs.contains(loc)) {
            visitedLocs.add(loc);
        }
    }

    /* Saves the String uuid (a location uuid) within storage in
     * the set named visitedLocationsSetName (a constant declared above).
      * If no such set exists, it is created. If the uuid is already present in the set, nothing
      * happens. */
    private static void saveVisitedStatusToDevice(String uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID must not be null");
        }
        // retrieve set of UUID strings from memory
        Set<String> visitedIDs = getVisitedLocationUUIDs();
        // if the String uuid has not yet been stored in device's set of visited IDs, then store it
        if (!visitedIDs.contains(uuid)) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            // copy the set from storage because it itself cannot be modified reliably (acc. to the docs)
            visitedIDs = new HashSet<String>(visitedIDs);
            visitedIDs.add(uuid); //add uuid to set
            // save the set to device storage
            editor.putStringSet(visitedLocationsSetName, visitedIDs);
            editor.apply(); // write asynchronously so the UI doesn't freeze
        }
    }

    /* Retrieve the set of visited location UUIDs stored in the device. If none exists, return
     * an empty set. */
    private static Set<String> getVisitedLocationUUIDs() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getStringSet(visitedLocationsSetName, new HashSet<String>());
    }

}
