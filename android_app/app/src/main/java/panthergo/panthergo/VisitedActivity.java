package panthergo.panthergo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;
import 	android.text.method.ScrollingMovementMethod;

import java.util.ArrayList;



public class VisitedActivity extends AppCompatActivity  {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);
        tv = (TextView) findViewById(R.id.textView);
        loadText();
    }

    /* Close this activity, thereby bringing the map activity back */
    public void launchMapActivity(View view) {
        this.finish();
    }

    private void loadText(){
        String s= "";

        for (Location loc: MapActivity.visitedLocations) {
            s+="Line: "+ loc.name +"\n";
        }
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setText(s);
    }
}
