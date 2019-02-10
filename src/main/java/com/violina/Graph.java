package com.violina;

import java.util.List;

public class Graph {

    private final List<Vertex> vertexes;
    private final List<Edge> edges;

    public Graph(List<Vertex> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        String string = "Vertices:\n";
        for (Vertex vertex : vertexes) {
            string += vertex + "\n";
        }
        string += "\nEdges:\n";
        for (Edge edge : edges) {
            string += edge + "\n";
        }
        return string;
    }

}
