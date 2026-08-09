package Data.Result;

import java.util.Objects;

public record Failure<T, E>(E error) implements Result<T, E> {
    public Failure {
        Objects.requireNonNull(error);
    }

    public static<T, E> Result<T, E> of(E error) {
        return new Failure<>(error);
    }
}
