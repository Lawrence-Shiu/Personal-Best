package com.example.lawsh.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    //private final int DAYS = 7;
    //private final String ACTIVE_KEY[] = {"ACTIVE_STEPS", "ACTIVE_STEPS_1", "ACTIVE_STEPS_2",
    //    "ACTIVE_STEPS_3", "ACTIVE_STEPS_4", "ACTIVE_STEPS_5", "ACTIVE_STEPS_6"};
    //private final String PASSIVE_KEY[] = {"PASSIVE_STEPS", "PASSIVE_STEPS_1", "PASSIVE_STEPS_2",
    //        "PASSIVE_STEPS_3", "PASSIVE_STEPS_4", "PASSIVE_STEPS_5", "PASSIVE_STEPS_6"};
    //SharedPreferences pref;

    private int[] active_steps; //SU M TU W TH F SA
    private int[] passive_steps; //SU M TU W TH F SA
    private int current_goal;
    private String dayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //Not sure how to OCP this
        HorizontalBarChart progress = (HorizontalBarChart) findViewById(R.id.progress_graph);

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                active_steps = new int[]{0,0,0,0,0,0,0};
                passive_steps = new int[]{0,0,0,0,0,0,0};
                current_goal = 5000;
                dayOfWeek = "Sunday";
            } else {
                active_steps = extras.getIntArray("ACTIVE_STEPS");
                passive_steps = extras.getIntArray("PASSIVE_STEPS");
                current_goal = extras.getInt("CURRENT_GOAL");
                dayOfWeek = extras.getString("DAY_OF_WEEK");
            }
        }
        Log.d("GraphActivity", dayOfWeek);

        //make sure array actually starts on correct day
        int numToRotate = determineNumToRotate(dayOfWeek);
        int[] rotatedActiveSteps = rotateArrayLeft(active_steps, numToRotate);
        int[] rotatedPassiveSteps = rotateArrayLeft(passive_steps, numToRotate);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        IChart chart = new HorizontalBarChartAdapter(progress);
        makeToolbar(mToolbar);
        makeGraph(chart, rotatedActiveSteps, rotatedPassiveSteps);
    }

    private int determineNumToRotate(String dayOfWeek) {
        switch(dayOfWeek) {
            case "Sunday": Log.d("Graph", "Sunday"); return 0;
            case "Monday": return 1;
            case "Tuesday": return 2;
            case "Wednesday": return 3;
            case "Thursday": return 4;
            case "Friday": return 5;
            case "Saturday": return 6;
        }
        Log.d("Graph", "Switch failed");
        return 0;
    }

    //Array rotation utility
    private int[] rotateArrayLeft(int[] arr, int numTimes) {
        int[] copy = arr.clone();
        for(int i = 0; i < numTimes; i++) {
            for(int j = 1; j < arr.length; j++) {
                copy[j - 1] = arr[j];
            }
            arr[arr.length - 1] = arr[0];
        }
        return copy;
    }

    public void makeToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Progress Report");
    }

    public void makeGraph(IChart chart, int[] activeSteps, int[] passiveSteps) {
        Graph graph = new GraphBuilder(chart, activeSteps, passiveSteps, current_goal).build();
        graph.createGraph();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId() ) {
            case android.R.id.home:
                //Intent intent = new Intent(GraphActivity.this, MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}