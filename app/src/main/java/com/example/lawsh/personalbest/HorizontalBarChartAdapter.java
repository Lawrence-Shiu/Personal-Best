package com.example.lawsh.personalbest;

import android.graphics.Color;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;

public class HorizontalBarChartAdapter implements IChart {

    private HorizontalBarChart chart;

    HorizontalBarChartAdapter(HorizontalBarChart chart) {
        this.chart = chart;
    }

    @Override
    public BarData getData() {
        return this.chart.getData();
    }

    @Override
    public void setData(BarData data) {
        this.chart.setData(data);
    }

    @Override
    public void notifyDataSetChanged() {
        this.chart.notifyDataSetChanged();
    }

    @Override
    public void draw(int current_goal, int maxStepValue) {
        LimitLine goalLine = new LimitLine(current_goal, "Current Goal");
        goalLine.setLineColor(Color.BLACK);
        goalLine.setLineWidth(4);

        chart.getAxisLeft().addLimitLine(goalLine);

        chart.setFitBars(true);
        chart.invalidate();
    }
}
