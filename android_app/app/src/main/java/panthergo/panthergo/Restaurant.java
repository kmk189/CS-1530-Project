package panthergo.panthergo;

public class Restaurant extends Location {
    public String hoursOperation;
    public String menu; //url

    public Restaurant(){
        super();
        hoursOperation="";
        menu="";
    }
    public Restaurant(String n, String d,double la, double lo, boolean v, String h, String m,
                      int id, String uuid){
        super(n,d,la,lo,v,id, uuid);
        hoursOperation=h;
        menu=m;
    }

    public Restaurant(String n, String d, double la, double lo, boolean v, int id, String uuid) {
        super(n, d, la, lo, v, id, uuid);
    }

    public void setHoursOperation(String hoursOperation) {
        this.hoursOperation = hoursOperation;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
