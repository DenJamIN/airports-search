package org.example;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class AirportFilterTest {

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
}
