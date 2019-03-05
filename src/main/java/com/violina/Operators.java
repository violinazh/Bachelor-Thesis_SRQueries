package com.violina;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Operators {

    private int start_point; // the vid of the start point given by the user
    private List<String> categories; // the set of categories, given by the user

    private NearestNeighbor [][] table;

    public Operators(int sp, List<String> categories, NearestNeighbor [][] table) {
        this.start_point = sp;
        this.categories = categories;
        this.table = table;
    }

    public Route pne_algorithm () {

        int initialCapacity = 15;
        Comparator<Route> comparator = new Comparator<Route>() {

            @Override
            public int compare(Route e1, Route e2) {
                return Double.compare(e1.getLength(), e2.getLength());
            }

        };
        PriorityQueue<Route> heap = new PriorityQueue<Route>(initialCapacity, comparator);

        // Adding the first route
        List<R_POI> pois = new ArrayList<>();
        NearestNeighbor nn = getNN(start_point, categories.get(0));
        pois.add(new R_POI(nn.getPID(),nn.getVID(), categories.get(0)));
        Route first = new Route(pois, nn.getDist());
        heap.add(first);

        Route candidate = new Route(new ArrayList<R_POI>(), 0);

        Route current = heap.poll();
        System.out.println("DEBUG | First route: \n" + current);

        while (current.getPois().size() < categories.size()) {

            // (a)
            Route newA = modifyRouteA(current);
            System.out.println("DEBUG | Modified route: \n" + newA);
            heap.add(newA);

            // Trimming (only one candidate PSR on the heap)
            /*if (newA.getPois().size() == categories.size()) {
                if (candidate.getLength() == 0) {
                    candidate = newA;
                    heap.add(newA);
                } else if (newA.getLength() < candidate.getLength()) {
                    candidate = newA;
                    heap.add(newA);
                } else {
                    // We don't add the new PSR
                }
            }*/

            // (b)
            //Route newB = modifyRouteB(current);
            // Trimming (only one candidate PSR on the heap)
            /*if (newB.getPois().size() == categories.size()) {
                if (candidate.getLength() == 0) {
                    candidate = newB;
                    heap.add(newB);
                } else if (newB.getLength() < candidate.getLength()) {
                    candidate = newB;
                    heap.add(newB);
                } else {
                    // We don't add the new PSR
                }
            }*/

            current = heap.poll();
        }

        return current;
    }

    private Route modifyRouteA(Route r) {
        Route route = new Route(r.getPois(), r.getLength());
        NearestNeighbor nn = getNN(route.getPois().get(route.getPois().size() - 1).getVid(),
                categories.get(route.getPois().size()));
        System.out.println("DEBUG | Next nn: " + nn);
        R_POI rpoi = new R_POI(nn.getPID(), nn.getVID(), categories.get(route.getPois().size()));
        route.addRpoi(rpoi);
        route.setLength(nn.getDist());
        return route;
    }

    private Route modifyRouteB(Route r) {
        Route route = new Route(r.getPois(), r.getLength());
        NearestNeighbor nn = getNN(route.getPois().get(route.getPois().size() - 2).getVid(),
                categories.get(route.getPois().size() - 1));
        R_POI rpoi = new R_POI(nn.getPID(), nn.getVID(), categories.get(route.getPois().size()));
        route.removeRpoi(route.getPois().size() - 1);
        route.addRpoi(rpoi);
        route.setLength(nn.getDist());
        return route;
    }

    private NearestNeighbor getNN(int vid, String category) {
        return table[vid][translateType(category)];
    }

    public int translateType(String category) {
        int type = 77;
        switch(category) {
            case "restaurant":
                type = 0;
                break;
            case "coffee_shop":
                type = 1;
                break;
            case "atm_bank":
                type = 2;
                break;
            case "movie_theater":
                type = 3;
                break;
            case "pharmacy":
                type = 4;
                break;
            case "pub_bar":
                type = 5;
                break;
            case "gas_station":
                type = 6;
                break;
            default:
                System.out.println("Error in translating category type.");
                break;

        }
        return type;
    }

}
