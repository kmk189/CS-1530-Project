package panthergo.panthergo;

public class Location {
    public String name;
    public String description;
    public double latitude;
    public double longitude;
    public boolean visited;
    public int id;

    public Location(){
        name="";
        description="";
        latitude=0;
        longitude=0;
        visited=false;
    }

    public Location(String n, String d,double la, double lo, boolean v, int id){
        name=n;
        description=d;
        latitude=la;
        longitude= lo;
        visited=v;
        this.id = id;
    }

    public Location(Location location) {
        name = location.name;
        description = location.description;
        latitude = location.latitude;
        longitude = location.longitude;
        visited = location.visited;
        id = location.id;
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
}
