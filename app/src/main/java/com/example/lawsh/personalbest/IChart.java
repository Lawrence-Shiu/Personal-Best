package com.example.lawsh.personalbest;

import com.github.mikephil.charting.data.BarData;

//Currently only supports bar charts - need a separate case for line charts
public interface IChart {
    BarData getData();
    void setData(BarData data);
    void notifyDataSetChanged();
    void draw(int current_goal, int maxStepValue);
}
