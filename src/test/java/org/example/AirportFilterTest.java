package org.example;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class AirportFilterTest {
    List<List<String>> airports;

    @Before
    public void init() throws IOException {
        ReadCSV readCSV = new ReadCSV();
        airports = readCSV.read();
    }

    @Test
    public void splitFilterByExpressions() {
        AirportFilter conjunction = new AirportFilter("column[1]>10&column[5]='GKA'");
        AirportFilter disjunction = new AirportFilter("column[1]>10||column[5]='GKA'");
        AirportFilter combo = new AirportFilter("column[1]>10&column[5]='GKA'||column[1]=10");
        AirportFilter one = new AirportFilter("column[1]>10");

        conjunction.splitFilterByExpressions();
        disjunction.splitFilterByExpressions();
        combo.splitFilterByExpressions();
        one.splitFilterByExpressions();

        assertThat(conjunction.getExpressions(), is(Arrays.asList("column[1]>10", "column[5]='GKA'")));
        assertThat(disjunction.getExpressions(), is(Arrays.asList("column[1]>10", "column[5]='GKA'")));
        assertThat(combo.getExpressions(), is(Arrays.asList("column[1]>10", "column[5]='GKA'", "column[1]=10")));
        assertThat(one.getExpressions(), is(Arrays.asList("column[1]>10")));

        assertThat(conjunction.getLogicalOperators(), is(Arrays.asList("&")));
        assertThat(disjunction.getLogicalOperators(), is(Arrays.asList("||")));
        assertThat(combo.getLogicalOperators(), is(Arrays.asList("&", "||")));
        assertThat(one.getLogicalOperators(), is(Arrays.asList()));
    }

    @Test
    public void splitExpressionsByComparisonOperators() {
        AirportFilter many = new AirportFilter("column[1]>10&column[5]='GKA'");
        AirportFilter one = new AirportFilter("column[1]>10");

        many.splitFilterByExpressions();
        one.splitFilterByExpressions();

        assertThat(many.splitExpressionsByComparisonOperators(), is(Arrays.asList(Arrays.asList("column[1]", "10"), Arrays.asList("column[5]", "'GKA'"))));
        assertThat(one.splitExpressionsByComparisonOperators(), is(Arrays.asList(Arrays.asList("column[1]", "10"))));
    }

    @Test
    public void switchByComparison() {
        AirportFilter airportFilter = new AirportFilter("column[1]>10&column[5]='GKA'");

        airportFilter.splitFilterByExpressions();

        boolean actual1 = airportFilter.switchByComparison(airports.get(0), airportFilter.getExpressions().get(0));
        boolean actual2 = airportFilter.switchByComparison(airports.get(0), airportFilter.getExpressions().get(1));

        assertThat(actual1, is(false));
        assertThat(actual2, is(true));
    }


}
