package group14.mc.asu.edu.brainnetteam14;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button setup;
    EditText Patient_Name,Patient_Age,Patinet_ID;
    String Gender = "Male";
    boolean Plot;
    GraphPlotter graphplotter;
    GraphView graph;
    ArrayList<Double> datapoints;
    private final Handler mHandler = new Handler();
    DatabaseHelper helper;
    String Tablename = "";
    final String TAG = "MainActivity";
    DataPoint[] xpoints,ypoints,zpoints;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);

    }


    public void setUpView(){

    }
}
