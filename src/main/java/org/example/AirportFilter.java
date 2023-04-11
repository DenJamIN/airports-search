package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AirportFilter {
    private final String filter;

    public AirportFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    public List<List<String>> filter(List<List<String>> airports) {
        return airports.stream()
                .filter(this::isFilteredByExpressions)
                .collect(Collectors.toList());
    }

    public boolean isFilteredByExpressions(List<String> airport) {
        List<String> expressions = splitByExpressions(filter);
        List<String> logicalOperators = collectLogicalOperatorsListFrom(filter);
        List<Boolean> expressionResults = collectExpressionsResultByAirport(expressions, airport);

        boolean result = false;
        while (logicalOperators.size() > 0) {
            int ampersandIndex = logicalOperators.indexOf("&");
            int priorityOpsIndex = ampersandIndex == -1 ? 0 : logicalOperators.indexOf("&");

            boolean expressionLeft = expressionResults.get(priorityOpsIndex);
            boolean expressionRight = expressionResults.get(priorityOpsIndex + 1);

            switch (logicalOperators.get(priorityOpsIndex)) {
                case "&":
                    result = expressionLeft && expressionRight;
                    reduceExpressionResultsAndLogicalOperators(expressionResults, logicalOperators, result, priorityOpsIndex);
                    break;
                case "||":
                    result = expressionLeft || expressionRight;
                    reduceExpressionResultsAndLogicalOperators(expressionResults, logicalOperators, result, priorityOpsIndex);
                    break;
            }
        }

        return result;
    }

    public List<String> splitByExpressions(String text) {
        return Arrays.asList(text.split("&|\\|{2}"));
    }

    public List<String> collectLogicalOperatorsListFrom(String text) {
        Pattern pattern = Pattern.compile("&|\\|{2}");
        Matcher matcher = pattern.matcher(text);
        List<String> values = new ArrayList<>();
        while (matcher.find()) {
            values.add(matcher.group());
        }
        return values;
    }

    public List<Boolean> collectExpressionsResultByAirport(List<String> expressions, List<String> airport) {
        return expressions.stream()
                .map((expression) -> solveExpression(airport, expression))
                .collect(Collectors.toList());
    }

    private void reduceExpressionResultsAndLogicalOperators(List<Boolean> expressionResults, List<String> logicalOperators,
                                                            boolean result, int priorityOpsIndex) {
        expressionResults.remove(priorityOpsIndex + 1);
        expressionResults.remove(priorityOpsIndex);
        expressionResults.add(priorityOpsIndex, result);
        logicalOperators.remove(priorityOpsIndex);
    }

    public boolean solveExpression(List<String> airport, String expression) {
        String[] values = splitByComparisonOperator(expression);
        String comparison = getComparisonOperator(expression);
        String valueLeft = substituteValuesFromAirport(values[0], airport);
        String valueRight = substituteValuesFromAirport(values[1], airport);

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
    
    public String[] splitByComparisonOperator(String expression) {
        return expression.split(">=|<=|<>|[<>=]");
    }

    public String getComparisonOperator(String expression){
        Pattern pattern = Pattern.compile(">=|<=|<>|[<>=]");
        Matcher matcher = pattern.matcher(expression);
        return matcher.find() ? matcher.group() : ">";
    }

    public String substituteValuesFromAirport(String value, List<String> airport) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(value);

        String result = value.contains("column") && matcher.find()
                ? airport.get(Integer.parseInt(matcher.group()) - 1)
                : value;
        return result.replaceAll("['\"]", "");
    }
}
