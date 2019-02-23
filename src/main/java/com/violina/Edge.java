package com.violina;

public class Edge {

    private final Vertex source;
    private final Vertex destination;
    private final double distance;

    public Edge(Vertex source, Vertex destination, double distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }

    public Vertex getDestination() {
        return destination;
    }

    public Vertex getSource() {
        return source;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return source + " -> " + destination + " : " + distance;
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return (o instanceof Edge) && (toString().equals(o.toString()));
    }

}
