package rail.mini_project.projectrail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class Schedule extends AppCompatActivity {
    String s_near, d_near,s_name,d_name;

    List<String> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);

        LatLng source = (LatLng) getIntent().getExtras().get("Source");
        LatLng destination = (LatLng) getIntent().getExtras().get("Destination");
        Log.e("Database", "Calling");


        Database db = new Database(this);
        db.insert();

        s_near = db.distance(source);
        d_near = db.distance(destination);
        s_name = db.find(s_near);
        d_name = db.find(d_near);
        try {
            data = new Scheduledata(s_near, d_near)
                    .execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            e.printStackTrace();
        }


        if(data.get(0).equals("Unsuccessful")){

                Toast.makeText(getApplicationContext(),
                        "Connection Error, Please try again", Toast.LENGTH_SHORT).show();
                Intent intentMain = new Intent(Schedule.this,MainActivity.class);

                Schedule.this.startActivity(intentMain);
        }

        if (data.get(0).equals("Not")){

            ViewGroup t = (ViewGroup) findViewById(R.id.list);
            final TextView n = new TextView(this);
            final TextView listvalue = new TextView(this);

            n.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            n.setPadding(0, 7, 0, 3);
            n.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            n.setText("Source Station " + s_name + " To Destination Station " + d_name);
            t.addView(n);

            listvalue.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            listvalue.setPadding(0, 7, 0, 3);
            listvalue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            listvalue.setText("*No Direct Train/s Found");
            t.addView(listvalue);
            return;

        }


        final ViewGroup t = (ViewGroup) findViewById(R.id.list);

        final TextView n = new TextView(this);

        n.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        n.setPadding(0, 7, 0, 3);
        n.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        n.setText("Source Station " + s_name + " To Destination Station " + d_name);
        t.addView(n);


        for (String d: data) {

            final TextView listvalue = new TextView(this);

            listvalue.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            listvalue.setPadding(0, 7, 0, 3);

            listvalue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            listvalue.setText(d);
            t.addView(listvalue);
        }

        final TextView white = new TextView(this);

        white.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        white.setBackgroundColor(getResources().getColor(
             android.R.color.transparent));
        t.addView(white);

    }
}