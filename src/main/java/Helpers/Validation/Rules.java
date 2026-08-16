package Helpers.Validation;

import Data.Validation.Rule;
import Data.Validation.Simple;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Rules {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private Rules() {}

    public static Rule<String> notEmpty(String message) {
        return stringRule(value -> value == null || !value.isEmpty(), "not_empty", message);
    }

    public static Rule<String> required(String message) {
        return stringRule(value -> value != null && !value.toString().trim().isBlank(), "required", message);
    }

    public static Rule<String> minLength(int minimum, String message) {
        return stringRule(value -> value == null || value.length() >= minimum, "min_length", message);
    }

    public static Rule<String> alphabetic(String message) {
        return stringRule(value -> value == null || value.toString().matches("\\p{L}+"), "alphabetic", message);
    }
    public static Rule<String> alphanumeric(String message) {
        return stringRule(value -> value == null || value.toString().matches("[\\p{L}\\p{N}]+"), "alphanumeric", message);
    }
    public static Rule<String> email(String message) {
        return stringRule(value -> value == null || EMAIL.matcher(value).matches(), "email", message);
    }
    public static Rule<String> numeric(String message) {
        return stringRule(value -> value == null || value.toString().matches("\\p{N}+"), "numeric", message);
    }
    public static Rule<String> matches(String expression, String message) {
        var pattern = Pattern.compile(expression);
        return stringRule(value -> value == null || pattern.matcher(value).matches(), "matches", message);
    }
    public static Rule<String> maxLength(int maximum, String message) {
        return stringRule(value -> value == null || value.length() <= maximum, "max_length", message);
    }
    public static Rule<String> lengthBetween(int minimum, int maximum, String message) {
        return stringRule(
                value -> value == null || (value.length() >= minimum && value.length() <= maximum), "length_between", message);
    }
    public static Rule<String> url(String message) {
        return stringRule(value -> {
            if (value == null)
                return true;
            try {
                var uri = java.net.URI.create(value.toString());
                return ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
                        && uri.getHost() != null;
            } catch (IllegalArgumentException ignored) {
                return false;
            }
        }, "url", message);
    }
    public static Rule<Number> positive(String message) {
        return new Simple<>(Number.class, value -> value == null || value.doubleValue() > 0, "positive", message);
    }
    public static Rule<Number> negative(String message) {
        return new Simple<>(Number.class, value -> value == null || value.doubleValue() < 0, "negative", message);
    }
    public static Rule<Number> nonZero(String message) {
        return new Simple<>(Number.class, value -> value == null || value.doubleValue() != 0, "non_zero", message);
    }
    public static Rule<Collection<?>> minSize(int minimum, String message) {
        @SuppressWarnings("unchecked") var type = (Class<Collection<?>>) (Class<?>) Collection.class;
        return new Simple<>(type, value -> value == null || value.size() >= minimum, "min_size", message);
    }
    public static Rule<Collection<?>> maxSize(int maximum, String message) {
        @SuppressWarnings("unchecked") var type = (Class<Collection<?>>) (Class<?>) Collection.class;
        return new Simple<>(type, value -> value == null || value.size() <= maximum, "max_size", message);
    }

    private static Rule<String> stringRule(Predicate<CharSequence> predicate, String identifier, String message) {
        return new Simple<>(String.class, predicate::test, identifier, message);
    }
}
