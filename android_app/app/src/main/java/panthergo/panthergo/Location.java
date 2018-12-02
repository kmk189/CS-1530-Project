package panthergo.panthergo;

import java.io.Serializable;

public class Location implements Serializable {
    public String name;
    public String description;
    public double latitude;
    public double longitude;
    public boolean visited;
    public int id;
    public String uuid;

    public Location(){
        name="";
        description="";
        latitude=0;
        longitude=0;
        visited=false;
    }

    public Location(String n, String d,double la, double lo, boolean v, int id, String uuid){
        name=n;
        description=d;
        latitude=la;
        longitude= lo;
        visited=v;
        this.id = id;
        this.uuid = uuid;
    }

    public Location(Location location) {
        name = location.name;
        description = location.description;
        latitude = location.latitude;
        longitude = location.longitude;
        visited = location.visited;
        id = location.id;
        uuid = location.uuid;
    }

    public void setName(String n){
        name=n;
    }
    public void setDescription(String d){
        description=d;
    }
    public void setLatitude(double la){
        latitude=la;
    }
    public void setLongitude(double lo) {
        longitude = lo;
    }
    public void setVisited(boolean v) {
        visited = v;
    }
    public void setId(int id) { this.id = id; };

    public Location copy() {
        return new Location(this);
    }

    @Override
    /* Two locations are considered equal if they have the same UUID (correspond to the same resource).
     * Note: the behavior of this method currently prevents duplicate locations being added
     * to the visited locations list in LocationVisitHandler.addLocationToVisitedList(). It makes it
     * so that two Location objects don't have to have the same pointer to be considered equal.
    */
    public boolean equals(Object obj) {
        return uuid.equals(((Location)obj).uuid);
    }
}
