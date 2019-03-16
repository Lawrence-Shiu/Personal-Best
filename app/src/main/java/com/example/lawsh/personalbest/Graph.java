package com.example.lawsh.personalbest;

import android.graphics.Color;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class Graph {
    private IChart chart;
    private int[] active_steps;
    private int[] passive_steps;
    private int current_goal;
    private int maxStepValue;

    protected Graph(IChart chart, int[] active_steps, int[] passive_steps, int current_goal) {
        this.chart = chart;
        this.active_steps = active_steps;
        this.passive_steps = passive_steps;
        this.current_goal = current_goal;
        this.maxStepValue = 0;
    }

    public void createGraph() {
        setChartData();
        chart.draw(current_goal, maxStepValue);
    }

    private int[] getColors(int activeColor, int passiveColor) {
        int[] colors = new int[2];
        colors[0] = activeColor; //active
        colors[1] = passiveColor; //passive

        return colors;
    }

    private void setChartData() {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        for(int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, new float[]{active_steps[i], passive_steps[i] - active_steps[i]}));

            //Get the maximum point reading, for setting X axis limits
            if(passive_steps[i] > maxStepValue) {
                maxStepValue = passive_steps[i];
            }
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
            dataset.setColors(getColors(Color.RED, Color.GREEN));
            dataset.setStackLabels(new String[]{"Active", "Passive"});

            ArrayList<IBarDataSet> datasets = new ArrayList<>();
            datasets.add(dataset);

            BarData data = new BarData(datasets);
            data.setValueFormatter(new StackedValueFormatter(false, "", 1));
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);
        }
    }

    public IChart getChart() {
        return chart;
    }
}
