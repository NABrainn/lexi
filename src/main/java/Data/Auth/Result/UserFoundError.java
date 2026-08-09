package Data.Auth.Result;

import java.util.Objects;

public record UserFoundError(String message) implements AuthError {
    public UserFoundError {
        Objects.requireNonNull(message);
    }
    public static UserFoundError of(String message) {
        return new UserFoundError(message);
    }
}