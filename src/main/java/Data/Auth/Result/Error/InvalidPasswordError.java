package Data.Auth.Result.Error;

public record InvalidPasswordError(String message) implements LoginError {
    public static InvalidPasswordError of(String message){
        return new InvalidPasswordError(message);
    }
}
