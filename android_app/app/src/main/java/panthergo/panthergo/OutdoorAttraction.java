package panthergo.panthergo;

public class OutdoorAttraction extends Location {
    public String type;
    public OutdoorAttraction(){
        super();
        type="";
    }

    public OutdoorAttraction(String n, String d,double la, double lo, boolean v, String t){
        super(n,d,la,lo,v);
        type=t;
    }

    public void setType(String type) {
        this.type = type;
    }
}
