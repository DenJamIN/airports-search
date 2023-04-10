package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        return airports.stream().filter((airport) -> switchByLogical(airport, filter)).collect(Collectors.toList());
    }

    public boolean switchByLogical(List<String> airport, String filter) {
        String regex = "&|\\|{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(filter);

        expressions = Arrays.asList(filter.split(regex));

        while (matcher.find()) {
            logicalOperators.add(matcher.group());
        }

        List<Boolean> expressionResults = new ArrayList<>();
        expressions.forEach((expression) -> expressionResults.add(switchByComparison(airport, expression)));

        boolean result = false;
        while (logicalOperators.size() > 0) {
            int ampersandIndex = logicalOperators.indexOf("&");
            int priorityOpsIndex = ampersandIndex == -1 ? 0 : logicalOperators.indexOf("&");

            boolean expressionLeft = expressionResults.get(priorityOpsIndex);
            boolean expressionRight = expressionResults.get(priorityOpsIndex + 1);

            switch (logicalOperators.get(priorityOpsIndex)){
                case "&":
                    result = expressionLeft && expressionRight;
                    executeExpression(expressionResults, result, priorityOpsIndex);
                    break;
                case "||":
                    result = expressionLeft || expressionRight;
                    executeExpression(expressionResults, result, priorityOpsIndex);
                    break;
            }
        }

        /*объединить выражения и операторы в один список. Можно попробовать работать отдельно тогда:
        Операций всегда будет на одну меньше
        [выр1, выр2, выр3]
        [опр1, опр2]

        сначала мы находим индекс приоритетной операции через logicalOperators.indexOf("&")
        Если индекс 1, то выражения под индексом 0 и 2; если индекс 2, то выражения под индексом 1 и 3
        Левое выражение (i-операции - 1); Правое выражение (i-операции + 1);

        Важно:
        1. При вычислении выражения-участники должны удалиться из списка и должен добавиться в список их результат
        2. Из списка операций удаляется операции-участник вычисления

        Камень:
        Чтобы меня результат значения в списке, список должен быть булевый. А Значит выражения нужно сначала рассчитать,
        а после: добавить в их в новый список List<Boolean>
        На самом деле это просто - мы вызываем switchByComparison() для вычисления выражений, а он возвращает как раз булевое
        */
        return result;
    }

    private void executeExpression(List<Boolean> expressionResults, boolean result, int priorityOpsIndex) {
        expressionResults.remove(priorityOpsIndex +1);
        expressionResults.remove(priorityOpsIndex);
        expressionResults.add(priorityOpsIndex, result);
        logicalOperators.remove(priorityOpsIndex);
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
        List<List<String>> comparisonOperatorsFromExpression = new ArrayList<>();
        expressions.forEach((expression) -> {
            String[] values = expression.split("=>|<=|<>|[<>=]");
            comparisonOperatorsFromExpression.add(Arrays.asList(values));
        });
        return comparisonOperatorsFromExpression;
    }

}
