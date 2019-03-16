package com.example.lawsh.personalbest;

public class GraphBuilder {

    IChart chart;
    int[] active_steps;
    int[] passive_steps;
    int current_goal;

    public GraphBuilder(IChart chart, int[] active_steps, int[] passive_steps, int current_goal) {
        this.chart = chart;
        this.active_steps = active_steps;
        this.passive_steps = passive_steps;
        this.current_goal = current_goal;
    }

    public Graph build() {
        return new Graph(chart, active_steps, passive_steps, current_goal);
    }

    private GraphBuilder setActiveSteps(int[] active_steps) {
        this.active_steps = active_steps;
        return this;
    }

    private GraphBuilder setPassiveSteps(int[] passive_steps) {
        this.passive_steps = passive_steps;
        return this;
    }

    private GraphBuilder setCurrentGoal(int current_goal) {
        this.current_goal = current_goal;
        return this;
    }

    private GraphBuilder setChart(IChart chart) {
        this.chart = chart;
        return this;
    }
}
