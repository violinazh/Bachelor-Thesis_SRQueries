package com.violina;

import java.util.List;

public class Route {

    private List<R_POI> pois;
    private double length;

    public Route(List<R_POI> pois, double length) {
        this.pois = pois;
        this.length = length;
    }

    public List<R_POI> getPois() {
        return pois;
    }

    public double getLength() {
        return length;
    }

    public void addRpoi(R_POI poi) {
        pois.add(poi);
    }

    public void setLength(double length) {
        this.length += length;
    }

    public void removeRpoi(int index) {
        pois.remove(index);
    }

    @Override
    public String toString() {
        String string = "POIs: ";
        for (R_POI poi : pois) {
            string += poi + ", ";
        }
        string += "Length: " + length;

        return string;
    }
}
