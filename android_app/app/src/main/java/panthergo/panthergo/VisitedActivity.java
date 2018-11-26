package panthergo.panthergo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import 	android.text.method.ScrollingMovementMethod;
import android.widget.Toast;

import java.util.ArrayList;



public class VisitedActivity extends AppCompatActivity {
    TextView tv;
    LinearLayout LinearLayoutView;
    public ArrayList<String> visitedList;
    public ArrayList<ClickableSpan> spanArrayList;
    public ArrayList<Integer> start;
    public ArrayList<Integer> end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);
        visitedList = new ArrayList<String>();
        spanArrayList = new ArrayList<ClickableSpan>();
        start = new ArrayList<Integer>();
        end = new ArrayList<Integer>();


        tv = (TextView) findViewById(R.id.textView);

        loadText();
    }

    private void loadText() {
        String s = "";
        int lengthStart = 0;
        int lengthEnd = 0;
        for (int x = 0; x < 10; x++) {
            lengthStart = s.length();
            s += "Line: " + String.valueOf(x) + "\n\n";
            lengthEnd = s.length();

            ClickableSpan temp=null;
            if(x==5){
            temp = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                        launchMainActivity();

                    //create method to load page based on ID number? to call here?
                }
            };}else{
                temp = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        launchMapActivity();

                        //create method to load page based on ID number? to call here?
                    }
                };
            }
            spanArrayList.add(temp);
            start.add(lengthStart);
            end.add(lengthEnd);

            //ss.setSpan(temp, lengthStart, lengthEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        SpannableString ss = new SpannableString(s);
        for (int x = 0; x < spanArrayList.size(); x++) {
            ss.setSpan(spanArrayList.get(x), start.get(x), end.get(x), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setText(s);
    }


    public void launchMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        //launch the map activity
        startActivity(intent);
    }
    public void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        //launch the map activity
        startActivity(intent);
    }

}