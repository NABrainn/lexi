package Data.Auth.Result.Error;

public sealed interface LoginError extends AuthError permits UserDoesNotExistError, InvalidPasswordError {
}
