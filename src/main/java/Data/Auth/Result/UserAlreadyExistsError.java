package Data.Auth.Result;

import java.util.Objects;

public record UserAlreadyExistsError(String message) implements AuthError {
    public UserAlreadyExistsError {
        Objects.requireNonNull(message);
    }
    public static UserAlreadyExistsError of(String message) {
        return new UserAlreadyExistsError(message);
    }
}