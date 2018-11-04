package panthergo.panthergo;

public class Museum extends Location {
    public String hoursOperation;
    public String price;

    public Museum(){
        super();
        hoursOperation="";
        price="";
    }

    public Museum(String n, String d,double la, double lo, boolean v, String h, String p, int id){
        super(n,d,la,lo,v,id);
        hoursOperation=h;
        price=p;
    }

    public Museum(String n, String d, double la, double lo, boolean v, int id) {
        super(n, d, la, lo, v, id);
    }

    public void setHoursOperation(String hoursOperation) {
        this.hoursOperation = hoursOperation;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

