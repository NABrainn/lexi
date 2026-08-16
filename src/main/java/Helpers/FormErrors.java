package Helpers;

import io.javalin.validation.ValidationError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record FormErrors(Map<String, List<String>> fields, String global) {
    public static FormErrors empty() {
        return new FormErrors(Map.of(), null);
    }

    public static FormErrors of(Map<String, ? extends List<? extends ValidationError<?>>> errors) {
        var fields = errors.entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .map(ValidationError::getMessage)
                                .toList()
                ));

        return new FormErrors(fields, null);
    }

    public static FormErrors global(String message) {
        return new FormErrors(Map.of(), message);
    }

    public String field(String name) {
        var errors = fields.get(name);

        return errors == null || errors.isEmpty()
                ? null
                : errors.getLast();
    }

    public boolean has(String name) {
        return field(name) != null;
    }

    public boolean isEmpty() {
        return fields.isEmpty() && global == null;
    }
}