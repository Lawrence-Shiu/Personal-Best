package com.example.lawsh.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
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

    private final int DAYS = 7;
    private final String ACTIVE_KEY[] = {"ACTIVE_STEPS", "ACTIVE_STEPS_1", "ACTIVE_STEPS_2",
        "ACTIVE_STEPS_3", "ACTIVE_STEPS_4", "ACTIVE_STEPS_5", "ACTIVE_STEPS_6"};
    private final String PASSIVE_KEY[] = {"PASSIVE_STEPS", "PASSIVE_STEPS_1", "PASSIVE_STEPS_2",
            "PASSIVE_STEPS_3", "PASSIVE_STEPS_4", "PASSIVE_STEPS_5", "PASSIVE_STEPS_6"};
    SharedPreferences pref;

    public int[] active_steps; //SU M TU W TH F SA
    public int[] passive_steps; //SU M TU W TH F SA

    public int current_goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                active_steps = new int[]{0,0,0,0,0,0,0};
                passive_steps = new int[]{0,0,0,0,0,0,0};
                current_goal = 5000;
            } else {
                active_steps = extras.getIntArray("ACTIVE_STEPS");
                passive_steps = extras.getIntArray("PASSIVE_STEPS");
                current_goal = extras.getInt("CURRENT_GOAL");
            }
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Progress Report");


        HorizontalBarChart progress = (HorizontalBarChart) findViewById(R.id.progress_graph);

        drawChart(progress);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId() ) {
            case android.R.id.home:
                Intent intent = new Intent(GraphActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void drawChart(HorizontalBarChart chart) {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        for(int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, new float[]{active_steps[i], passive_steps[i]}));
        }

        BarDataSet dataset;

        if(chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            dataset = (BarDataSet) chart.getData().getDataSetByIndex(0);
            dataset.setValues(entries);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            dataset = new BarDataSet(entries, "");
            dataset.setDrawIcons(false);
            dataset.setColors(getColors());
            dataset.setStackLabels(new String[]{"Passive", "Active"});

            ArrayList<IBarDataSet> datasets = new ArrayList<>();
            datasets.add(dataset);

            BarData data = new BarData(datasets);
            data.setValueFormatter(new StackedValueFormatter(false, "", 1));
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);
        }

        LimitLine goalLine = new LimitLine(current_goal, "Current Goal");
        goalLine.setLineColor(Color.BLACK);
        goalLine.setLineWidth(4);

        chart.getAxisLeft().addLimitLine(goalLine);

        chart.setFitBars(true);
        chart.invalidate();
    }

    private int[] getColors() {
        int[] colors = new int[2];
        colors[0] = Color.GREEN; //passive
        colors[1] = Color.RED; //passive

        return colors;
    }

    public void setSteps(int[] active, int[] passive) {
        active_steps = active;
        passive_steps = passive;
    }

    private void endActivity() {
        finish();
    }
}