package panthergo.panthergo;

public class AcademicBuilding extends Location {
    public String hoursOperation;

    public AcademicBuilding(){
        super();
        hoursOperation="";
    }

    public AcademicBuilding(String n, String d,double la, double lo, boolean v, String h, int id, String uuid){
        super(n,d,la,lo,v,id,uuid);
        hoursOperation=h;
    }

    public AcademicBuilding(String n, String d, double la, double lo, boolean v, int id, String uuid) {
        super(n, d, la, lo, v, id, uuid);
    }

    public void setHoursOperation(String hoursOperation) {
        this.hoursOperation = hoursOperation;
    }
}
