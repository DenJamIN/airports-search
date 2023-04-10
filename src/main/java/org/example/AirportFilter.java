package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AirportFilter {
    private final String filter;
    private List<String> expressions = new ArrayList<>();
    private List<String> logicalOperators = new ArrayList<>();

    public AirportFilter(String filter) {
        this.filter = filter;
    }

    public List<String> getLogicalOperators() {
        return new ArrayList<>(logicalOperators);
    }

    public void setLogicalOperators(List<String> logicalOperators) {
        this.logicalOperators = logicalOperators;
    }

    public String getFilter() {
        return filter;
    }

    public List<String> getExpressions() {
        return new ArrayList<>(expressions);
    }

    public void setExpressions(List<String> expressions) {
        this.expressions = expressions;
    }

    // &|\|{2} - поиск разделителей выражений & и ||
    // =>|<=|<>|[<>=] - поиск операторов сравнения: для инама операторов; для сплита на составные (лево-право)
    // \d+ - поиск индекса колонки из сплита на составные(лево)
    // Значение сравнения - из сплита на составные(право)

    public List<List<String>> filter(List<List<String>> airports) {
        airports.stream().filter((airport) -> condition());
        return airports;
    }

    public boolean condition() {


        return true;
    }

    public boolean switchByLogical() {

        return true;
    }

    public boolean switchByComparison(List<String> airport, String expression) {
        String regex = "=>|<=|<>|[<>=]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        String[] values = expression.split(regex);
        String comparison = matcher.find() ? matcher.group() : ">";

        String valueLeft = regexNum(values[0], airport);
        String valueRight = regexNum(values[1], airport);
        boolean result = false;

        switch (comparison) {
            case ">":
                result = Integer.parseInt(valueLeft) > Integer.parseInt(valueRight);
                break;
            case "<":
                result = Integer.parseInt(valueLeft) < Integer.parseInt(valueRight);
                break;
            case "=":
                try {
                    int num1 = Integer.parseInt(valueLeft);
                    int num2 = Integer.parseInt(valueRight);
                    result = num1 == num2;
                } catch (NumberFormatException nfe) {
                    result = valueLeft.contains(valueRight);
                }
                break;
            case "<=":
                result = Integer.parseInt(valueLeft) <= Integer.parseInt(valueRight);
                break;
            case ">=":
                result = Integer.parseInt(valueLeft) >= Integer.parseInt(valueRight);
                break;
            case "<>":
                try {
                    int num1 = Integer.parseInt(valueLeft);
                    int num2 = Integer.parseInt(valueRight);
                    result = num1 != num2;
                } catch (NumberFormatException nfe) {
                    result = !valueLeft.contains(valueRight);
                }
                break;
        }

        return result;
    }

    private String regexNum(String value, List<String> airport) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(value);

        String result = value.contains("column") && matcher.find()
                ? airport.get(Integer.parseInt(matcher.group()) - 1)
                : value;
        return result.replaceAll("['\"]", "");
    }

    public void splitFilterByExpressions() {
        String regex = "&|\\|{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(filter);

        expressions = Arrays.asList(filter.split(regex));

        while (matcher.find()) {
            logicalOperators.add(matcher.group());
        }
    }

    public List<List<String>> splitExpressionsByComparisonOperators() {
        //левое и правое значение
        List<List<String>> comparisonOperatorsFromExpression = new ArrayList<>();
        expressions.forEach((expression) -> {
            String[] values = expression.split("=>|<=|<>|[<>=]");
            comparisonOperatorsFromExpression.add(Arrays.asList(values));
        });
        return comparisonOperatorsFromExpression;
    }

}
