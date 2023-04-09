package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadCSV {
    private final static String COMMA_DELIMITER = ",";

    private List<List<String>> airports;
    private final FileReader reader;

    public ReadCSV() throws FileNotFoundException {
        reader = new FileReader("C:\\Users\\denla\\Рабочий стол\\airports.csv");
        airports = new ArrayList<>();
    }

    public List<List<String>> read() throws IOException {
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                airports.add(Arrays.asList(values));
            }
        }
        return airports;
    }

    public void print(List<List<String>> airports){
        airports.forEach((airport) -> {
            airport.forEach((data) -> System.out.print(data + ", "));
            System.out.println();
        });
    }
}
