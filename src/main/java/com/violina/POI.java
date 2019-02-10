package com.violina;

public class POI {

    final private int pid;
    final private String type;
    final private double dist;

    public POI(int pid, String type, double dist) {
        this.pid = pid;
        this.type = type;
        this.dist = dist;
    }

    public int getPID() {
        return pid;
    }

    public String getType() {
        return type;
    }

    public double getDist() {
        return dist;
    }

    @Override
    public String toString() {
        return "(" + pid + ", " + type + ", " + dist + ")";
    }

}
