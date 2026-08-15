package Data.Auth.Result.Error;

import java.util.Objects;

public record UserAlreadyExistsError(String message) implements RegisterError {
    public UserAlreadyExistsError {
        Objects.requireNonNull(message);
    }
    public static UserAlreadyExistsError of(String message) {
        return new UserAlreadyExistsError(message);
    }
}