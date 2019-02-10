package com.violina;

public class NearestNeighbor {

    private int pid;
    private int vid; // the vid to which the POI is mapped
    private double dist;

    public NearestNeighbor(int pid, int vid, double dist) {
        this.pid = pid;
        this.vid = vid;
        this.dist = dist;
    }

    public int getPID() {
        return pid;
    }

    public void setPID(int pid) {
        this.pid = pid;
    }

    public int getVID() {
        return vid;
    }

    public void setVID(int vid) {
        this.vid = vid;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    @Override
    public String toString() {
        return "[" + pid + ", " + vid + ", " + dist + "]";
    }
}
