package com.violina;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OperatorsTest {

    @Test
    public void testExecute() throws IOException {

        int start_point = 0;
        List<String> categories = new ArrayList<>();
        categories.add("restaurant");
        categories.add("pharmacy");
        categories.add("gas_station");
        categories.add("pub_bar");

        NearestNeighbor [][] table = new NearestNeighbor[5][7];
        fillTable(table);
        String t = "";
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                t += table[i][j] + " ";
            }
            t+= "\n";
        }
        System.out.println("Table: \n" + t);

        Operators operator = new Operators(start_point, categories, table);

        operator.pne_algorithm();

        //System.out.println(operator.pne_algorithm());

    }

    public NearestNeighbor parseNN(String s) {
        String[] values = s.split(":");
        int pid = Integer.parseInt(values[0]);
        int vid = Integer.parseInt(values[1]);
        double dist = Double.parseDouble(values[2]);
        NearestNeighbor nn = new NearestNeighbor(pid, vid, dist);
        return nn;
    }

    public void fillTable(NearestNeighbor [][] table) throws IOException {
        int c = 0;
        Path path = Paths.get("src/main/resources/table_test.txt");
        Scanner scanner = new Scanner(path);
        //System.out.println("Read text file using Scanner");
        //read line by line
        while(scanner.hasNextLine()){
            //process each line
            String line = scanner.nextLine();
            String[] values = line.split(" ");
            for (int i = 0; i < 7; i++) {
                table[c][i] = parseNN(values[i]);
            }
            c++;
        }
        scanner.close();
    }
}
