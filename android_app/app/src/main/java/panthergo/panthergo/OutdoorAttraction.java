package panthergo.panthergo;

public class OutdoorAttraction extends Location {
    public String type;
    public OutdoorAttraction(){
        super();
        type="";
    }

    public OutdoorAttraction(String n, String d,double la, double lo, boolean v, String t, int id){
        super(n,d,la,lo,v,id);
        type=t;
    }

    public OutdoorAttraction(String n, String d, double la, double lo, boolean v, int id) {
        super(n, d, la, lo, v, id);
    }

    public void setType(String type) {
        this.type = type;
    }
}
