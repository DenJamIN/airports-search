package org.example;

import java.util.List;
import java.util.stream.Collectors;

public class SearchAirports {

    private final List<List<String>> airports;
    private final String searchLine;

    public SearchAirports(List<List<String>> airports, String searchLine) {
        this.airports = airports;
        this.searchLine = searchLine.toLowerCase();
    }

    public List<List<String>> search() {
        return airports.stream()
                .filter((airport) -> {
                    String name = airport.get(1).toLowerCase().replaceAll("['\"]", "");
                    return name.startsWith(searchLine);
                })
                .collect(Collectors.toList());
    }

    public void print(List<List<String>> airports) {
        airports.forEach((airport) -> {
            System.out.print(airport.get(1) + "[");
            airport.forEach((value) -> System.out.print(value + ", "));
            System.out.print("]");
            System.out.println();
        });
    }
}
