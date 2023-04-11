package org.example;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class SearchAirportsTest {
    List<List<String>> airports;

    @Before
    public void init() throws IOException {
        ReadCSV readCSV = new ReadCSV();
        airports = readCSV.read();
    }

    @Test
    public void testSearch() {
        SearchAirports searchAirports = new SearchAirports(airports, "Bo");
        long start = System.currentTimeMillis();
        searchAirports.search();
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        Assert.assertFalse("Время выполнения программы: " + elapsed + " (дольше 25мс)", elapsed > 25L);
    }
}