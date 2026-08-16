package Helpers;

import java.util.Collection;
import java.util.regex.Pattern;
import kotlin.jvm.functions.Function1;

public final class Rules {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private Rules() {}

    public static Function1<String, Boolean> notEmpty() {
        return value -> value == null || !value.isEmpty();
    }

    public static Function1<String, Boolean> required() {
        return value -> value != null && !value.isBlank();
    }

    public static Function1<String, Boolean> minLength(int minimum) {
        return value -> value == null || value.length() >= minimum;
    }

    public static Function1<String, Boolean> alphabetic() {
        return value -> value == null || value.matches("\\p{L}+");
    }
    public static Function1<String, Boolean> alphanumeric() {
        return value -> value == null || value.matches("[\\p{L}\\p{N}]+");
    }
    public static Function1<String, Boolean> email() {
        return value -> value == null || EMAIL.matcher(value).matches();
    }
    public static Function1<String, Boolean> numeric() {
        return value -> value == null || value.matches("\\p{N}+");
    }
    public static Function1<String, Boolean> matches(String expression) {
        var pattern = Pattern.compile(expression);
        return value -> value == null || pattern.matcher(value).matches();
    }
    public static Function1<String, Boolean> maxLength(int maximum) {
        return value -> value == null || value.length() <= maximum;
    }
    public static Function1<String, Boolean> lengthBetween(int minimum, int maximum) {
        return value -> value == null || (value.length() >= minimum && value.length() <= maximum);
    }
    public static Function1<String, Boolean> url() {
        return value -> {
            if (value == null)
                return true;
            try {
                var uri = java.net.URI.create(value);
                return ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
                        && uri.getHost() != null;
            } catch (IllegalArgumentException ignored) {
                return false;
            }
        };
    }
    public static Function1<Number, Boolean> positive() {
        return value -> value == null || value.doubleValue() > 0;
    }
    public static Function1<Number, Boolean> negative() {
        return value -> value == null || value.doubleValue() < 0;
    }
    public static Function1<Number, Boolean> nonZero() {
        return value -> value == null || value.doubleValue() != 0;
    }
    public static Function1<Collection<?>, Boolean> minSize(int minimum) {
        return value -> value == null || value.size() >= minimum;
    }
    public static Function1<Collection<?>, Boolean> maxSize(int maximum) {
        return value -> value == null || value.size() <= maximum;
    }
}
