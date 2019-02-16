package com.violina;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Importer {

    private static final File databaseDirectory = new File( "src/main/resources/berlin.db" );

    // tag::vars[]
    private GraphDatabaseService graphDb;
    private Label label = Label.label("CROSSROAD");
    // end::vars[]

    List<Vertex> nodes = new ArrayList<Vertex>();
    List<Edge> edges = new ArrayList<Edge>();

    int initialCapacity = 15;
    Comparator<POI> comparator = new Comparator<POI>() {

        @Override
        public int compare(POI e1, POI e2) {
            return Double.compare(e1.getDist(), e2.getDist());
        }

    };

    void startDB() {
        // tag::startDb[]
        System.out.println("Starting database ...\n");
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(databaseDirectory);
        registerShutdownHook(graphDb);
        // end::startDb[]
    }

    void importGraph() throws IOException {

        // tag::transaction[]
        try (Transaction tx = graphDb.beginTx()) {

            // Importing the edges
            for (int i = 0; i < 504229; i++) { // 504229
                addEdge(i);
            }

            //addEdge(355995);
            //addEdge(355996);
            //addEdge(355997);

            /*for (int i = 0; i < 100; i++) {
                test(i);
            }*/

            // tag::transaction[]
            tx.success();
        }
        System.out.println("Graph imported.");
        // end::transaction[]
    }

    // Alternative variant for adding edges through the nodes
    private void test(int id) {
        Node node = graphDb.findNode(label, "id", id);
        Vertex s = addVertex(node);

        // get the relationships from type HAS_POI
        Iterable<Relationship> rel = node.getRelationships(RelationshipType.withName("ROAD"));
        Iterator<Relationship> iter = rel.iterator();
        ArrayList<Relationship> relationships = new ArrayList<>();
        while (iter.hasNext()) {
            relationships.add(iter.next());
        }
        // iterate the relationships
        for (Relationship relationship : relationships) {
            Node end = relationship.getEndNode();
            Vertex e = addVertex(end);
            double distance = (Double) relationship.getProperty("distance");

            // adding a double edge
            Edge to = new Edge(s, e, distance);
            if (!edges.contains(to)){ // check if the vertex is already in the list of vertices to avoid duplicates
                edges.add(to);
            }

            Edge from = new Edge(e, s, distance);
            if (!edges.contains(from)){ // check if the vertex is already in the list of vertices to avoid duplicates
                edges.add(from);
            }
        }
    }

    private void addEdge(int id) {
        Relationship rel = graphDb.getRelationshipById(id);
        Node start = rel.getStartNode();
        Node end = rel.getEndNode();
        double distance = (Double) rel.getProperty("distance");

        //System.out.println(start.getProperty("id") + " " + end.getProperty("id") + ", dist: " + distance);

        // Importing the vertices
        Vertex s = addVertex(start);
        Vertex e = addVertex(end);

        // adding a double edge
        Edge to = new Edge(s, e, distance);
        edges.add(to);
        Edge from = new Edge(e, s, distance);
        edges.add(from);

    }

    private Vertex addVertex(Node node) {
        int vid = (int)((long) node.getProperty("id"));
        PriorityQueue<POI> pois = new PriorityQueue<POI>(initialCapacity, comparator);

        // get the relationships from type HAS_POI
        Iterable<Relationship> rel = node.getRelationships(RelationshipType.withName("HAS_POI"));
        Iterator<Relationship> iter = rel.iterator();
        ArrayList<Relationship> relationships = new ArrayList<>();
        while (iter.hasNext()) {
            relationships.add(iter.next());
        }
        // iterate the relationships
        for (Relationship relationship : relationships) {
            Node poi = relationship.getEndNode();
            int pid = (int)((long) poi.getProperty("id"));
            double distance = (Double) poi.getProperty("dist");

            //System.out.println("This crossroad is connected to crossroad with id " + pid);
            String[] types = (String[]) poi.getProperty("type");
            for (String type : types) {
                //System.out.println(type + " " + distance);
                pois.add(new POI(pid, type, distance));
            }

        }

        Vertex v = new Vertex(vid, pois);
        if (!nodes.contains(v)){ // check if the vertex is already in the list of vertices to avoid duplicates
            nodes.add(v);
        }

        return v;
    }

    void shutDown ()
    {
        System.out.println("\nShutting down database ...");
        // tag::shutdownServer[]
        graphDb.shutdown();
        // end::shutdownServer[]
    }

    // tag::shutdownHook[]
    private static void registerShutdownHook ( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
    // end::shutdownHook[]

}
