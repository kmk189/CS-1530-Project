package panthergo.panthergo;

public class AcademicBuilding extends Location {
    public String hoursOperation;

    public AcademicBuilding(){
        super();
        hoursOperation="";
    }

    public AcademicBuilding(String n, String d,double la, double lo, boolean v, String h){
        super(n,d,la,lo,v);
        hoursOperation=h;
    }

    public void setHoursOperation(String hoursOperation) {
        this.hoursOperation = hoursOperation;
    }
}
