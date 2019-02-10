package com.violina;

import java.util.List;
import java.util.PriorityQueue;

public class Vertex {

    final private int vid;
    //private final List<POI> pois;

    private PriorityQueue<POI> pois;


    public Vertex(int vid, PriorityQueue<POI> pois) {
        this.vid = vid;
        this.pois = pois;
    }

    public int getVID() {
        return vid;
    }

    public PriorityQueue<POI> getPOIs() {
        return pois;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        return vid == other.vid;
    }

    @Override
    public String toString() {
        return "" + vid + ", " + pois;
    }

}
