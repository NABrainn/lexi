package Data.Auth.Result;

public record UserDoesNotExistError(String message) implements LoginError {
    public static UserDoesNotExistError of(String message) {
        return new UserDoesNotExistError(message);
    }
}
