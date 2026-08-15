package Data.Auth.Result;

public sealed interface LoginError extends AuthError permits UserDoesNotExistError{
}
