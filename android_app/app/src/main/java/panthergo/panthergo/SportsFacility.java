package panthergo.panthergo;

public class SportsFacility extends Location {
    public String sports;
    public String teams;
    public String schedule;//url??

    public SportsFacility(){
        super();
        sports="";
        teams="";
        schedule="";
    }

    public SportsFacility(String n, String d,double la, double lo, boolean v, String sp, String t,
                          String sch,int id){
        super(n,d,la,lo,v,id);
        sports=sp;
        teams=t;
        schedule=sch;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public void setTeams(String teams) {
        this.teams = teams;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}

