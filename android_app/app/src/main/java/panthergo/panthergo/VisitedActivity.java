package panthergo.panthergo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;



public class VisitedActivity extends AppCompatActivity {

    public ArrayList<String> visitedList = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);
        visitedList.add("a");
        visitedList.add("b");
        visitedList.add("c");
        visitedList.add("d");
        visitedList.add("e");
        visitedList.add("f");


    }
    public void launchMapActivity(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        //launch the map activity
        startActivity(intent);
    }
}
