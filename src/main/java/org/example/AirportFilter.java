package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AirportFilter {
    private final String filter;
    private List<String> expressions;

    public AirportFilter(String filter) {
        this.filter = filter;
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

    //? добавить метод, с которого начинается вся эта фильтрация: разбиваем на выражения -> разбиваем на значения
    //! фильтрация подразумевает в себе реализацию bool, следовательно, наши фильтры по итоге должны решать выражение

    public List<List<String>> filter(List<List<String>> airports){
        airports.stream().filter((airport) -> condition());
        return airports;
    }

    public boolean condition(){
        //Получаем выражения
        splitFilterByExpressions();
        //? Каждое выражение надо вычислить: column[1]>10 & column[5]=’GKA' - выражение должно участвовать в фильтрации полностью
        //? Чтобы решить задачу: нужно подставить соответствующие значения и собрать единое логическое выражение
        //? Можно использовать matcher.replaceAll
        //! Фильтр - это строка, чтобы вычислить выражение

        return true;
    }

    public void splitFilterByExpressions(){
        expressions = Arrays.asList(filter.split("&|\\|{2}"));
    }

    public List<List<String>> splitExpressionsByComparisonOperators(){
        //левое и правое значение
        List<List<String>> choto = new ArrayList<>();
        expressions.forEach((expression) -> {
            String[] values = expression.split("=>|<=|<>|[<>=]");
            choto.add(Arrays.asList(values));
        });
        return choto;
    }

}
