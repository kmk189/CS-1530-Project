package panthergo.panthergo;

public class OutdoorAttraction extends Location {
    public String type;
    public OutdoorAttraction(){
        super();
        type="";
    }

    public OutdoorAttraction(String n, String d,double la, double lo, boolean v, String t, int id, String uuid){
        super(n,d,la,lo,v,id, uuid);
        type=t;
    }

    public OutdoorAttraction(String n, String d, double la, double lo, boolean v, int id,  String uuid) {
        super(n, d, la, lo, v, id, uuid);
    }

    public void setType(String type) {
        this.type = type;
    }
}
