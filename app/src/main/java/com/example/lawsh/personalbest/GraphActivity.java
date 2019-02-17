package com.example.lawsh.personalbest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;

import com.github.mikephil.charting.charts.HorizontalBarChart;
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

    private int[] active_steps; //SU M TU W TH F SA
    private int[] passive_steps; //SU M TU W TH F SA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        active_steps = new int[DAYS];
        passive_steps = new int[DAYS];

        for(int i = 0; i < DAYS; i++) {
            pref = getSharedPreferences(ACTIVE_KEY[i], MODE_PRIVATE);
            active_steps[i] = pref.getInt(ACTIVE_KEY[i], 0);
            pref = getSharedPreferences(PASSIVE_KEY[i], MODE_PRIVATE);
            passive_steps[i] = pref.getInt(PASSIVE_KEY[i], 0);
        }

        HorizontalBarChart progress = (HorizontalBarChart) findViewById(R.id.progress_graph);

        drawChart(progress);
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
            dataset.setStackLabels(new String[]{"Passive, Active"});

            ArrayList<IBarDataSet> datasets = new ArrayList<>();
            datasets.add(dataset);

            BarData data = new BarData(datasets);
            data.setValueFormatter(new StackedValueFormatter(false, "", 1));
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);
        }

        chart.setFitBars(true);
        chart.invalidate();
    }

    private int[] getColors() {
        int[] colors = new int[2];
        colors[0] = Color.GREEN; //passive
        colors[1] = Color.RED; //passive

        return colors;
    }
}