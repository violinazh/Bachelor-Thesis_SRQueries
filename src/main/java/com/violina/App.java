package com.violina;

//import org.neo4j.driver.v1.*;
//import org.neo4j.driver.v1.Transaction;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;


import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

import static org.neo4j.driver.v1.Values.parameters;

/**
 *
 *
 */
public class App
{

    // Connects to the database
    private Connection connect() {

        // SQLite connection string
        String url = "jdbc:sqlite:/home/vzh/IdeaProjects/Test_GH/src/main/resources/table";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Inserts a row to the database
    public void insertRow(int vid, String r, String cs, String ab, String mt, String ph, String pb, String gs) {
        String sql = "INSERT INTO NearestNeighbor(VID,restaurant, coffee_shop, " +
                "atm_bank, movie_theater, pharmacy, pub_bar,gas_station) " +
                "VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vid);
            pstmt.setString(2, r);
            pstmt.setString(3, cs);
            pstmt.setString(4, ab);
            pstmt.setString(5, mt);
            pstmt.setString(6, ph);
            pstmt.setString(7, pb);
            pstmt.setString(8, gs);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Selects an entry in the table
    public String select () {
        String s = "";
        String sql = "SELECT restaurant FROM NearestNeighbor WHERE VID = 2";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) { // parse the results from every row
                s = rs.getString("restaurant");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return s;
    }

    // -> Selects an entry in the table by vid and category type
    public String selectEntry (String column, int vid) {
        String s = "";
        String format = String.format("SELECT %s FROM NearestNeighbor", column);
        String sql = format + " WHERE VID = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1, vid);
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) { // parse the results from every row
                s = rs.getString("coffee_shop");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return s;
    }

    // Inserts the whole table into the db (Format for entry: pid:vid:dist)
    public void insertTable (NearestNeighbor [][] table) {
        for (int i = 0; i < table.length; i++) { // column - for every vid
            String r = table[i][0].getPID() + ":" + table[i][0].getVID() + ":" + table[i][0].getDist();
            String cs = table[i][1].getPID() + ":" + table[i][1].getVID() + ":" + table[i][1].getDist();
            String ab = table[i][2].getPID() + ":" + table[i][2].getVID() + ":" + table[i][2].getDist();
            String mt = table[i][3].getPID() + ":" + table[i][3].getVID() + ":" + table[i][3].getDist();
            String ph = table[i][4].getPID() + ":" + table[i][4].getVID() + ":" + table[i][4].getDist();
            String pb = table[i][5].getPID() + ":" + table[i][5].getVID() + ":" + table[i][5].getDist();
            String gs = table[i][6].getPID() + ":" + table[i][6].getVID() + ":" + table[i][6].getDist();

            insertRow(i, r, cs, ab, mt, ph, pb, gs);
        }
        System.out.println("Inserted the table to the database.");
    }

    private static void writeToFile(String data) {
        File file = new File("src/main/resources/table.txt");
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

    // Parses an entry from the database
    public void parse(String s) {
        String[] values = s.split(":");
        int vid = Integer.parseInt(values[0]);
        int pid = Integer.parseInt(values[1]);
        double dist = Double.parseDouble(values[2]);
        System.out.println(vid + ", " + pid + ", "+ dist);
    }

    // -> Parses the vid
    public int parseVID(String s) {
        String[] values = s.split(":");
        return Integer.parseInt(values[0]);
    }

    // -> Parses the pid
    public int parsePID(String s) {
        String[] values = s.split(":");
        return Integer.parseInt(values[1]);
    }

    // -> Parses the distance
    public double parseDist(String s) {
        String[] values = s.split(":");
        return Double.parseDouble(values[2]);
    }

    // Maybe we need to copy the contents of Importer
    static List<Vertex> nodes = new ArrayList<Vertex>();
    static List<Edge> edges = new ArrayList<Edge>();

    public static void main( String[] args ) throws Exception {


        // Starting the importer
        System.out.println("--- Importing the graph from Neo4j");
        Importer importer = new Importer();
        importer.startDB();
        importer.importGraph();
        importer.shutDown();

        // Testing the vertices
        /*System.out.println("Vertices:");
        for (Vertex vertex : importer.nodes) {
            System.out.println(vertex);
        }

        // Testing the edges
        System.out.println("\nEdges:");
        for (Edge edge : importer.edges) {
            System.out.println(edge);
        }*/

        // Building the graph
        Graph graph = new Graph(importer.nodes, importer.edges);

        // Testing the graph
        //System.out.println("\nGraph:");
        //System.out.println(graph);

        // Building dijkstra
        System.out.println("\n--- Building Dijkstra");
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

        // Executing dijkstra for every vertex to fill the table
        System.out.println("\n--- Executing Dijkstra for every vertex");
        for (Vertex node: graph.getVertexes()) { // 428769
            System.out.println("\nNode: " + node);
            dijkstra.execute(node);
        }
        System.out.println("\nDijkstra execution done.");

        // Testing the table; for every element in the table we put it in a database
        System.out.println("\n--- Building the string");
        String t = "";
        for (int i = 0; i < dijkstra.getTable().length; i++) {
            System.out.println(i);
            for (int j = 0; j < dijkstra.getTable()[i].length; j++) {
                t += dijkstra.getTable()[i][j] + " ";
            }
            t += "\n";
        }
        //System.out.println(t);
        System.out.println("\n--- Writing the table to a file");
        writeToFile(t);

        App app = new App();
        System.out.println("\n--- Importing the table to the database");
        app.insertTable(dijkstra.getTable());


        //String s = app.selectEntry("coffee_shop",1);
        //System.out.println(app.parseDist(s));

    }


}