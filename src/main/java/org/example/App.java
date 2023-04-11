package org.example;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ReadCSV readCSV = new ReadCSV();
        List<List<String>> airports = readCSV.read();

        System.out.println("Введите фильтр:");
        String filter = scanner.nextLine();

        if(!filter.trim().isEmpty()){
            AirportFilter airportFilter = new AirportFilter(filter);
            airports = airportFilter.filter(airports);
        }

        while(true) {
            System.out.println("Введите строку для поиска (или введие '!quit'):");
            String search = scanner.nextLine();

            if (search.contains("!quit")) break;

            SearchAirports searchAirports = new SearchAirports(airports, search);

            long start = System.currentTimeMillis();
            List<List<String>> founded = searchAirports.search();
            long finish = System.currentTimeMillis();
            long elapsed = finish - start;

            searchAirports.print(founded);
            System.out.println("Количество найденных строк: " + founded.size());
            System.out.println("Время, затраченное на поиск: " + elapsed);
        }
    }
}
