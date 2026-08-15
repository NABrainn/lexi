package Helpers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class FormBinder {
    public static <T> T bind(Context ctx, Class<T> clazz) {
        if (!clazz.isRecord()) {
            throw new IllegalArgumentException(clazz.getName() + " must be a record");
        }

        try {
            var components = clazz.getRecordComponents();

            var parameterTypes = Arrays.stream(components)
                    .map(RecordComponent::getType)
                    .toArray(Class<?>[]::new);

            var arguments = new Object[components.length];

            for (var i = 0; i < components.length; i++) {
                var component = components[i];
                var name = component.getName();
                var type = component.getType();

                if (type == UploadedFile.class) {
                    arguments[i] = ctx.uploadedFile(name);
                    continue;
                }

                if (type.isArray()) {
                    var elementType = type.getComponentType();

                    if (elementType == UploadedFile.class) {
                        var files = ctx.uploadedFiles(name);
                        var array = Array.newInstance(elementType, files.size());

                        for (var j = 0; j < files.size(); j++) {
                            Array.set(array, j, files.get(j));
                        }

                        arguments[i] = array;
                        continue;
                    }

                    var values = ctx.formParams(name);
                    var array = Array.newInstance(elementType, values.size());

                    for (var j = 0; j < values.size(); j++) {
                        Array.set(array, j, convert(values.get(j), elementType));
                    }

                    arguments[i] = array;
                    continue;
                }

                if (type == List.class) {
                    var genericType = component.getGenericType();

                    if (!(genericType instanceof ParameterizedType parameterizedType)) {
                        throw new IllegalArgumentException(name + " must declare its List element type");
                    }

                    var elementTypeArgument = parameterizedType.getActualTypeArguments()[0];

                    if (!(elementTypeArgument instanceof Class<?> elementType)) {
                        throw new IllegalArgumentException("Unsupported List element type for " + name);
                    }

                    if (elementType == UploadedFile.class) {
                        arguments[i] = new ArrayList<>(ctx.uploadedFiles(name));
                        continue;
                    }

                    var values = ctx.formParams(name);
                    var list = new ArrayList<>(values.size());

                    for (var value : values) {
                        list.add(convert(value, elementType));
                    }

                    arguments[i] = list;
                    continue;
                }

                var value = ctx.formParam(name);
                arguments[i] = convert(value, type);
            }

            var constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);

            return constructor.newInstance(arguments);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not create " + clazz.getName(), e);
        }
    }

    private static Object convert(String value, Class<?> type) {
        if (value == null) {
            if (type == boolean.class) {
                return false;
            }

            if (type.isPrimitive()) {
                throw new IllegalArgumentException("Missing value for primitive " + type.getName());
            }

            return null;
        }

        if (value.isEmpty() && type != String.class) {
            if (type.isPrimitive()) {
                throw new IllegalArgumentException("Empty value for primitive " + type.getName());
            }

            return null;
        }

        if (type.isEnum()) {
            return Enum.valueOf(type.asSubclass(Enum.class), value);
        }

        return switch (type.getName()) {
            case "java.lang.String" -> value;

            case "byte", "java.lang.Byte" -> Byte.parseByte(value);
            case "short", "java.lang.Short" -> Short.parseShort(value);
            case "int", "java.lang.Integer" -> Integer.parseInt(value);
            case "long", "java.lang.Long" -> Long.parseLong(value);
            case "float", "java.lang.Float" -> Float.parseFloat(value);
            case "double", "java.lang.Double" -> Double.parseDouble(value);

            case "boolean", "java.lang.Boolean" -> switch (value) {
                case "true", "on", "1" -> true;
                case "false", "off", "0" -> false;
                default -> throw new IllegalArgumentException("Invalid boolean value: " + value);
            };

            case "char", "java.lang.Character" -> {
                if (value.length() != 1) {
                    throw new IllegalArgumentException("Expected one character, got: " + value);
                }

                yield value.charAt(0);
            }

            case "java.math.BigDecimal" -> new BigDecimal(value);
            case "java.math.BigInteger" -> new BigInteger(value);

            case "java.util.UUID" -> UUID.fromString(value);

            case "java.time.LocalDate" -> LocalDate.parse(value);
            case "java.time.LocalTime" -> LocalTime.parse(value);
            case "java.time.LocalDateTime" -> LocalDateTime.parse(value);
            case "java.time.YearMonth" -> YearMonth.parse(value);

            default -> throw new IllegalArgumentException("Unsupported type: " + type.getName());
        };
    }
}