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
    public ArrayList<String> visitedList = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);

        tv = (TextView) findViewById(R.id.textView);

        loadText();


    }
    public void launchMapActivity(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        //launch the map activity
        startActivity(intent);
    }
    private void loadText(){
        String s= "";

        for(int x=0; x<100; x++){
            s+="Line: "+ String.valueOf(x) +"\n";
        }
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setText(s);
    }
}
