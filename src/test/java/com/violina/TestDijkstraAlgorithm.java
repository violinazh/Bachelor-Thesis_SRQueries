package com.violina;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestDijkstraAlgorithm {

    private List<Vertex> nodes;
    private List<Edge> edges;

    @Test
    public void testExecute() {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();

        int initialCapacity = 15;
        Comparator<POI> comparator = new Comparator<POI>() {

            @Override
            public int compare(POI e1, POI e2) {
                return Double.compare(e1.getDist(), e2.getDist());
            }

        };

        // Testing the vertices and poi
        int vid1 = 0;
        PriorityQueue<POI> pois1 = new PriorityQueue<POI>(initialCapacity, comparator);
        pois1.add(new POI(0, "restaurant", 0.24));
        pois1.add(new POI(0, "coffee_shop", 0.24));
        pois1.add(new POI(1, "pub", 0.17));
        /*for (POI poi : pois1) {
            System.out.println(poi);
        }*/
        Vertex location1 = new Vertex(vid1, pois1);
        nodes.add(location1);

        int vid2 = 1;
        PriorityQueue<POI> pois2 = new PriorityQueue<POI>(initialCapacity, comparator);
        Vertex location2 = new Vertex(vid2, pois2);
        nodes.add(location2);

        int vid3 = 2;
        PriorityQueue<POI> pois3 = new PriorityQueue<POI>(initialCapacity, comparator);
        pois3.add(new POI(2,"restaurant", 0.56));
        Vertex location3 = new Vertex(vid3, pois3);
        nodes.add(location3);

        int vid4 = 3;
        PriorityQueue<POI> pois4 = new PriorityQueue<POI>(initialCapacity, comparator);
        pois4.add(new POI(3, "pub", 0.78));
        pois4.add(new POI(4, "pharmacy", 0.35));
        Vertex location4 = new Vertex(vid4, pois4);
        nodes.add(location4);

        int vid5 = 4;
        PriorityQueue<POI> pois5 = new PriorityQueue<POI>(initialCapacity, comparator);
        pois5.add(new POI(5, "coffee_shop", 0.16));
        Vertex location5 = new Vertex(vid5, pois5);
        nodes.add(location5);

        /*System.out.println("Vertices:");
        for (Vertex vertex : nodes) {
            System.out.println(vertex);
        }*/

        // Testing the edges, !!! should add both directions
        Edge lane1 = new Edge(nodes.get(0), nodes.get(1), 8.35);
        edges.add(lane1);
        //Edge lane11 = new Edge(nodes.get(1), nodes.get(0), 8.35);
        //edges.add(lane11);
        Edge lane2 = new Edge(nodes.get(0), nodes.get(4), 5.5);
        edges.add(lane2);
        //Edge lane22 = new Edge(nodes.get(4), nodes.get(0), 5.5);
        //edges.add(lane22);
        Edge lane3 = new Edge(nodes.get(1), nodes.get(3), 6.78);
        edges.add(lane3);
        //Edge lane33 = new Edge(nodes.get(3), nodes.get(1), 6.78);
        //edges.add(lane33);
        Edge lane4 = new Edge(nodes.get(4), nodes.get(3), 4.3);
        edges.add(lane4);
        //Edge lane44 = new Edge(nodes.get(3), nodes.get(4), 4.3);
        //edges.add(lane44);

        /*System.out.println("\nEdges:");
        for (Edge edge : edges) {
            System.out.println(edge);
        }*/

        // Testing the graph
        System.out.println("\nGraph:");
        Graph graph = new Graph(nodes, edges);
        System.out.println(graph);

        // Testing dijkstra; path from 0 to 3
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

        // Executing dijkstra for every vertex to fill the table
        for (int i = 0; i < 5; i++) {
            System.out.println("Executing Dijkstra for node: " + i);
            dijkstra.execute(nodes.get(i));
        }

        // Testing the table
        System.out.println("\n");
        NearestNeighbor [][] table = dijkstra.getTable();
        String t = "";
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                t += table[i][j] + " ";
            }
            t+= "\n";
        }
        System.out.println(t);
        writeToFile(t);

        // This part is not needed for table creation
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(3));

        assertNotNull(path);
        assertTrue(path.size() > 0);

        System.out.println("\nPath:");
        for (Vertex vertex : path) {
            System.out.println(vertex);
        }

        /*Graph<Vertex, Edge> g = new SimpleWeightedGraph<Vertex, Edge>(Edge.class);
        for (Vertex vertex : nodes) {
            g.addVertex(vertex);
        }
        for (Edge edge : edges) {
            g.addEdge(edge.getSource(), edge.getDestination(), edge);
            g.setEdgeWeight(edge.getSource(), edge.getDestination(), edge.getDistance());
        }
        DijkstraShortestPath<Vertex, Edge> dijkstra =
                new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<Vertex, Edge> iPaths = dijkstra.getPaths(nodes.get(3));
        System.out.println(iPaths.getPath(nodes.get(0)) + "\n");*/


    }

    private static void writeToFile(String data) {
        File file = new File("src/main/resources/table_test.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Wrote the table to a file.");
    }

}
