package com.violina;

public class R_POI {

    int pid;
    int vid;
    String type;

    public R_POI(int pid, int vid, String type) {
        this.pid = pid;
        this.vid = vid;
        this.type = type;
    }

    public int getPid() {
        return pid;
    }

    public int getVid() {
        return vid;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "(" + pid + ", " + vid + ", " + type + ")";
    }
}
