package com.violina;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.EdgeIteratorState;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 *
 */
public class App
{
    // for connecting to the database
    private Connection connect() {

        // SQLite connection string
        String url = "jdbc:sqlite:/home/vzh/IdeaProjects/Test_GH/database";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // for inserting a row to the database
    public void insert(int pid, int vid) {
        String sql = "INSERT INTO berlin(PID,VID) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pid);
            pstmt.setInt(2, vid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main( String[] args ) throws Exception {

        String osmFile = "src/main/resources/berlin-latest.osm.pbf";
        String graphFolder = "/home/vzh/IdeaProjects/Test_GH";

        // create singleton
        GraphHopper hopper = new GraphHopper().forServer();
        hopper.setOSMFile(osmFile);
        // where to store graphhopper files
        hopper.setGraphHopperLocation(graphFolder);
        hopper.setEncodingManager(new EncodingManager("car"));

        // now this can take minutes if it imports or a few seconds for loading
        // of course this is dependent on the area you import
        hopper.importOrLoad();

        /*// simple configuration of the request object, see the GraphHopperServlet class for more possibilities.
        double[] orig = new double[]{52.1536925d, 11.6395728d};
        double[] dest = new double[]{52.1610366d, 11.6322505d};

        GHRequest request = new GHRequest(orig[0], orig[1], dest[0], dest[1]);
        //request.putHint("calcPoints", false);
        //request.putHint("instructions", false);
        request.setWeighting("fastest");
        request.setVehicle("car"); // "car"
        GHResponse route = hopper.route(request);*/

        LocationIndex index = hopper.getLocationIndex();

        //System.out.println( "Hello World! \n" );

        // parsing file "Berlin_PoIs.json"
        Object obj = new JSONParser().parse(new FileReader("src/main/resources/Berlin_PoIs.json"));

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        // getting venues
        JSONArray venues = (JSONArray) ((Map)jo.get("response")).get("venues");

        App app = new App();

        // iterating the venues
        Iterator itr = venues.iterator();
        while (itr.hasNext())
        {
            int pid = 0;
            double lat = 0;
            double lon = 0;
            Iterator<Map.Entry> itr1 = ((Map) itr.next()).entrySet().iterator();
            while (itr1.hasNext()) {

                Map.Entry pair1 = itr1.next();
                if (pair1.getKey().equals("id")) {
                    pid = Integer.parseInt(pair1.getValue().toString());
                    //System.out.println(pair1.getKey() + " : " + pair1.getValue());
                }

                // iterating the locations of the venues
                if (pair1.getKey().equals("location")) {
                    Map location = ((Map)pair1.getValue());
                    Iterator<Map.Entry> itr2 = location.entrySet().iterator();
                    while (itr2.hasNext()) {
                        Map.Entry pair2 = itr2.next();
                        if (pair2.getKey().equals("lat")) {
                            lat = Double.parseDouble(pair2.getValue().toString());
                            //System.out.println(pair2.getKey() + " : " + pair2.getValue());
                        }
                        if (pair2.getKey().equals("lng")) {
                            lon = Double.parseDouble(pair2.getValue().toString());
                            //System.out.println(pair2.getKey() + " : " + pair2.getValue());
                        }
                    }
                }

            }

            // fetching the closest vertices to the pois and adding them to a database
            QueryResult qr = index.findClosest(lat, lon, EdgeFilter.ALL_EDGES );
            int vid = qr.getClosestNode();
            //System.out.println("VID: " + vid);
            System.out.println("PID: " + pid + ", VID: " + vid);
            app.insert(pid, vid);
            System.out.println("\n");
        }

        //app.insert(1, 7);
        //app.insert(2, 7);

        //double lat = 52.52446691047428;
        //double lon = 13.410111731235915;
        //QueryResult qr = index.findClosest(lat, lon, EdgeFilter.ALL_EDGES );
        //System.out.println("Closest node of this venue: " + qr.getClosestNode());
    }

}