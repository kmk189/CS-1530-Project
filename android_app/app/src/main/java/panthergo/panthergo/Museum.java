package panthergo.panthergo;

public class Museum extends Location {
    public String hoursOperation;
    public double price;

    public Museum(){
        super();
        hoursOperation="";
        price=-1;//default is - bc 0 is free and valid price
    }

    public Museum(String n, String d,double la, double lo, boolean v, String h, double p){
        super(n,d,la,lo,v);
        hoursOperation=h;
        price=p;
    }

    public void setHoursOperation(String hoursOperation) {
        this.hoursOperation = hoursOperation;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

