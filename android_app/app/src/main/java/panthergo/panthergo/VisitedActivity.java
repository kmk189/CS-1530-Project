package panthergo.panthergo;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class VisitedActivity extends AppCompatActivity  {
    TextView tv;
    TextView tvStat;
    Context con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);
        tv = (TextView) findViewById(R.id.textView);
        tvStat = (TextView) findViewById(R.id.tvStats);
        loadText();
    }

    /* Close this activity, thereby bringing the map activity back */
    public void launchMapActivity(ClickableSpan view) {
        this.finish();
    }

    private void loadText(){
        String s= "";
        String stat="";
        float percent = (MapActivity.visitedLocations.size() * 100.0f) / MapActivity.locations.size();

        stat= "Visited Locations: "+MapActivity.visitedLocations.size()+"\n"+percent+"% visited";
        if (MapActivity.visitedLocations.size()>0){

            for (Location loc: MapActivity.visitedLocations) {
                s+=loc.name +"\n";
            }
            SpannableString ss= new SpannableString(s);
            String[] locNames= s.split("\n");
            for (final String loc:locNames) {
                con= this;
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                            Log.e("TAG: THIS IS THE LOC : ",loc );
                            Location local = findLocationByName(loc);
                            Utility.displayLocationAlert(local, con);

                    }
                };
                ss.setSpan(clickableSpan, s.indexOf(loc), s.indexOf(loc) + loc.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(ss);
        }else{
            s="YOU HAVE NOT YET\nVISITED ANY LOCATIONS\n\nRETURN TO THE MAP \nTO CONTINUE";
            tv.setText(s);
        }
        tvStat.setText(stat);

    }
    private Location findLocationByName(String loc){
        Location locat=null;
        for (int i=0; i<MapActivity.visitedLocations.size(); i++){
            if (MapActivity.locations.get(i).name.equalsIgnoreCase(loc)){
                return MapActivity.locations.get(i);
            }
        }
        return locat;
    }


}
