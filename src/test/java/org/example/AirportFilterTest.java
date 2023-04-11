package org.example;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class AirportFilterTest {
    List<List<String>> airports;
    AirportFilter conjunction;
    AirportFilter disjunction;
    AirportFilter combo;
    AirportFilter one;

    @Before
    public void init() throws IOException {
        ReadCSV readCSV = new ReadCSV();
        airports = readCSV.read();

        conjunction = new AirportFilter("column[1]>10&column[5]='GKA'");
        disjunction = new AirportFilter("column[1]>10||column[5]='GKA'");
        combo = new AirportFilter("column[1]>10&column[5]='GKA'||column[1]=10");
        one = new AirportFilter("column[1]>10");
    }

    @Test
    public void splitByExpressions() {
        List<String> actual1 = conjunction.splitByExpressions(conjunction.getFilter());
        List<String> actual2 = disjunction.splitByExpressions(disjunction.getFilter());
        List<String> actual3 = combo.splitByExpressions(combo.getFilter());
        List<String> actual4 = one.splitByExpressions(one.getFilter());

        assertThat(actual1, is(asList("column[1]>10", "column[5]='GKA'")));
        assertThat(actual2, is(asList("column[1]>10", "column[5]='GKA'")));
        assertThat(actual3, is(asList("column[1]>10", "column[5]='GKA'", "column[1]=10")));
        assertThat(actual4, is(asList("column[1]>10")));
    }

    @Test
    public void collectLogicalOperatorsList(){
        List<String> actual1 = conjunction.collectLogicalOperatorsListFrom(conjunction.getFilter());
        List<String> actual2 = disjunction.collectLogicalOperatorsListFrom(disjunction.getFilter());
        List<String> actual3 = combo.collectLogicalOperatorsListFrom(combo.getFilter());
        List<String> actual4 = one.collectLogicalOperatorsListFrom(one.getFilter());

        assertThat(actual1, is(asList("&")));
        assertThat(actual2, is(asList("||")));
        assertThat(actual3, is(asList("&", "||")));
        assertThat(actual4, is(asList()));
    }

    @Test
    public void splitByComparisonOperator() {
        String[] actual1 = one.splitByComparisonOperator("column[1]<10");
        String[] actual2 = one.splitByComparisonOperator("column[1]>10");
        String[] actual3 = one.splitByComparisonOperator("column[1]<>10");
        String[] actual4 = one.splitByComparisonOperator("column[1]=10");
        String[] actual5 = one.splitByComparisonOperator("column[1]>=10");
        String[] actual6 = one.splitByComparisonOperator("column[1]<=10");

        String[] expected = new String[] {"column[1]", "10"};

        assertThat(actual1, is(expected));
        assertThat(actual2, is(expected));
        assertThat(actual3, is(expected));
        assertThat(actual4, is(expected));
        assertThat(actual5, is(expected));
        assertThat(actual6, is(expected));
    }

    @Test
    public void getComparisonOperator() {
        String actual1 = one.getComparisonOperator("column[1]<10");
        String actual2 = one.getComparisonOperator("column[1]>10");
        String actual3 = one.getComparisonOperator("column[1]<>10");
        String actual4 = one.getComparisonOperator("column[1]=10");
        String actual5 = one.getComparisonOperator("column[1]>=10");
        String actual6 = one.getComparisonOperator("column[1]<=10");

        assertThat(actual1, is("<"));
        assertThat(actual2, is(">"));
        assertThat(actual3, is("<>"));
        assertThat(actual4, is("="));
        assertThat(actual5, is(">="));
        assertThat(actual6, is("<="));
    }

    @Test
    public void substituteValuesFromAirport(){
        List<String> airport = airports.get(0);

        String actual1 = one.substituteValuesFromAirport("column[1]", airport);
        String actual2 = one.substituteValuesFromAirport("10", airport);
        String actual3 = one.substituteValuesFromAirport("'GKA'", airport);

        assertThat(actual1, is(airport.get(0)));
        assertThat(actual2, is("10"));
        assertThat(actual3, is("GKA"));
    }
}
