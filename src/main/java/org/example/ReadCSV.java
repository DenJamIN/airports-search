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

    private final List<List<String>> airports;
    private final FileReader reader;

    public ReadCSV() throws FileNotFoundException {
        reader = new FileReader("src/main/resources/airports.csv");
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
}
