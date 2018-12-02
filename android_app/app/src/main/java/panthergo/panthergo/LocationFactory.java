package panthergo.panthergo;

public class LocationFactory {
    /* Returns a location with the class named by the locationClass argument. The
     * other arguments supply information for that location */
    public static Location getLocation(String locationClass, String name, String description, double lat,
                                double lon, int id, String uuid) {
        // in future: retrieve visited from device memory [edit: taken care of elsewhere]
        boolean visited = false;
        // use Location (superclass) constructor to instantiate a location object with limited information
        if (locationClass.equals("Restaurant")) {
            return new Restaurant(name, description, lat, lon, visited, id, uuid);
        } else if (locationClass.equals("AcademicBuilding")) {
            return new AcademicBuilding(name, description, lat, lon, visited, id, uuid);
        } else if (locationClass.equals("Museum")) {
            return new Museum(name, description, lat, lon, visited, id, uuid);
        } else if (locationClass.equals("OutdoorAttraction")) {
            return new OutdoorAttraction(name, description, lat, lon, visited, id, uuid);
        } else if (locationClass.equals("SportsFacility")) {
            return new SportsFacility(name, description, lat, lon, visited, id, uuid);
        }
        return null;
    }
}
