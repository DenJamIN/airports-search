package org.example;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws IOException {
        ReadCSV readCSV = new ReadCSV();

        Scanner scanner = new Scanner(System.in);
        String filter = scanner.nextLine();
        String search = scanner.nextLine();

        long start = System.currentTimeMillis();

        AirportFilter airportFilter = new AirportFilter(filter);
        List<List<String>> airportsFiltered = airportFilter.filter(readCSV.read());

        SearchAirports searchAirports = new SearchAirports(airportsFiltered, search);
        List<List<String>> founded = searchAirports.search();
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;

        searchAirports.print(founded);
        System.out.println("Количество найденных строк: " + founded.size());
        System.out.println("Время, затраченное на поиск: " + elapsed);
    }
}
