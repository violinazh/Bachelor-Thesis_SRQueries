package com.violina;

import java.util.*;

public class DijkstraAlgorithm {

    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    //private Set<Vertex> unSettledNodes;
    private int initialCapacity = 15;
    private Comparator<Vertex> comparator = new Comparator<Vertex>() {

        @Override
        public int compare(Vertex e1, Vertex e2) {
            return Double.compare(getShortestDistance(e1), getShortestDistance(e2));
        }

    };
    private PriorityQueue<Vertex> unSettledNodes = new PriorityQueue<Vertex>(initialCapacity, comparator);
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Double> distance;

    private NearestNeighbor [][] table = new NearestNeighbor[428769][7]; //row: vid, column: category type

    public DijkstraAlgorithm(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<Vertex>(graph.getVertexes());
        this.edges = new ArrayList<Edge>(graph.getEdges());

        // Initializing the table
        for (int row = 0; row < table.length; row ++)
            for (int col = 0; col < table[row].length; col++)
                table[row][col] = new NearestNeighbor(7777, 0, 0);
    }

    public NearestNeighbor [][] getTable() {
        return table;
    }

    public void execute(Vertex source) {
        int sid = source.getVID(); // added this for easy table entries
        System.out.println("Executing Dijsktra for vertex: " + sid);
        int counter = 0;

        settledNodes = new HashSet<Vertex>();
        //unSettledNodes = new HashSet<Vertex>();
        distance = new HashMap<Vertex, Double>();
        predecessors = new HashMap<Vertex, Vertex>();
        distance.put(source, 0.0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0 && counter < 7) {
            //Vertex node = getMinimum(unSettledNodes); // the node is eligible for type check; shortest path to this node has been found
            Vertex node = unSettledNodes.poll();
            //System.out.println("-Checking node for neighbors: " + node);
            // Creating the table
            for (POI poi : node.getPOIs()) {
                switch(poi.getType()) {
                    case "restaurant":
                        if (table[sid][0].getPID() == 7777) {
                            table[sid][0].setVID(node.getVID());
                            table[sid][0].setPID(poi.getPID());
                            table[sid][0].setDist(distance.get(node));
                            counter++;
                            System.out.println("Found a restaurant " + table[sid][0]);
                            System.out.println("Counter: " + counter);
                        }
                        break;
                    case "coffee_shop":
                        if (table[sid][1].getPID() == 7777) {
                            table[sid][1].setVID(node.getVID());
                            table[sid][1].setPID(poi.getPID());
                            table[sid][1].setDist(distance.get(node));
                            counter++;
                            System.out.println("Found a coffee shop " + table[sid][1]);
                            System.out.println("Counter: " + counter);
                        }
                        break;
                    case "atm_bank":
                        if (table[sid][2].getPID() == 7777) {
                            table[sid][2].setVID(node.getVID());
                            table[sid][2].setPID(poi.getPID());
                            table[sid][2].setDist(distance.get(node));
                            counter++;
                            System.out.println("Found an atm/bank " + table[sid][2]);
                            System.out.println("Counter: " + counter);
                        }
                        break;
                    case "movie_theater":
                        if (table[sid][3].getPID() == 7777) {
                            table[sid][3].setVID(node.getVID());
                            table[sid][3].setPID(poi.getPID());
                            table[sid][3].setDist(distance.get(node));
                            counter++;
                            System.out.println("Found a movie theater " + table[sid][3]);
                            System.out.println("Counter: " + counter);
                        }
                        break;
                    case "pharmacy":
                        if (table[sid][4].getPID() == 7777) {
                            table[sid][4].setVID(node.getVID());
                            table[sid][4].setPID(poi.getPID());
                            table[sid][4].setDist(distance.get(node));
                            counter++;
                            System.out.println("Found a pharmacy " + table[sid][4]);
                            System.out.println("Counter: " + counter);
                        }
                        break;
                    case "pub_bar":
                        if (table[sid][5].getPID() == 7777) {
                            table[sid][5].setVID(node.getVID());
                            table[sid][5].setPID(poi.getPID());
                            table[sid][5].setDist(distance.get(node));
                            counter++;
                            System.out.println("Found a pub/bar " + table[sid][5]);
                            System.out.println("Counter: " + counter);
                        }
                        break;
                    case "gas_station":
                        if (table[sid][6].getPID() == 7777) {
                            table[sid][6].setVID(node.getVID());
                            table[sid][6].setPID(poi.getPID());
                            table[sid][6].setDist(distance.get(node));
                            counter++;
                            System.out.println("Found a gas station " + table[sid][6]);
                            System.out.println("Counter: " + counter);
                        }
                        break;
                    default:
                        System.out.println("Found nothing.");
                        break;

                }
            }

            settledNodes.add(node);
            //unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Vertex node) {
        List<Vertex> adjacentNodes = getNeighbors(node);
        for (Vertex target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private double getDistance(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getDistance();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Vertex> getNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    private Vertex getMinimum(Set<Vertex> vertexes) {
        Vertex minimum = null;
        for (Vertex vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }

    private double getShortestDistance(Vertex destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<Vertex>();
        Vertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

}
