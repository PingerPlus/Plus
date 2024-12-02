package io.pinger.plus.orm;

public interface Expressions {

    static String incrementExpression(String columnName, Number value) {
        return String.format("`%s` + %s", columnName, value);
    }

    static String decrementExpression(String columnName, Number value) {
        return String.format("`%s` - %s", columnName, value);
    }

    static String sumExpression(String field) {
        return String.format("SUM(`%s`)", field);
    }

    static String as(String expression, String fieldName) {
        return String.format("%s as `%s`", expression, fieldName);
    }

    static String descending(String columnName) {
        return String.format("`%s` DESC", columnName);
    }

}
