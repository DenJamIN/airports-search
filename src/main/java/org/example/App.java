package org.example;

import java.io.IOException;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) throws IOException {
        ReadCSV readCSV = new ReadCSV();

        //readCSV.print(readCSV.read());

        Scanner scanner = new Scanner(System.in);
        String filter = scanner.nextLine();

        AirportFilter airportFilter = new AirportFilter(filter);
        airportFilter.splitFilterByExpressions();
    }
}
