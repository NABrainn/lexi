package Data.Result;

import java.util.Objects;

public record Success<T, E>(T value) implements Result<T, E> {
    public Success {
        Objects.requireNonNull(value);
    }

    public static <T, E> Result<T, E> of(T value) {
        return new Success<>(value);
    }
}
