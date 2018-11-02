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
                      int id){
        super(n,d,la,lo,v,id);
        hoursOperation=h;
        menu=m;
    }

    public Restaurant(String n, String d, double la, double lo, boolean v, int id) {
        super(n, d, la, lo, v, id);
    }

    public void setHoursOperation(String hoursOperation) {
        this.hoursOperation = hoursOperation;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
